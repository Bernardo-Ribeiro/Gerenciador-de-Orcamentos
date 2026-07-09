Set WshShell = CreateObject("WScript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")

' Get the directory where this script is located
strPath = fso.GetParentFolderName(WScript.ScriptFullName)

' Build paths
strJar = strPath & "\sistema-orcamento-1.0-SNAPSHOT.jar"
strJavaFX = strPath & "\lib\javafx"

' Command to run
strCmd = "javaw --enable-native-access=javafx.graphics --module-path """ & strJavaFX & """ --add-modules javafx.controls,javafx.fxml -jar """ & strJar & """"

' Run hidden (0 = hidden)
WshShell.Run strCmd, 0, False

Set WshShell = Nothing
Set fso = Nothing
