# BookMate

BookMate is a mobile application built natively for Android that provides book recommendations. Whether you're an avid reader looking for your next book or just curious about what's trending, BookMate helps you find the perfect read.

## Features

- **Personalized Recommendations**: Get book recommendations tailored to your preferences.
- **Search Functionality**: Search for books by title, author, or genre.
- **Book Details**: View detailed information about books including summaries, ratings, and reviews.
- **Favorites**: Save your favorite books for quick access later.
- **User Reviews**: Read and write reviews for books.

## Installation

To get started with BookMate, follow these steps:

1. **Clone the repository:**
   ```
   git clone https://github.com/yourusername/BookMate.git

2. Open the project in Android Studio:
   - Open Android Studio.
   - Click on File → Open and select the BookMate directory.


3. Build and run the project:
   - Click on Build → Rebuild Project to build the project.
   - Once the build is complete, click on Run -> Run 'app' to install and run the app on an emulator or connected device.

## Commit Message Format
We use a structured commit message format to keep the history clean and readable. Please follow this format for your commit messages:
- **feat**: Changes about addition or removal of a feature.
  Ex: `feat: add table on landing page`, `feat: remove table from landing page`
- **fix**: Bug fixing, followed by the bug.
  Ex: `fix: illustration overflows in mobile view`
- **docs**: Update documentation (README.md)
- **style**: Updating style, and not changing any logic in the code (reorder imports, fix whitespace, remove comments)
- **chore**: Installing new dependencies, or bumping deps.
  Example: `chore: update Gradle to version 6.8`

- **refactor**: Changes in code, same output, but different approach.
  Example: `refactor: improve performance of book search algorithm`

- **ci**: Update GitHub workflows, husky.
  Example: `ci: add build check on pull request`

- **test**: Update testing suite, cypress files.
  Example: `test: add unit tests for RecommendationService`

- **revert**: When reverting commits.
  Example: `revert: revert commit 12345`

- **perf**: Fixing something regarding performance (deriving state, using memo, callback).
  Example: `perf: optimize image loading in book details`

### Pull Request Process

1. **Create new issue** and create your branch from `staging` based on the issue created.
2. **Write clear, concise commit messages** using the format above.
3. **Ensure the code passes all tests** and follows the style guidelines.
4. **Open a Pull Request**, providing a clear description of the changes and linking to any relevant issues.
5. **Tag others** as the reviewers of your pull request.

### Code Style

- Follow the **Kotlin coding conventions**.
- Ensure all **imports are organized** and unused imports are removed.
- Use **meaningful variable and method names**.

## License

This project is licensed under the Capstone Project Team

```
Happy coding!
```
