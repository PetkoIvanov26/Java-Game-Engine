# Scope and roadmap

## Product definition

The end goal is a reusable engine runtime capable of supporting multiple small-to-medium desktop 2D and 3D games. Games provide simulation rules, assets, scenes, and presentation choices through engine APIs.

“Any game” means the architecture is not tied to one genre. It does not mean the first version supports every platform, rendering technique, networking model, or AAA content workflow.

## Scope rules

Every milestone must:

1. produce something observable in the sandbox;
2. add deterministic tests for non-graphics behavior;
3. document ownership and failure behavior;
4. leave the full build green;
5. avoid empty speculative subsystems.

## Milestones

### 0. Foundation - current

- Multi-module build with one-way dependencies.
- Fixed-step scheduler and engine loop.
- Desktop window/context lifecycle.
- Neutral sandbox and headless unit tests.

Definition of done: `gradlew build` succeeds and the sandbox opens a clear-color window that closes cleanly.

### 1. Platform and input

- Keyboard, mouse buttons, cursor, scroll, and window events.
- Separate held state from one-shot frame events.
- Queue one-shot input so catch-up simulation ticks do not repeat it.
- Frame statistics and controlled shutdown.

Definition of done: the sandbox visualizes input state and every edge event is consumed exactly once.

### 2. Graphics pipeline fundamentals

- OpenGL debug callback and capability reporting.
- Shader compilation/linking with readable diagnostics.
- Vertex/index buffers and vertex-array objects.
- One indexed triangle/quad rendered without per-frame native leaks.

Definition of done: a documented CPU-to-GPU data path and a rendered primitive that survives window resize.

### 3. Math, transforms, camera, and 2D renderer

- Vectors, matrices, coordinate spaces, and interpolation exercises.
- Transform components and an orthographic camera.
- Textures, sprites, sprite batching, and debug primitives.
- A tiny Pong/Breakout-style validation game outside the engine modules.

Definition of done: a complete small 2D game proves the public API.

### 4. Assets and lifetime

- Stable asset identifiers and typed loaders.
- Texture/shader caches and deterministic native cleanup.
- Background file I/O and decoding with main-thread GPU upload.
- Development-mode hot reload only after normal loading is reliable.

### 5. World model

- Generational entity handles.
- Component storage and query masks.
- Explicit system order and deferred structural changes.
- Scene/world loading boundary.

Definition of done: a headless simulation can be recorded, replayed, and produce the same result.

### 6. Collision and basic physics

- Rays, circles/spheres, AABBs, and intersection tests.
- Spatial grid or another measured broad phase.
- Fixed-step velocity/acceleration integration and simple collision response.

Use third-party physics only after this foundation makes its tradeoffs understandable.

### 7. First 3D slice

- Perspective camera, depth testing, meshes, materials, and lighting basics.
- Model import as an offline or asset-loading concern.
- Frustum culling after profiling demonstrates the need.

### 8. Animation, audio, tools, and packaging

Add these as separate vertical slices: local animation clocks and blending; OpenAL playback and streaming; debug overlays and profilers; then packaged runtime images.

### 9. Concurrency and advanced systems

Profile a complete game before creating a job system. Good early candidates are asset decoding, visibility preparation, animation pose batches, and independent spatial queries.

## Out of scope until a game proves the need

- Visual editor and full asset authoring suite.
- Multiplayer replication and rollback.
- Vulkan or multiple simultaneous graphics backends.
- Console, mobile, and web ports.
- General-purpose scripting language integration.
- Commercial-grade rigid-body solver.
- One thread per engine subsystem.
