# Substack Widget

A native Android home screen widget (Jetpack Compose + Glance) that surfaces the latest posts from your favorite Substack publications — grouped, glanceable, and built to replace mindless scrolling with something worth reading.

*Same thumb, better scroll.*

## Why this exists

Endless Reels scrolling gives you nothing back. Substack scratches the same itch — a feed you scroll through — except what's in it is actually worth your time. There was no non-browser, home-screen-native way to read it, so this exists to close that gap: glance at your home screen, catch up on what your favorite writers published, tap through to read, done.

No algorithm trying to keep you there. Just headlines, quietly minding their own business.

## What it does

- Add any number of Substack publications by handle (the part before `.substack.com`)
- See their latest posts **grouped by publication** in one scrollable widget
- Tap any post to open it in your browser
- Widget refreshes automatically after you add/remove a publication — no need to reopen the app
- Full light/dark theming matched to Substack's own orange-and-cream palette
- Works standalone from the app icon *or* as a true home-screen widget — same screen, two entry points

## Tech stack

Kotlin · Jetpack Compose · Glance (widget UI) · Hilt · Retrofit + OkHttp · Jetpack DataStore · WorkManager · kotlinx.serialization

Clean MVVM under the hood — a pure-Kotlin domain layer, Retrofit/DataStore in a data layer behind a repository interface, Compose ViewModels for the app screens, and Glance for the widget itself. No public Substack API exists, so every publication's RSS feed (`<handle>.substack.com/feed`) is parsed directly. Widget refreshes run through WorkManager rather than a plain coroutine, so they survive the app being backgrounded or killed.

## Setup

**Fastest — grab the APK**

Download from [Releases](#) and install. Ships ready to go — no configuration required before first launch.

**From source**

1. Clone the repo and open in Android Studio
2. Run on a device or emulator
3. On first launch you'll see a short instructions screen — tap **Add Widget to Home Screen** (or long-press your home screen → Widgets → Substack Widget)
4. Add publications by handle (e.g. `androidengineers`) on the screen that follows
5. Tap **Done** — the widget updates within seconds, no restart needed

Opening the app again later goes straight to your publication list if a widget is already placed; otherwise you'll see the instructions screen again.

## Roadmap

- [ ] Widget size variants (small/medium/large layouts)
- [ ] Per-widget publication lists (multiple widgets, different publications each)
- [ ] Manual pull-to-refresh from within the app
- [ ] Unit tests across the domain/use-case layer
- [ ] Discovery layer — curated genre lists (tech, psychology, philosophy, etc.) as a browsing aid on top of manual publication entry
