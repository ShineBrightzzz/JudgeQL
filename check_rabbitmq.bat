@echo off
echo Checking RabbitMQ status...

rem Try to connect to RabbitMQ Management API (default port 15672)
curl -s -o nul -w "%%{http_code}" http://localhost:15672/api/overview -u guest:guest > %TEMP%\rabbitmq_status.txt
set /p status=<%TEMP%\rabbitmq_status.txt
del %TEMP%\rabbitmq_status.txt

if "%status%" == "200" (
    echo RabbitMQ is running.
    echo.
    echo Getting RabbitMQ server information:
    curl -s http://localhost:15672/api/overview -u guest:guest | findstr /C:"rabbitmq_version" /C:"cluster_name"
) else (
    echo RabbitMQ appears to be stopped or not installed.
    echo.
    echo If RabbitMQ is not installed, you can download it from:
    echo https://www.rabbitmq.com/download.html
    echo.
    echo If RabbitMQ is installed but not running, try starting it with:
    echo net start RabbitMQ
    echo.
    echo You can also check the installation status with:
    echo rabbitmqctl status
)

echo.
echo Testing connection to RabbitMQ AMQP port (5672):
telnet localhost 5672 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Connection to RabbitMQ AMQP port successful.
) else (
    echo Connection to RabbitMQ AMQP port failed.
    echo Make sure RabbitMQ is running and port 5672 is accessible.
)
