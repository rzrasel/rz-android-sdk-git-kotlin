@echo off
:: @echo off
REM set /p input="Enter ID: "
REM echo %input%
REM pause -

@echo off
set /p input="Enter Commit Text: "
:: cls
:: echo value %input%
:: set newinput=%input%
set date=%DATE%
set commit=git commit -m
set gitcommit=%commit% "%input% ⇒ %date% %TIME%"
git add .
:: git commit -m "COMMIT COMPLETE A"
:: echo %gitcommit%
%gitcommit%
git pull
git push --all
echo Process Complete Press Enter...
pause
