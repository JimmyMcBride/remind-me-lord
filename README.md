# 🙏 Remind Me, Lord

**Remind Me, Lord** is a personal Android app to encourage and uplift users by showing them Bible
verses tailored to their current mood, struggle, or need. It’s designed to blend spiritual
encouragement with clean, modern design and thoughtful user experience.

## ✨ Vision

The goal is to build a **context-aware lock screen and widget app** that:

- Displays relevant Bible verses based on the user’s selected tags (like "anxiety", "courage", or "
  faith")
- Automatically rotates verses daily without frequent repetition
- Allows searching and filtering verses manually when needed
- Eventually includes lock screen support and reminders to return to God’s Word

The long-term idea is to help people carry Scripture with them every day, with personalization and a
sense of gentle divine nudging.

## ✅ What's Been Built So Far

- 🔍 **Home Screen UI** with:
    - Search bar
    - Dynamic tag filters with a clean chip-based UI
    - Paginated list of verses using **Paging 3**
- 📖 **Verses Seeded** from a local `verses.json` file with tags like `["peace", "faith", "anxiety"]`
- 🔄 **Reactive Filtering**: Automatically updates the list as the user searches or toggles tags
- 🧠 **Custom Tag Ordering**: Selected tags appear first in the UI for a better UX
- ⚡ **Offline-first design** using **Room** as the local database
- 🔌 Dependency injection powered by **Hilt**

## 🛠 Technical Highlights

### 🧱 Architecture

- **MVVM** with a clean separation between UI, ViewModel, and Repository
- **Room Database** with a custom `VerseEntity` and type converters for `List<String>`
- **Paging 3** with **raw SQL queries** for maximum flexibility when filtering
- **Jetpack Compose** for all UI components (search, chips, verse cards, lazy columns)
- **Hilt DI** for scalable, testable dependencies

### 🔎 Search + Filter Implementation

```kotlin
snapshotFlow { searchQuery to selectedTags }
    .distinctUntilChanged()
    .flatMapLatest { (query, tags) ->
        verseRepository.getFilteredPaged(query, tags)
    }
```

This reactive approach ensures that the UI always reflects current input without triggering
unnecessary reloads.

### 📦 Data Seeding

```kotlin
val input = context.assets.open("verses.json").bufferedReader().use { it.readText() }
val verses = Json.decodeFromString<List<VerseSeed>>(input)
dao.insertAll(verses.map { it.toEntity() })
```

The database is initially populated with tagged verses from a local JSON file.

## 🔜 What's Coming Next

- 📱 **Home screen widget** with auto-refresh daily
- 🔒 **Lock screen integration** for Android 14+
- 💡 Tag suggestions and moods (e.g., "I'm feeling anxious")
- 🌙 Dark mode theming
- ☁️ Sync + cloud backup (future vision)

## 🙌 Contributing

This is currently a solo project, but I'm always open to feedback and ideas! Want to help test,
contribute a verse pack, or work on a feature together? Let’s connect.

## 📖 License

This project is open source and freely available under the MIT License.

> _“Your word is a lamp to my feet and a light to my path.” — Psalm 119:105_

## Current Progress

[remindmelord-search-filter-tags-demo.webm](https://github.com/user-attachments/assets/c5d90685-733e-4c55-8ff7-e1e6a9e6d9e0)
