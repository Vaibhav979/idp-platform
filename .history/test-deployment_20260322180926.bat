@echo off
powershell -Command "$b=@{name='my-app';repoUrl='https://github.com/user/repo'}|ConvertTo-Json; $p=Invoke-RestMethod http://localhost:808
