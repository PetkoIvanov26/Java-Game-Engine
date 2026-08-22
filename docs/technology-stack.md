# Technology stack

## Selected foundation

| Area | Choice | Reason |
|---|---|---|
| Language/runtime | Java 25 | Current installed LTS-era toolchain, records and modern language features, strong profiling and tooling |
| Build | Gradle Wrapper 9.2 | Already present and officially supports running on Java 25 |
| Native access | LWJGL 3.4.2 | Direct, low-level Java bindings rather than a higher-level game framework |
| Window/input | GLFW through LWJGL | Small cross-platform desktop boundary |
| Graphics | OpenGL 3.3 Core initially | Broad enough desktop support and a manageable API for learning the GPU pipeline |
| Math | Hand-written exercises, then JOML 1.10.9 | Learn the operations first; use a mature allocation-conscious library in engine runtime code |
| Images/fonts | STB through LWJGL when the asset milestone starts | Small native loaders suitable for the first content pipeline |
| Tests | JUnit Jupiter 5.14.2 | Headless deterministic tests for clocks, math, ECS, collision, and assets |
| Logging | `System.Logger` initially | Avoid another dependency until structured logging requirements are known |
| Packaging | Gradle distributions, then `jlink`/`jpackage` | Add only after the runtime is useful |

Official compatibility references:

- OpenJDK 25: https://openjdk.org/projects/jdk/25/
- Gradle Java compatibility: https://docs.gradle.org/current/userguide/compatibility.html
- LWJGL releases: https://github.com/LWJGL/lwjgl3/releases
- JOML releases: https://github.com/JOML-CI/JOML/releases
- JUnit documentation: https://docs.junit.org/5.14.2/user-guide/

## Deliberately deferred

- Vulkan: do not maintain two rendering backends while learning the first one.
- OpenAL: add it at the audio milestone, not as an unused dependency.
- Box2D or another physics engine: first implement basic collision and integration ourselves.
- Lombok: keep the public engine model visible and unsurprising.
- Dependency injection frameworks: constructor wiring is sufficient at this scale.
- Scripting, networking, editor UI, and serialization frameworks: each requires a concrete game/tool use case first.

OpenGL is the learning and first production backend, not a permanent promise that every future platform must use it. A second backend is considered only after the renderer contract is proven by a complete game.
