# SideEffectsLab

This repository contains hands-on Jetpack Compose implementations demonstrating the correct usage of all major Compose Side-Effect APIs.

## Effect API Choices & Rationale
1. **Task 1 (Splash Screen):** Uses `LaunchedEffect` paired with `rememberUpdatedState` to prevent stale lambdas without resetting timer coroutines on every parent recomposition.
2. **Task 2 (Snackbar Button):** Uses `rememberCoroutineScope` because coroutines launched from user event callbacks (`onClick`) cannot utilize Composable-only effects like `LaunchedEffect`.
3. **Task 3 (Lifecycle Logger):** Uses `DisposableEffect` with `lifecycleOwner` as key to safely register observers on entry and clean them up via `onDispose` on exit.
4. **Task 4 (SideEffect & Analytics):** Uses `SideEffect` to publish Compose state changes to external non-Compose objects safely after successful commits.
5. **Task 5 (Scroll Optimization):** Uses `derivedStateOf` to suppress high-frequency scroll offset recompositions and `snapshotFlow` to turn Compose state changes into a reactive event stream.
6. **Task 6 (produceState Bonus):** Uses `produceState` to bridge asynchronous suspend functions cleanly into observable Compose `State` with built-in cancellation and conflation.

Answers for theoretical questions in file "Side-Effects Practice Theoretical Question Answers.pdf"
