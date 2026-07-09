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
SetupIconFile=src\main\resources\com\grafica\img\app.ico
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
Source: "target\SistemaOrcamento.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "target\sistema-orcamento-1.0-SNAPSHOT.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "database\*"; DestDir: "{app}\database"; Flags: recursesubdirs createallsubdirs

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\SistemaOrcamento.exe"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\SistemaOrcamento.exe"; Tasks: desktopicon

[Run]
Filename: "{app}\SistemaOrcamento.exe"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

[Code]
function InitializeSetup(): Boolean;
var
  ResultCode: Integer;
begin
  Result := True;
  
  // Verifica se Java 21+ está instalado
  if not RegKeyExists(HKEY_LOCAL_MACHINE, 'SOFTWARE\JavaSoft\JDK\21') then
  begin
    if not RegKeyExists(HKEY_LOCAL_MACHINE, 'SOFTWARE\JavaSoft\JDK\22') and
       not RegKeyExists(HKEY_LOCAL_MACHINE, 'SOFTWARE\JavaSoft\JDK\23') then
    begin
      MsgBox('Java 21 ou superior não foi detectado no seu sistema.' + #13 + #13 + 
             'Por favor, instale o Java 21 JDK antes de continuar.', mbError, MB_OK);
      Result := False;
    end;
  end;
end;