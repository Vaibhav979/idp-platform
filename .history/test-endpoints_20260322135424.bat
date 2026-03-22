@echo off
REM Test endpoints to verify no 405

echo Creating project...
powershell -Command "[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.SecurityProtocolType]::Tls12; $body = @{name='test'; repoUrl='git://test'} | ConvertTo-Json; Invoke-RestMethod -Uri 'http://localhost:8080/projects' -Method Post -ContentType 'application/json' -Body $body"

REM Wait a bit
timeout /t 5

echo Triggering deployment for project 1...
powershell -Command "[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.SecurityProtocolType]::Tls12; Invoke-RestMethod -Uri 'http://localhost:8080/deployments/projects/1' -Method Post"

REM Wait for async sim
timeout /t 10

echo Getting deployment 1...
powershell -Command "[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.SecurityProtocolType]::Tls12; Invoke-RestMethod -Uri 'http://localhost:8080/deployments/1' -Method Get"

pause
