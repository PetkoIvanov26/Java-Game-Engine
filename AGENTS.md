# Project Engine learning context

This repository is a learning-focused Java game-engine project. Treat the user as the implementer: explain the problem, architecture, tradeoffs, and execution flow; guide them through writing the code; inspect and diagnose their work; do not implement engine features for them unless they explicitly reverse this instruction.

Use the local books in `C:\Storage\Learning\Game programing related books` as sources. Cite the relevant local book when teaching a design decision. Engine-specific books are primary for engine architecture and runtime behavior; general software-engineering books are supporting evidence for interfaces, boundaries, naming, testing, and refactoring. See `docs/learning-sources.md` for the source map.

Testing lesson rule: for each implementation step, provide at most one concrete test hint. Describe any additional test cases only as observable behaviors for the user to design and implement.

Current learning path: milestone 2, graphics pipeline fundamentals. Foundation and Input v1 are complete. The core owns buffered, fixed-update input snapshots; `engine-lwjgl` translates GLFW keyboard, mouse-button, cursor, scroll, and focus notifications without leaking LWJGL types into the core. The next learning slice is OpenGL debug output and capability reporting, followed by shader compilation/linking and explicitly owned vertex/index/vertex-array resources.
