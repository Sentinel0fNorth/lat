# Testing Instructions for Attendance App

## Prerequisites

1. **Backend Setup:**
   - Ensure your Spring Boot backend is running on port 8080
   - Make sure PostgreSQL is running and accessible
   - The backend should be accessible at `http://localhost:8080`

2. **Android Setup:**
   - Android Studio with API level 24+ device/emulator
   - Internet permission enabled (already configured)

## Testing Steps

### 1. Start the Backend
```bash
cd C:\Users\Deependra\OneDrive\Desktop\lat
./mvnw.cmd spring-boot:run
```

### 2. Start the Android App
- Open Android Studio
- Run the app on emulator or device
- The app will connect to `http://10.0.2.2:8080` (emulator) or your local IP

### 3. Test Basic Flow

#### Login/Guest Access:
- **Option 1:** Use credentials: username="teacher", password="password"
- **Option 2:** Click "Guest Access" to skip login

#### Student Management:
- The app will load demo students if no students exist in backend
- Students displayed: John Doe (CS101), Jane Smith (CS102), etc.

#### Take Attendance:
1. Select/deselect students using checkboxes or by tapping cards
2. Use "Select All" or "Clear All" buttons for bulk operations
3. Change date using "Change Date" button if needed
4. Click "Submit" to save attendance

#### View History:
- Use the menu button (three dots) to access "Attendance History"
- View previously submitted attendance records

### 4. API Testing

#### Test Student Creation:
Currently, student creation is implemented in MainActivity but the UI button may not be visible. You can:
- Add students directly through backend API
- Or modify the layout to add a "Add Student" button

#### Test Backend Connectivity:
The app will:
- Try to load students from backend first
- Fall back to demo data if backend is unreachable
- Show appropriate error messages for network issues

### 5. Common Issues & Solutions

#### Connection Failed:
- Ensure backend is running on port 8080
- Check if emulator can reach 10.0.2.2:8080
- For physical device, use your computer's IP address instead

#### Authentication Errors:
- Backend authentication is disabled for testing
- If you get 401 errors, ensure SecurityConfig is properly configured

#### Layout Issues:
- Some layout files may need adjustments based on actual resource IDs
- Check logcat for any missing resources

## Expected Behavior

1. **Successful Connection:** App loads real students from backend
2. **Offline Mode:** App works with demo data when backend is unreachable
3. **Attendance Submission:** Currently simulated (shows success message)
4. **Error Handling:** Proper error messages for network/API failures

## Notes for Full Integration

The current implementation includes:
- ✅ Complete UI with attendance taking
- ✅ API client setup with proper endpoints
- ✅ Student management models matching backend
- ✅ Error handling and offline fallback
- ⚠️ Attendance submission is currently simulated due to model differences

For complete backend integration:
1. Align backend attendance model to accept bulk submissions
2. Implement proper attendance history retrieval
3. Add real student creation UI