# Railway Deployment Guide for Spring Boot Application

## Issues Fixed

### 1. **Project Structure Conflicts** ✅
- **Problem**: Mixed Spring Boot (Maven) and Android (Gradle) projects in same directory
- **Solution**: Created `.railwayignore` to exclude Android project and explicit build script

### 2. **Build System Detection** ✅
- **Problem**: Railway was confused between Maven and Gradle
- **Solution**: Explicit build script (`build.sh`) forces Maven usage

### 3. **Health Check Mismatch** ✅
- **Problem**: `railway.json` expected `/actuator/health`, app provides `/api/ping`
- **Solution**: Updated health check path to `/api/ping`

### 4. **Configuration Inconsistencies** ✅
- **Problem**: Different JVM arguments across files
- **Solution**: Standardized memory settings and port configuration

## Current Configuration

### Files Updated:
- ✅ `.railwayignore` - Excludes Android project
- ✅ `nixpacks.toml` - Fixed build configuration
- ✅ `railway.json` - Corrected health check path
- ✅ `Procfile` - Standardized JVM arguments
- ✅ `build.sh` - Explicit Maven build script

### Health Endpoints Available:
- `GET /api/ping` - Simple ping endpoint (used for health checks)
- `GET /api/status` - Detailed status information
- `GET /actuator/health` - Spring Actuator health (if you enable it)

## Environment Variables Needed

Set these in Railway dashboard:

### Database Configuration:
```bash
DATABASE_URL=postgresql://user:password@host:port/database
PGUSER=your_pg_user
PGPASSWORD=your_pg_password
```

### Application Configuration:
```bash
PORT=8080  # Railway sets this automatically
SPRING_PROFILES_ACTIVE=prod
```

## Testing Your Deployment

1. **Local Build Test:**
   ```bash
   ./mvnw clean package -DskipTests
   java -jar target/lat-0.0.1-SNAPSHOT.jar
   ```

2. **Health Check:**
   ```bash
   curl http://localhost:8080/api/ping
   curl http://localhost:8080/api/status
   ```

3. **Database Connection:**
   - Ensure PostgreSQL database is connected in Railway
   - Check logs for connection errors

## Common Issues & Solutions

### Build Fails:
1. Check Railway logs for build errors
2. Verify all dependencies are in `pom.xml`
3. Ensure Java 17 is specified correctly

### Health Check Fails:
1. Verify app is starting on correct port (`$PORT`)
2. Check `/api/ping` endpoint is accessible
3. Look for startup errors in logs

### Database Connection Issues:
1. Verify `DATABASE_URL` environment variable
2. Check PostgreSQL service is running
3. Validate connection credentials

### Memory Issues:
1. Current setting: `-Xmx512m` (should be sufficient for Railway)
2. Monitor memory usage in Railway dashboard
3. Adjust if needed, but Railway has memory limits

## Next Steps for Deployment

1. **Commit all changes:**
   ```bash
   git add .
   git commit -m "Fix Railway deployment configuration"
   git push
   ```

2. **Deploy to Railway:**
   - Connect your repository to Railway
   - Set environment variables (DATABASE_URL, etc.)
   - Deploy and monitor logs

3. **Test deployed application:**
   - Visit your Railway URL + `/api/ping`
   - Check `/api/status` for detailed info
   - Test your API endpoints

## Monitoring

Once deployed, monitor:
- Application logs in Railway dashboard
- Memory and CPU usage
- Database connection status
- Response times for health checks

## Support

If you encounter issues:
1. Check Railway build logs first
2. Verify environment variables are set
3. Test locally with same configuration
4. Check this guide for common solutions