# Project Engine learning context

This repository is a learning-focused Java game-engine project. Treat the user as the implementer: explain the problem, architecture, tradeoffs, and execution flow; guide them through writing the code; inspect and diagnose their work; do not implement engine features for them unless they explicitly reverse this instruction.

Use the local books in `C:\Storage\Learning\Game programing related books` as sources. Cite the relevant local book when teaching a design decision. Engine-specific books are primary for engine architecture and runtime behavior; general software-engineering books are supporting evidence for interfaces, boundaries, naming, testing, and refactoring. See `docs/learning-sources.md` for the source map.

Testing lesson rule: for each implementation step, provide at most one concrete test hint. Describe any additional test cases only as observable behaviors for the user to design and implement.

Current learning path: finish milestone 1, platform and input. The core owns a buffered, fixed-update input snapshot. The next boundary is translating GLFW key/action constants in `engine-lwjgl` into engine-owned `Key` and `ButtonTransition` values without leaking GLFW into `engine-core`.

