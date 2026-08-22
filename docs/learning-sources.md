# Learning source map

This file is persistent context for future Project Engine learning sessions. It is a routing map, not a claim that every book should dictate the architecture. Before citing a source, inspect the relevant section in the local copy.

## Teaching protocol

- The user writes engine feature code; guidance explains what problem each type and operation solves.
- Review and diagnose the user's implementation without silently replacing it.
- Give at most one concrete test hint per implementation step. Express further tests only as behavioral requirements.
- Prefer a small vertical slice, a green build, and an observable sandbox result before expanding a subsystem.
- Distinguish engine policy from platform mechanism: `engine-core` owns portable concepts; adapters translate GLFW/OpenGL/native details.

## Primary engine and game-programming sources

- `Game Engine Architecture.pdf` - runtime foundations, engine subsystems, human-interface devices, polling/events, cross-platform input abstraction, remapping, rendering, animation, physics, and tools.
- `Game Coding Complete 4th.pdf` - application/platform layer, game views, event systems, process management, resource caching, actor/game logic, and testing-friendly input translation.
- `Game Programming Patterns.pdf` - command, game loop, update, event queue, component, state, object pool, data locality, and other focused patterns with their tradeoffs.
- `Game Programming Algorithms and Techniques.pdf` - practical engine and gameplay algorithms.
- `Game Programming Golden Rules.pdf` - performance-conscious game-programming principles.
- `Multi-Threaded Game Engine Design.pdf` - concurrency and engine threading; defer application until profiling demonstrates a need.
- `Video Game Optimization.pdf` - measurement and optimization workflows; use after a working vertical slice exists.

## New software-construction sources added 2026-08-22

### Clean Code

Local file: `Clean Code.pdf` (456 PDF pages).

Relevant routing:

- Chapters 2-3: intention-revealing names and focused functions.
- Chapter 6: objects versus data structures and the Law of Demeter.
- Chapter 8, Boundaries: constrain third-party APIs behind application-specific interfaces; use isolated learning tests to explore an external library.
- Chapter 9: readable, focused unit tests.
- Chapters 10-11: cohesive classes and separating system construction from use.
- Chapter 13: concurrency hazards and limiting shared mutable state.

Use critically rather than as absolute rules. For this engine, Chapter 8 directly supports keeping GLFW constants and callbacks inside `engine-lwjgl` instead of exposing them through the core API.

### Refactoring: Improving the Design of Existing Code

Local file: `Refactoring.pdf` (337 PDF pages; Martin Fowler et al., first-edition material).

Relevant routing:

- Chapters 1-2: behavior-preserving change in small, test-backed steps.
- Chapter 3: code smells as prompts for investigation, not automatic verdicts.
- Chapter 4: self-testing code as the safety net for structural change.
- Chapters 6-11: composing methods, moving responsibilities, organizing data, simplifying conditionals and calls, and improving generalization.
- Chapter 12: larger separations such as domain from presentation.

For this project, use its small-step loop when moving responsibilities across `engine-core`, `engine-lwjgl`, and `sandbox`: change one boundary, compile, test, then continue.

### Code Complete, Second Edition

Local file: `Code Complete, 2nd Edition.pdf` (952 PDF pages).

Relevant routing:

- Chapters 3-5: prerequisites, architecture, construction decisions, abstraction, information hiding, cohesion, and coupling.
- Chapter 6: narrow, consistent class interfaces and encapsulation.
- Chapters 7-9: routine design, defensive programming, and construction by precise pseudocode.
- Chapters 10-19: variables, data types, control flow, and table-driven techniques.
- Chapters 20-24: software quality, developer testing, debugging, and safe refactoring.
- Chapters 25-26: measure before code tuning and optimize iteratively.

For this engine, Chapter 6 supports exposing `Input` to game code while retaining mutable `BufferedInput` ownership inside engine composition.

## Mathematics, geometry, graphics, and physics

- `3D Math Primer for Graphics and Game Development 2nd Edition.pdf`
- `Foundations of Game Engine Development, Volume 1 Mathematics.pdf`
- `Mathematics for 3D Game Programming and Computer Graphics, 3rd Edition, 2011.pdf`
- `Computational Geometry.pdf`
- `Geometric Tools for Computer Graphics.pdf`
- `Fundamentals of Computer Graphics, Fourth Edition.pdf` (a duplicate copy with `(1)` also exists)
- `Real Time Rendering.pdf`
- `Physically Based Rendering.pdf`
- `Real-Time 3D Rendering with DirectX and HLSL.epub`
- `Physics for Game Developers 2nd Ed (2013).pdf`

Use these during math, transforms, cameras, rendering, collision, and physics milestones. Preserve coordinate-space and convention details when citing them.

## AI and networking

- `Artificial Intelligence A Modern Approach (3rd Edition).pdf`
- `Artificial Intelligence for Games.pdf`
- `Unity AI Game Programming - Second Edition.pdf`
- `Multiplayer Game Programming.pdf`

These are deferred until a vertical slice creates a concrete AI or networking requirement.

## Additional code-quality source

- `The.Art.of.Readable.Code.pdf` - readability, naming, control flow, and communicating intent.

