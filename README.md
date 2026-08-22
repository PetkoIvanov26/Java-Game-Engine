# Project Engine

Project Engine is a learning-focused Java game engine. Its purpose is to expose the low-level systems behind a game engine while remaining reusable by multiple games and tools.

The engine is currently at milestone 0: a tested fixed-step core, a desktop LWJGL host, and a neutral sandbox window.

## Project structure

- `engine-core` - dependency-free lifecycle, timing, and host abstractions. It must remain runnable in headless tests.
- `engine-lwjgl` - GLFW/OpenGL desktop integration and native resource ownership.
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

The sandbox currently opens an empty OpenGL 3.3 window and runs simulation at a fixed 60 Hz. Close the window to stop the engine.

## Design principles

1. Build one working vertical slice at a time.
2. Keep simulation deterministic and testable without graphics.
3. Keep native API details behind the desktop adapter.
4. Make ownership and cleanup explicit.
5. Introduce abstractions after a concrete need appears.
6. Profile before adding concurrency or specialized data structures.

See [architecture](docs/architecture.md), [technology stack](docs/technology-stack.md), [roadmap](docs/roadmap.md), and the persistent [learning source map](docs/learning-sources.md).
