@echo off
title SkillSync AI Server
echo ===========================================
echo   Starting SkillSync AI Server...
echo ===========================================
cd /d "%~dp0"
call .\mvnw.cmd spring-boot:run
pause
