# High-level architecture

## Direction of dependencies

```text
Game or Tool
     |
     v
Public Engine API
     |
     v
Engine Core  <----  Headless Tests
     |
     v
Desktop Adapter (LWJGL)
     |
     v
GLFW / OpenGL / OS / GPU
```

The dependency direction is the most important rule: games use the engine; the engine never imports game code. Platform code implements narrow interfaces owned by the core. Simulation code does not call GLFW or OpenGL.

## Runtime ownership

`DesktopApplication` owns the native desktop lifetime. It creates and closes `GlfwWindow`. `EngineLoop` owns execution order and timing. `EngineClient` owns game or tool state.

```text
DesktopApplication
  create native window/context
  EngineLoop.run
    client.initialize
    while running
      host.pollEvents
      measure real elapsed time
      schedule 0..N fixed updates
      client.fixedUpdate(fixedDelta)
      client.render(interpolationAlpha)
      host.present
    client.shutdown
  destroy native window/context
```

The loop uses three distinct notions of time:

- Real time is measured by a monotonic clock.
- Simulation time advances only through fixed updates.
- Render time uses an interpolation alpha between completed simulation states.

## Current modules

### `engine-core`

This is pure Java and has no native or graphics dependency. It contains:

- the public `EngineClient`, `EngineControl`, and `EngineHost` contracts;
- validated engine timing configuration;
- fixed-step scheduling;
- the main engine loop.

The core must always be testable with fake clocks and fake hosts.

### `engine-lwjgl`

This is the first platform adapter. It owns GLFW initialization, the window, the OpenGL context, native callbacks, buffer presentation, and cleanup.

It will later own concrete input and rendering implementations. LWJGL types should not leak into `engine-core`.

### `sandbox`

The sandbox is a disposable client of the engine. Every subsystem first appears here as a small demonstration. It prevents example gameplay from becoming an engine dependency.

## Planned subsystem boundaries

Do not create empty modules for every future feature. Add these boundaries incrementally:

- Platform: windows, input devices, monitors, timing, and native events.
- Rendering: render commands, GPU resources, cameras, materials, and frame graphs.
- Assets: identifiers, loaders, caches, dependency tracking, and hot reload.
- World: entity handles, component storage, system scheduling, and scenes.
- Collision/physics: geometric queries, broad phase, narrow phase, and integration.
- Animation: clips, local clocks, poses, blending, and state machines.
- Audio: devices, buffers, sources, streaming, and spatialization.
- Tools: logging, metrics, debug draw, inspection, importers, and eventually an editor.

A subsystem becomes a separate Gradle module only when it needs an independently testable dependency boundary or an optional runtime dependency.

## Threading policy

The initial engine is deliberately single-threaded. GLFW event handling and OpenGL commands stay on the owning thread. Background work is introduced first for independent asset I/O and decoding. Simulation jobs are added only after profiling identifies a suitable data-parallel workload.

A job must operate on explicitly owned input/output data. Shared mutable world state is not a job API.

## Memory and resource policy

Java manages ordinary engine objects. Native resources do not follow Java object lifetime automatically, so every window, callback, GPU object, audio buffer, and native allocation must have an explicit owner and deterministic cleanup, normally through `AutoCloseable`.

Per-frame code should avoid accidental allocations, but allocation-free code is a measured optimization rather than a blanket requirement.
