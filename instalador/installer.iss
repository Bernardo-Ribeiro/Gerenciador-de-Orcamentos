#define MyAppName "Sistema de Orçamento"
#define MyAppVersion "1.0"
#define MyAppPublisher "Gráfica"
#define MyAppExeName "SistemaOrcamento.exe"
#define MyAppURL "https://www.grafica.com.br"

[Setup]
AppId={{A3B5E8C1-9D2F-4E6A-8B3C-1F5D7E9A2B4C}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={autopf}\{#MyAppName}
DefaultGroupName={#MyAppName}
DisableProgramGroupPage=yes
LicenseFile=
OutputDir=..\dist\instalador
OutputBaseFilename=SetupSistemaOrcamento_{#MyAppVersion}
SetupIconFile={#SourcePath}\..\sistema-orcamento\src\main\resources\com\grafica\img\app.ico
Compression=lzma2/max
SolidCompression=yes
WizardStyle=modern
PrivilegesRequired=admin
ArchitecturesAllowed=x64
ArchitecturesInstallIn64BitMode=x64

[Languages]
Name: "brazilianportuguese"; MessagesFile: "compiler:Languages\BrazilianPortuguese.isl"
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "{#SourcePath}\..\sistema-orcamento\target\SistemaOrcamento.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "{#SourcePath}\..\sistema-orcamento\target\sistema-orcamento-1.0-SNAPSHOT.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "{#SourcePath}\..\sistema-orcamento\target\lib\*"; DestDir: "{app}\lib"; Flags: recursesubdirs createallsubdirs ignoreversion
Source: "{#SourcePath}\..\sistema-orcamento\database\*"; DestDir: "{app}\database"; Flags: recursesubdirs createallsubdirs
Source: "{#SourcePath}\..\dist\portatil\IniciarSistema.vbs"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\SistemaOrcamento.exe"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\SistemaOrcamento.exe"; Tasks: desktopicon

[Run]
Filename: "{app}\SistemaOrcamento.exe"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent
