;----------------------------------------------------------------------------
; General configuration

; The name and version of the installer

 !define VERSION 1.0
 Name "Viajes Compartidos"
 OutFile InstalarViajesCompartidos.exe
 InstallDir "$PROGRAMFILES\Viajes Compartidos"

 ;;; Request application privileges for Windows Vista (can be user or admin)
 RequestExecutionLevel admin
;----------------------------------------------------------------------------


;--------------------------------
;Use Modern User Interface
 !include MUI2.nsh
;--------------------------------


;--------------------------------
; Interface Settings

 BrandingText "Viajes Compartidos"
 
 !define MUI_ICON vc.ico
 !define MUI_UNICON vc.ico
 
 !define MUI_HEADERIMAGE
 !define MUI_HEADERIMAGE_BITMAP header.bmp ; size 150x57
 !define MUI_HEADERIMAGE_UNBITMAP header.bmp ; size 150x57

 !define MUI_PAGE_HEADER_TEXT "Instalar Viajes Compartidos"
 !define MUI_PAGE_HEADER_SUBTEXT ""

 !define MUI_WELCOMEPAGE_TITLE "Bienvenido a la instalacion de viajes compartidos"
 !define MUI_WELCOMEPAGE_TEXT "Bienvenido a la instalacion de viajes compartidos. $\nEste asistente lo guiara a traves del proceso de instalacion. $\n$\nIMPORTANTE: El servidor de Viajes Compartidos utiliza la tecnologia Java.$\nAsegurese de tener instalado el entorno de ejecucion Java, version 8 o superior. $\n$\nPresione Siguiente para comenzar"
 
 !define MUI_COMPONENTSPAGE_TEXT_TOP "Para que el sistema funcione correctamente debe instalar todos los componentes. Si desactiva alguno de ellos debera configurarlo manualmente."
 !define MUI_COMPONENTSPAGE_TEXT_COMPLIST "Componentes a instalar"
 !define MUI_COMPONENTSPAGE_TEXT_DESCRIPTION_TITLE ""
 !define MUI_COMPONENTSPAGE_TEXT_DESCRIPTION_INFO "Se recomienda instalar todos los componentes"

 !define MUI_TEXT_DIRECTORY_TITLE "Directorio de instalacion"
 !define MUI_TEXT_DIRECTORY_SUBTITLE "Seleccione el directorio de instalacion"
 !define MUI_DIRECTORYPAGE_TEXT_TOP "Seleccione el directorio donde instalar Viajes Compartidos"
 !define MUI_DIRECTORYPAGE_TEXT_DESTINATION "Directorio de destino"
 
 !define MUI_TEXT_INSTALLING_TITLE "Instalando"
 !define MUI_TEXT_INSTALLING_SUBTITLE "Se estan instalando los componentes seleccionados"

 !define MUI_FINISHPAGE_TITLE "Se ha completado la instalacion de Viajes Compartidos"
 !define MUI_FINISHPAGE_TEXT "La instalacion de Viajes Compartidos se ha completado satisfactoriamente $\n"
 !define MUI_FINISHPAGE_BUTTON "Finalizar"
 
 !define MUI_TEXT_ABORT_TITLE "Cancelar"
 !define MUI_TEXT_ABORT_SUBTITLE "Cancelar la instalacion"

 !define MUI_BUTTONTEXT_FINISH "Finalizar"
 
 !define MUI_ABORTWARNING "Esta seguro que desea cancelar la instalacion de Viajes Compartidos?"

; Pages

 !define MUI_WELCOMEFINISHPAGE_BITMAP left.bmp ; size 164x314
 !define MUI_WELCOMEPAGE_TITLE_3LINES

 !define MUI_INSTFILESPAGE_COLORS "f1f1f1 000000"
 !define MUI_FINISHPAGE_NOAUTOCLOSE

 !define MUI_FINISHPAGE_RUN "$INSTDIR\viajescompartidos.bat"
 !define MUI_FINISHPAGE_RUN_TEXT "Iniciar el servidor"

 !insertmacro MUI_PAGE_WELCOME
 !insertmacro MUI_PAGE_COMPONENTS
 !insertmacro MUI_PAGE_DIRECTORY
 !insertmacro MUI_PAGE_INSTFILES
 !insertmacro MUI_PAGE_FINISH

 !insertmacro MUI_UNPAGE_CONFIRM
 !insertmacro MUI_UNPAGE_INSTFILES

 ShowInstDetails hide
 ShowUninstDetails show
;---------------------------------------------

;---------------------------------------------
; I don't know why but required to show images

 !insertmacro MUI_LANGUAGE Spanish
 !insertmacro MUI_LANGUAGE English
;---------------------------------------------

;--------------------------------
; The stuff to install

Section "Viajes Compartidos"
 SetShellVarContext all
 SetOutPath $INSTDIR
 File viajescompartidos.bat
 File vc.ico
 File JavaHome.java
 File JavaHome.class
 SetOutPath $INSTDIR\tomcat8\webapps\ROOT
 File /r ROOT\*
 WriteUninstaller $INSTDIR\desinstalar.exe
 SetOutPath $INSTDIR
 CreateShortCut "$DESKTOP\Iniciar Servidor Viajes Compartidos.lnk" $INSTDIR\viajescompartidos.bat  "$INSTDIR\vc.ico"
 CreateDirectory "$STARTMENU\Viajes Compartidos"
 CreateShortCut "$STARTMENU\Viajes Compartidos\Iniciar Servidor Viajes compartidos.lnk" $INSTDIR\viajescompartidos.bat "$INSTDIR\vc.ico"
 CreateShortCut "$STARTMENU\Viajes Compartidos\Desinstalar Viajes Compartidos.lnk" $INSTDIR\desinstalar.exe
 WriteRegStr HKLM Software\Microsoft\Windows\CurrentVersion\Uninstall\ViajesCompartidos \
 DisplayName "Viajes Compartidos"
 WriteRegStr HKLM Software\Microsoft\Windows\CurrentVersion\Uninstall\ViajesCompartidos \
 UninstallString $INSTDIR\desinstalar.exe
 WriteRegStr HKLM Software\Microsoft\Windows\CurrentVersion\Uninstall\ViajesCompartidos \
 DisplayIcon $INSTDIR\vc.ico
 WriteRegStr HKLM Software\Microsoft\Windows\CurrentVersion\Uninstall\ViajesCompartidos \
 Publisher "Seminario Integración Profesional, UNLu"
SectionEnd

Section "Servidor Web Tomcat 8"
 SetShellVarContext all
 SetOutPath $INSTDIR\tomcat8
 File /r apache-tomcat-8.0.35\*
SectionEnd

Section "Servidor de Base de Datos MySQL 5"
 SetShellVarContext all
 SetOutPath $INSTDIR\mysql5
 File /r mysql-5.7.13-win32\*
SectionEnd
;--------------------------------

;--------------------------------
; The stuff to Uninstall
Section Uninstall
 ;;; Foo App will be removed for all users
 SetShellVarContext all
 SetOutPath $INSTDIR
 RMDir /r $INSTDIR
 RMDir /r "$STARTMENU\Viajes Compartidos"
 Delete "$DESKTOP\Iniciar Servidor Viajes Compartidos.lnk"
 ;;; Remove Foo App info from the windows registry
 DeleteRegKey HKLM Software\Microsoft\Windows\CurrentVersion\Uninstall\ViajesCompartidos
SectionEnd
;-------------------------------- 
