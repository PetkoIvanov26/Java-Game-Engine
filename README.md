# Project Engine

Project Engine is a learning-focused Java game engine. Its purpose is to expose the low-level systems behind a game engine while remaining reusable by multiple games and tools.

Foundation and basic desktop input are complete. The current milestone is graphics pipeline fundamentals: exposing the CPU-to-GPU path through a small, explicitly owned OpenGL vertical slice.

## Project structure

- `engine-core` - dependency-free lifecycle, timing, fixed-update input snapshots, and host abstractions. It must remain runnable in headless tests.
- `engine-lwjgl` - GLFW/OpenGL desktop integration, native input translation, and native resource ownership.
- `sandbox` - a replaceable engine client used to exercise features. It is not part of the engine library.
- `docs` - architecture, technology, and scope decisions.

A real game will eventually be another module or repository that depends on the engine. The engine must never depend on a particular game.

## Commands

Use JDK 25.

```powershell
.\gradlew.bat build
.\gradlew.bat test
.\gradlew.bat :sandbox:run
```

The sandbox opens an OpenGL 3.3 window, runs simulation at a fixed 60 Hz, and exercises keyboard, mouse-button, cursor, and scroll input. Escape requests a clean engine shutdown.

## Design principles

1. Build one working vertical slice at a time.
2. Keep simulation deterministic and testable without graphics.
3. Keep native API details behind the desktop adapter.
4. Make ownership and cleanup explicit.
5. Introduce abstractions after a concrete need appears.
6. Profile before adding concurrency or specialized data structures.

See [architecture](docs/architecture.md), [technology stack](docs/technology-stack.md), [roadmap](docs/roadmap.md), and the persistent [learning source map](docs/learning-sources.md).
