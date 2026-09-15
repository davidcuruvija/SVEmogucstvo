# SVEmogućstvo

Native Android app for [SVEmogućstvo](https://svemogucstvo.com) (brand mark "CBE"), a fashion label founded by Marina Ćuruvija. The app mirrors the store's website experience (browsing, cart, and checkout), backed by the same live WooCommerce store.

## Features

- **Home** - rotating hero campaign imagery, featured products pulled from WooCommerce
- **Shop** - product catalog with search, category filters, and sorting
- **Product details** - image gallery, color/size variation selection, sale pricing
- **Cart & Checkout** - full cash-on-delivery order flow against a live WooCommerce store
- **Gallery** - offline, bundled lookbook of past collections
- **About / Contact** - offline, hardcoded content (no network dependency)
- Custom brand theming: adaptive launcher icon, splash screen, typography, and color palette sourced from the live site

## Tech Stack

- **Kotlin** + **Jetpack Compose** (Material3)
- **Hilt** for dependency injection
- **Retrofit** + **Gson** for networking against the WooCommerce REST API (`wc/v3`) and Store API (`wc/store/v1`)
- **Coil3** for image loading
- **Navigation Compose** for screen navigation and the app's navigation drawer
- **JUnit** + **Robolectric** for unit tests

## Architecture

See [`docs/architecture.md`](docs/architecture.md) for a diagram of how screens, ViewModels, repositories, and the WooCommerce APIs fit together.
