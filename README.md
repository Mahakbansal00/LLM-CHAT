# LLM Chat - React + Java Full Stack Application

A full-stack chat application with React frontend and Java Spring Boot backend that integrates with OpenAI's API.

## Features

- React frontend with Vite
- Java Spring Boot backend
- OpenAI GPT integration
- CORS configuration for cross-origin requests
- Real-time chat interface

## Setup Instructions

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Set your OpenAI API key as an environment variable:
   ```bash
   export OPENAI_API_KEY=your_api_key_here
   ```

3. Compile and run the backend:
   ```bash
   mvn spring-boot:run
   ```
   The backend will start on http://localhost:8080

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies (already done):
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```
   The frontend will start on http://localhost:3000

## Usage

1. Make sure both backend and frontend are running
2. Open http://localhost:3000 in your browser
3. Type a message and click "Send" to chat with the AI assistant

## API Endpoints

- `POST /api/chat` - Send a message to the AI and get a response

## Project Structure

```
├── backend/
│   ├── src/main/java/com/example/chatapi/
│   │   ├── ChatApiApplication.java  # Main Spring Boot app
│   │   └── ChatController.java      # REST controller
│   ├── src/main/resources/
│   │   └── application.properties   # Configuration
│   └── pom.xml                     # Maven configuration
└── frontend/
    ├── src/
    │   ├── App.jsx                 # Main React component
    │   ├── main.jsx                # React entry point
    │   └── styles.css              # CSS styles
    ├── index.html                  # HTML template
    ├── package.json                # Node.js dependencies
    └── vite.config.js              # Vite configuration
```

## Dependencies

### Backend
- Spring Boot 3.3.3
- Jackson for JSON processing
- Lombok for reducing boilerplate code

### Frontend
- React 18
- Axios for HTTP requests
- Vite for development server
# LLM-CHAT
