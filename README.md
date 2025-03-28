# Euroleague Tiebreaker App

This Euroleague Tiebreaker app helps determine the head-to-head (H2H) ranking between multiple teams. If the H2H ranking cannot be automatically calculated, users can manually input their predictions for the remaining matches.

## Features
- Calculates H2H rankings between multiple teams
- Allows users to enter predictions for unresolved matchups

## Live Application
You can access the app at: [Euroleague Tiebreaker](https://euroleague-tiebreaker.com)

### Running with Docker
You can run the application using Docker:

```sh
# Clone the repository
git clone https://github.com/nika998/Euroleague-Tiebreaker.git
```

```sh
# Run the application using Docker Compose
docker-compose up -d
```

## Usage
1. Choose the teams you want to compare.
2. The app will check for a valid H2H ranking.
3. If an automatic ranking is not possible, input your predictions for the remaining matches.
4. View the updated rankings instantly.

## Technologies Used
- **Backend:** Spring Boot (Java)
- **Frontend:** Thymeleaf, HTML, CSS, JavaScript
- **Containerization:** Docker, Docker Compose
- **Hosting:** Hetzner

## Contributing
Feel free to open an issue or create a pull request if you want to contribute.
