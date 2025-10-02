# LAT - Learning Attendance Tracker

A comprehensive attendance management system consisting of a Spring Boot backend API and an Android mobile application for tracking student attendance in educational institutions.

## 📱 Project Overview

The Learning Attendance Tracker (LAT) is a full-stack solution designed to streamline attendance management for educational institutions. It provides a robust backend API for data management and a user-friendly Android application for teachers to take attendance on-the-go.

## 🏗️ Project Structure

```
lat/
├── src/                          # Spring Boot Backend
│   ├── main/
│   │   ├── java/com/example/lat/
│   │   │   ├── controller/       # REST API Controllers
│   │   │   ├── service/          # Business Logic Layer
│   │   │   ├── model/            # JPA Entity Models
│   │   │   ├── repository/       # Data Access Layer
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   └── config/           # Security & Configuration
│   │   └── resources/            # Application Properties
│   └── test/                     # Backend Tests
├── MyApplication/                # Android Application
│   ├── app/
│   │   ├── src/main/
│   │   │   ├── java/             # Kotlin Source Code
│   │   │   │   ├── model/        # Data Models
│   │   │   │   ├── api/          # Retrofit API Client
│   │   │   │   ├── adapter/      # RecyclerView Adapters
│   │   │   │   └── utils/        # Utility Classes
│   │   │   └── res/              # Android Resources
│   │   └── build.gradle.kts      # Android Build Configuration
│   └── gradle/                   # Gradle Wrapper
├── pom.xml                       # Maven Dependencies
└── mvnw, mvnw.cmd               # Maven Wrapper
```

## ⚡ Key Features

### Backend (Spring Boot)
- **Student Management**: CRUD operations for student records
- **Attendance Tracking**: Mark attendance with date and status
- **Bulk Operations**: Submit attendance for multiple students at once
- **Statistics & Reports**: Generate attendance statistics and identify low-attendance students
- **RESTful API**: Comprehensive REST endpoints for all operations
- **Database Integration**: PostgreSQL database with JPA/Hibernate
- **Security**: Spring Security configuration (currently disabled for testing)

### Android Application
- **User Authentication**: Login system with guest access option
- **Intuitive UI**: Material Design-based user interface
- **Attendance Taking**: 
  - Select/deselect students with checkboxes or card taps
  - Bulk select/clear all functionality
  - Date picker for historical attendance
  - Real-time student count display
- **Student Management**: Add, edit, and manage student records
- **Attendance History**: View past attendance records
- **Low Attendance Reports**: Identify students with poor attendance
- **Offline Support**: Works with demo data when backend is unavailable
- **Export Functionality**: Export attendance data
- **Network Error Handling**: Graceful error handling and user feedback

## 🛠️ Technologies Used

### Backend Stack
- **Framework**: Spring Boot 3.5.6
- **Language**: Java 21
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security
- **Build Tool**: Maven
- **Additional Libraries**:
  - Lombok (for reducing boilerplate code)
  - Spring Boot Test (for testing)

### Android Stack
- **Language**: Kotlin 2.0.21
- **UI Framework**: Android Views with Material Design
- **Architecture**: MVVM pattern with Repository pattern
- **Networking**: Retrofit 2 for API calls
- **Build System**: Gradle with Kotlin DSL
- **UI Components**:
  - RecyclerView for student lists
  - Material Design components
  - Custom adapters and layouts
- **Target SDK**: Android API 24+
- **Libraries**:
  - AndroidX Core KTX 1.17.0
  - Lifecycle Runtime KTX 2.9.4
  - Compose BOM 2025.09.00 (for future Compose integration)

## 🚀 Getting Started

### Prerequisites
- **Backend**: Java 21, PostgreSQL, Maven
- **Android**: Android Studio, Android SDK API 24+
- **Database**: PostgreSQL server running on localhost:5432

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd lat
   ```

2. **Configure Database**
   - Create PostgreSQL database named `attendance_db`
   - Update credentials in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/attendance_db
   spring.datasource.username=postgres
   spring.datasource.password=12345678
   ```

3. **Run the Backend**
   ```bash
   ./mvnw spring-boot:run
   ```
   The API will be available at `http://localhost:8080`

### Android App Setup

1. **Open Android Studio**
   ```bash
   cd MyApplication
   # Open this directory in Android Studio
   ```

2. **Build and Run**
   - Sync the project with Gradle files
   - Run the app on an emulator or device
   - The app connects to `http://10.0.2.2:8080` (emulator) or your local IP

### Testing the Application

Please refer to [`TESTING_INSTRUCTIONS.md`](MyApplication/TESTING_INSTRUCTIONS.md) for detailed testing procedures.

## 📊 API Endpoints

### Student Management
- `GET /students` - Get all students
- `GET /students/{id}` - Get student by ID
- `POST /students` - Create new student
- `PUT /students/{id}` - Update student
- `DELETE /students/{id}` - Delete student

### Attendance Management
- `POST /attendance` - Mark single attendance
- `POST /attendance/bulk` - Bulk attendance submission
- `GET /attendance?date={date}` - Get attendance by date
- `GET /attendance/student/{studentId}` - Get student attendance history
- `GET /attendance/statistics` - Get attendance statistics
- `GET /attendance/statistics/low-attendance` - Get low attendance report

## 🎯 Usage

### For Teachers (Android App)
1. **Login**: Use credentials `teacher`/`password` or access as guest
2. **Take Attendance**: 
   - Select students present using checkboxes or card taps
   - Use bulk select/clear for convenience
   - Choose date if marking historical attendance
   - Submit to save records
3. **View Reports**: Access attendance history and low-attendance reports via menu
4. **Manage Students**: Add or edit student information

### For Administrators (API)
- Use REST API endpoints directly for system integration
- Access comprehensive attendance statistics
- Manage students and attendance records programmatically

## 🔧 Configuration

### Backend Configuration
Key configuration in `application.properties`:
- Database connection settings
- Server port (default: 8080)
- JPA/Hibernate settings
- Authentication token (for future use)

### Android Configuration
Key configuration in `gradle/libs.versions.toml`:
- Android Gradle Plugin version
- Kotlin version
- AndroidX library versions
- API endpoint configuration in `ApiClient.kt`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🐛 Known Issues

- Attendance submission in Android app is currently simulated due to model differences
- Student creation UI button may not be visible in the current Android layout
- Authentication is disabled in backend for testing purposes

## 🔮 Future Enhancements

- Complete backend-frontend model alignment
- Real-time attendance synchronization
- Biometric authentication for students
- Advanced reporting and analytics
- Push notifications for attendance reminders
- Multi-language support
- Dark theme support in Android app

## 📞 Support

For support and questions, please create an issue in the repository or contact the development team.

---

**Made with ❤️ for educational institutions**