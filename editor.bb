; Wonderland Adventures Editor
; Open Source Edition
; 
; v10.04
;
;
; COMPILER NOTE:	Requires	User32.decls with Hasfocus() function in Blitz3D/userlibs
;								msfolder.dll and .decls in Blitz3D/userlibs
;
;

Global WAEpisode=0	; 0-editor 
				   	; 1-Quest For the Rainbow Shards
					; 2-Mysteries of Fire Island
					; 3-Planet of the Z-Bots

; Versions
Const OpenSource=False
Const UseWOI=True

; Options
Global UseNewUI=True
Global TempScreen
Global UseNewFormat=True
Global AlwaysPackContent=False
Global DisplayFont$="Default"

Dim CompleteCustomHubOverideData$(0) ;required because of shared source with player
Dim LevelTileExtrusion(100, 100)
Global EditorStinkerTexture

; App title and includes
Global VersionDate$="07/16/23"
AppTitle "Wonderland Adventures MNIKEditor (Version "+VersionDate$+")"

; GLOBAL DEFINATIONS
Include "shared/defines.bb"
Include "editor/editor-cutscene-define.bb"
Include "shared/particles-define.bb"
Include "sound-define.bb"

; STARTUP
Include "editor/editor-startup.bb"

LoadSounds()

CalculateUIValues()
InitializeGraphicsCameras() ; needed for loading particles
InitializeGraphicsEntities()
InitializeGraphicsTextures()

; check if valid username is already selected
flag=True
file=ReadFile(GlobalDirName$+"\custom\Editing\Profiles\currentuser.dat")
If file=0
	flag=False
Else
	EditorUserName$=ReadString$(file)
	If FileType(GlobalDirName$+"\custom\Editing\Profiles\"+editorusername$)<>2
		flag=False
		CloseFile(file)
		DeleteFile GlobalDirName$+"\custom\Editing\Profiles\currentuser.dat"
	EndIf
EndIf

If flag=False
	StartUserSelectScreen()
Else
	StartAdventureSelectScreen()
EndIf

ResetLevel()
BuildLevelModel()

LoadTilePreset()
LoadObjectPreset()

Global Mouseimg=LoadImage ("data\Mouseimg.bmp")

StartupConfigs()

TweenPeriod=1000/60;85
TweenTime=MilliSecs()-TweenPeriod

ReadTestFile()

Repeat

	If HasFocus()

		Repeat
			TweenElapsed=MilliSecs()-TweenTime
		Until TweenElapsed>TweenPeriod

		If TweenElapsed>20*TweenPeriod
			TweenElapsed=20*TweenPeriod
			TweenTime=MilliSecs()-TweenElapsed
		EndIf

		;how many 'frames' have elapsed
		TweenTicks=TweenElapsed/TweenPeriod
		;fractional remainder
		Tween#=Float(TweenElapsed Mod TweenPeriod)/Float(TweenPeriod)

		For k=1 To TweenTicks

			Tweentime=Tweentime+Tweenperiod
			If k=Tweenticks
				CaptureWorld
			EndIf

			UpdateEditor()

		Next

	Else

		Repeat
			Delay 200
		Until HasFocus()

		OnRegainFocus()

	EndIf

Until False ;KeyDown(1) ; escape

EndApplication()

Function OnRegainFocus()

	;FlushKeys ; WHY DOES THIS NOT WORK??? Apparently it doesn't get rid of currently-pressed keys.

	;ForceKeyRelease(56,"left alt")
	;ForceKeyRelease(184,"right alt")

	ForceKeyRelease(42,"left shift")
	ForceKeyRelease(54,"right shift")

	ForceKeyRelease(29,"left ctrl")
	ForceKeyRelease(157,"right ctrl")

	ForceKeyRelease(57,"space")

	; Apparently Blitz doesn't pick up on the Tab key when alt-tabbing. That's pretty nice.

	ReadConfigs()

End Function

Function CalculateUIValues()

	GfxAspectRatio#=Float#(GfxWidth)/Float#(GfxHeight)
	GfxZoomScaling#=OriginalRatio#/GfxAspectRatio#

	ToolbarBrushModeX=ToolbarPositionX(1)
	ToolbarBrushModeY=ToolbarPositionY(1)

	ToolbarBrushSizeX=ToolbarPositionX(2)
	ToolbarBrushSizeY=ToolbarPositionY(1)

	ToolbarTexPrefixX=ToolbarPositionX(2)
	ToolbarTexPrefixY=ToolbarPositionY(2)

	ToolbarDensityX=ToolbarPositionX(3)
	ToolbarDensityY=ToolbarPositionY(1)

	ToolbarElevateX=ToolbarPositionX(3)
	ToolbarElevateY=ToolbarPositionY(2)

	ToolbarBrushWrapX=ToolbarPositionX(4)
	ToolbarBrushWrapY=ToolbarPositionY(1)

	ToolbarStepPerX=ToolbarPositionX(4)
	ToolbarStepPerY=ToolbarPositionY(2)

	ToolbarShowMarkersX=ToolbarPositionX(5)
	ToolbarShowMarkersY=ToolbarPositionY(1)

	ToolbarShowObjectsX=ToolbarPositionX(5)
	ToolbarShowObjectsY=ToolbarPositionY(2)

	ToolbarShowLogicX=ToolbarPositionX(6)
	ToolbarShowLogicY=ToolbarPositionY(1)

	ToolbarShowLevelX=ToolbarPositionX(6)
	ToolbarShowLevelY=ToolbarPositionY(2)

	ToolbarIDFilterX=ToolbarPositionX(7)
	ToolbarIDFilterY=ToolbarPositionY(1)

	ToolbarSimulationLevelX=ToolbarPositionX(7)
	ToolbarSimulationLevelY=ToolbarPositionY(2)

	ToolbarExitX=ToolbarPositionX(8)
	ToolbarExitY=ToolbarPositionY(1)

	ToolbarSaveX=ToolbarPositionX(8)
	ToolbarSaveY=ToolbarPositionY(2)

	LetterWidth#=Float#(GfxWidth)/Float#(LettersCountX)*GfxZoomScaling#
	LetterHeight#=Float#(GfxHeight)/Float#(LettersCountY)

	LevelViewportWidth=GfxWidth-SidebarWidth
	LevelViewportHeight=GfxHeight-ToolbarHeight
	TilePickerZoomScaling#=Float#(LevelViewportHeight)/Float#(LevelViewportWidth) ; The numerator is 1 because the original 500x500 viewport is a 1:1 ratio.

	SidebarX=LevelViewportWidth
	SidebarY=0

	FlStartX=SidebarX+206 ; 706
	FlStartY=SidebarY+165

	LowerButtonsCutoff=LetterHeight*26

End Function

Function InitializeGraphicsTextures()

	ParticleTexture=myLoadTexture("data\graphics\particles.bmp",1)
	ResetParticles("data/graphics/particles.bmp")
	EntityTexture ParticlePreview,ParticleTexture

	TextTexture=myLoadTexture("Data/Graphics/font.bmp",4)
	ResetText("data/graphics/font.bmp")

	UpdateButtonGateTexture()
	LoadLevelTextureDefault()
	LoadWaterTextureDefault()

End Function

Function ResetGraphicsTextures()

	; Setting these handles to 0 since the pointees will be lost by a call to Graphics3D.
	TextTexture=0
	ParticleTexture=0
	TextMesh=0
	ParticleMesh=0
	ParticleMesh2=0

	InitializeGraphicsTextures()

End Function

Function InitializeGraphicsCameras()

	CameraPanning=False
	GameCamera=False

	Camera1 = CreateCamera() ; level camera
	Camera1PerspectiveZoom#=1.0*GfxZoomScaling#
	Camera1OrthographicZoom#=0.015*GfxZoomScaling#
	Camera1Zoom#=Camera1PerspectiveZoom#
	Camera4Zoom#=8.0

	TurnEntity Camera1,65,0,0
	PositionEntity Camera1,7,Camera1StartY,-14
	CameraViewport camera1,0,0,LevelViewportWidth,LevelViewportHeight
	CameraRange camera1,.1,1000 ;50

	Camera2 = CreateCamera() ; tile camera
	CameraClsColor camera2,255,0,0
	CameraViewport Camera2,SidebarX+10,SidebarY+20,200,220
	CameraRange camera2,.1,1000
	RotateEntity Camera2,45,25,0
	PositionEntity Camera2,4.9,109,-8
	CameraZoom Camera2,5

	Camera3 = CreateCamera() ; texture picker camera
	CameraClsColor camera3,0,0,0 ;255,0,0
	CameraViewport Camera3,0,0,LevelViewportWidth,LevelViewportHeight
	CameraRange camera3,.1,50 ;1000
	RotateEntity Camera3,90,0,0
	PositionEntity Camera3,0.5,210,-0.5
	CameraZoom Camera3,20.0*TilePickerZoomScaling#

	Camera4 = CreateCamera() ; objects menu camera
	CameraClsColor camera4,155,0,0
	CameraViewport Camera4,SidebarX+195,SidebarY+305,100,125
	CameraRange camera4,.1,1000
	RotateEntity Camera4,25,0,0
	PositionEntity Camera4,0,303.8,-8

	Camera = CreateCamera() ; Text Screen Camera
	CameraZoom Camera,GfxZoomScaling#

	ParticleViewportSize=150
	CameraParticle = CreateCamera() ; particle camera
	CameraClsColor CameraParticle,0,0,0
	CameraViewport CameraParticle,SidebarX-ParticleViewportSize,SidebarY+310,ParticleViewportSize,ParticleViewportSize
	CameraRange CameraParticle,.1,50
	RotateEntity CameraParticle,90,0,0
	PositionEntity CameraParticle,0.5,410,-0.5
	CameraZoom CameraParticle,20.0*TilePickerZoomScaling#
	EntityOrder CameraParticle,-1

	UpdateCameraProj()
	UpdateCameraClsColor()

End Function

Function CreateBrushMesh()

	BrushMesh=CreateMesh()
	BrushSurface=CreateSurface(BrushMesh)
	EntityAlpha BrushMesh,BrushMeshAlpha

	BrushTextureMesh=CreateMesh()
	BrushTextureSurface=CreateSurface(BrushTextureMesh)
	EntityAlpha BrushTextureMesh,0.6 ;BrushMeshAlpha

	; This translation is needed to prevent z-fighting and to give Blitz3D a hint about the sorting order between the two transparent entities.
	TranslateEntity BrushTextureMesh,0,-0.005,0

End Function

Function GenerateCurrentGrabbedObjectMarkerEntity()

	CurrentGrabbedObjectMarkers(0)=CreateMesh()

	; Lots and lots of duplicated code here. Haha, whoops!
	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,-0.5,0,-0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole

	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,0.5,0,-0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole

	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,-0.5,0,0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole

	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,0.5,0,0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole

	RotateMesh CurrentGrabbedObjectMarkers(0),90,0,0

	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,-0.5,0,-0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole

	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,0.5,0,-0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole

	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,-0.5,0,0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole

	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,0.5,0,0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole

	RotateMesh CurrentGrabbedObjectMarkers(0),0,0,90

	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,-0.5,0,-0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole

	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,0.5,0,-0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole

	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,-0.5,0,0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole

	Pole=CreateCube()
	ScaleMesh Pole,0.025,0.5,0.025
	PositionMesh Pole,0.5,0,0.5
	AddMesh Pole,CurrentGrabbedObjectMarkers(0)
	FreeEntity Pole

	PositionMesh CurrentGrabbedObjectMarkers(0),0,0.5,0
	ScaleMesh CurrentGrabbedObjectMarkers(0),0.95,0.95,0.95

	EntityColor CurrentGrabbedObjectMarkers(0),100,255,100
	EntityFX CurrentGrabbedObjectMarkers(0),1 ; fullbright
	EntityOrder CurrentGrabbedObjectMarkers(0),-1 ; disable depth sorting
	HideEntity(CurrentGrabbedObjectMarkers(0))

	EntityAlpha CurrentGrabbedObjectMarkers(0),0.5

	For i=1 To MaxNofObjects-1
		CurrentGrabbedObjectMarkers(i)=CopyEntity(CurrentGrabbedObjectMarkers(0))
	Next

End Function

Function InitializeGraphicsEntities()

	Light=CreateLight()
	RotateEntity Light,80,15,0

	SpotLight=CreateLight(3)
	RotateEntity SpotLight,60,0,0
	LightConeAngles SpotLight,0,60

	InitializeLevelModel()

;	For i=0 To MaxLevelCoordinate
;		LevelMesh(i)=CreateMesh()
;		WaterMesh(i)=CreateMesh()
;		LogicMesh(i)=CreateMesh()
;	Next

	TexturePlane=CreateMesh()
	TexturePlaneSurface=CreateSurface(TexturePlane)
	AddVertex TexturePlaneSurface,0,200,0,0,0
	AddVertex TexturePlaneSurface,1,200,0,1,0
	AddVertex TexturePlaneSurface,0,200,-1,0,1
	AddVertex TexturePlaneSurface,1,200,-1,1,1
	AddTriangle TexturePlaneSurface,0,1,2
	AddTriangle TexturePlaneSurface,1,3,2
	UpdateNormals TexturePlane
	EntityPickMode TexturePlane,2

	For i=0 To 3
		; Pillar
		CursorMeshPillar(i)=CreateCube()
		ScaleMesh CursorMeshPillar(i),0.1,10,0.1
		EntityAlpha CursorMeshPillar(i),BrushMeshAlpha
		EntityColor CursorMeshPillar(i),255,255,200

		; Little square at the center of the brush
		CursorMeshOpaque(i)=CreateCube()
		ScaleMesh CursorMeshOpaque(i),.2,0.01,.2
	Next

	; The square region that the brush covers, used only by the texture picker
	CursorMeshTexturePicker=CreateCube()
	ScaleMesh CursorMeshTexturePicker,.5,0.1,.5
	EntityAlpha CursorMeshTexturePicker,BrushMeshAlpha
	EntityColor CursorMeshTexturePicker,255,255,200
	HideEntity CursorMeshTexturePicker

	CreateBrushMesh()

	CurrentObjectMarkerMesh=CreateCylinder()
	ScaleEntity CurrentObjectMarkerMesh,.01,3,.01
	PositionEntity CurrentObjectMarkerMesh,0,300,0

	LightPillar=CreateCube()
	ScaleMesh LightPillar,0.5,90,0.5
	EntityColor LightPillar,100,255,100
	EntityFX LightPillar,16 ; disable back-face culling
	HideEntity LightPillar

	GenerateCurrentGrabbedObjectMarkerEntity()

	WorldAdjusterPositionMarker(0)=CopyEntity(LightPillar)
	EntityColor WorldAdjusterPositionMarker(0),100,200,255
	For i=1 To 3
		WorldAdjusterPositionMarker(i)=CopyEntity(WorldAdjusterPositionMarker(0))
	Next

	CurrentObjectMoveXYGoalMarker=CopyEntity(LightPillar)
	EntityColor CurrentObjectMoveXYGoalMarker,255,100,100

	WhereWeEndedUpMarker=CopyEntity(LightPillar)
	EntityColor WhereWeEndedUpMarker,255,255,0
	ShowEntity WhereWeEndedUpMarker

	MirrorEntityX=CreateCube()
	ScaleMesh MirrorEntityX,0.1,0.3,200.0
	EntityColor MirrorEntityX,GetBrushModeColor(BrushModeSetMirror,0),GetBrushModeColor(BrushModeSetMirror,1),GetBrushModeColor(BrushModeSetMirror,2)
	EntityAlpha MirrorEntityX,0.3
	HideEntity MirrorEntityX

	MirrorEntityY=CreateCube()
	ScaleMesh MirrorEntityY,200.0,0.3,0.1
	EntityColor MirrorEntityY,GetBrushModeColor(BrushModeSetMirror,0),GetBrushModeColor(BrushModeSetMirror,1),GetBrushModeColor(BrushModeSetMirror,2)
	EntityAlpha MirrorEntityY,0.3
	HideEntity MirrorEntityY

	BlockModeMesh=CreateMesh()
	BlockModeSurface=CreateSurface(BlockModeMesh)
	AddVertex (BlockModeSurface,0,0,0)
	AddVertex (BlockModeSurface,0,0,0)
	AddVertex (BlockModeSurface,0,0,0)
	AddVertex (BlockModeSurface,0,0,0)
	AddTriangle (BlockModeSurface,0,1,2)
	AddTriangle (BlockModeSurface,1,3,2)
	EntityAlpha BlockModeMesh,0.5
	EntityOrder BlockModeMesh,-1

	CurrentWaterTile=CreateMesh()
	CurrentWaterTileSurface=CreateSurface(CurrentWaterTile)

	AddVertex (CurrentWaterTileSurface,1,99.5,3,CurrentTile\Water\Texture/4.0,0)
	AddVertex (CurrentWaterTileSurface,3,99.5,3,CurrentTile\Water\Texture/4.0+.25,0)
	AddVertex (CurrentWaterTileSurface,1,99.5,1,CurrentTile\Water\Texture/4.0,.25)
	AddVertex (CurrentWaterTileSurface,3,99.5,1,CurrentTile\Water\Texture/4.0+.25,.25)
	AddTriangle (CurrentWaterTileSurface,0,1,2)
	AddTriangle (CurrentWaterTileSurface,2,1,3)
	UpdateNormals CurrentWaterTile

	FreeEntity LightPillar

	ParticlePreview=CreateMesh()
	ParticlePreviewSurface=CreateSurface(ParticlePreview)
	AddVertex ParticlePreviewSurface,0,400,0
	AddVertex ParticlePreviewSurface,1,400,0
	AddVertex ParticlePreviewSurface,0,400,-1
	AddVertex ParticlePreviewSurface,1,400,-1

	AddTriangle ParticlePreviewSurface,0,1,2
	AddTriangle ParticlePreviewSurface,1,3,2
	UpdateNormals ParticlePreview

	;EntityOrder ParticlePreview,-10
	;EntityFX ParticlePreview,1

End Function

Function ResetGraphicsEntities()

	InitializeGraphicsCameras()
	InitializeGraphicsEntities()
	ResetGraphicsTextures()

	; reload object entities
	For i=0 To NofObjects-1
		BuildLevelObjectModel(i)
	Next

End Function

Function ResetWindowSize()

	;EndGraphics
	Graphics3D 800,600,16,2
	SetBuffer BackBuffer()
	Graphics3D 800,600,16,3

	ResetGraphicsEntities()

End Function

Function ResolutionWasChanged()

	Return ; Exit for now until InitializeGraphicsEntities is complete, assuming that ever happens.

	CalculateUIValues()
	ResetGraphicsEntities()

End Function

Function ShowParticlePreviewLeftAligned(x#,y#,tex)

	nudge#=.001 ; push this much inward from texture border to avoid grabbing pieces of neighbour

	u1#=Float(tex Mod 8)*0.125+nudge
	u2#=u1+0.125-2*nudge
	v1#=Float(Floor(tex/8))*0.125+nudge
	v2#=v1+0.125-2*nudge

	VertexTexCoords(ParticlePreviewSurface,0,u1#,v1#)
	VertexTexCoords(ParticlePreviewSurface,1,u2#,v1#)
	VertexTexCoords(ParticlePreviewSurface,2,u1#,v2#)
	VertexTexCoords(ParticlePreviewSurface,3,u2#,v2#)

	CameraParticleProj=1
	CameraViewport CameraParticle,x#,y#-CameraParticlePreviewSize,CameraParticlePreviewSize,CameraParticlePreviewSize
	UpdateCameraProj()

End Function

Function ShowParticlePreviewRightAligned(x#,y#,tex)

	ShowParticlePreviewLeftAligned(x#-CameraParticlePreviewSize,y#,tex)

End Function

Function ShowParticlePreviewCenterAligned(x#,y#,tex)

	ShowParticlePreviewLeftAligned(x#-CameraParticlePreviewSize/2,y#,tex)

End Function

Function UpdateCameraProj()

	CameraProjMode Camera1,Camera1Proj
	CameraProjMode Camera2,Camera2Proj
	CameraProjMode Camera3,Camera3Proj
	CameraProjMode Camera4,Camera4Proj
	CameraProjMode Camera,CameraProj
	CameraProjMode CameraParticle,CameraParticleProj

	CameraZoom Camera1,Camera1Zoom#
	CameraZoom Camera4,Camera4Zoom#

End Function

Function UpdateCameraClsColor()

	CameraClsColor camera2,TileColorR,TileColorG,TileColorB
	CameraClsColor camera4,ObjectColorR,ObjectColorG,ObjectColorB

End Function

Function Camera1To3Proj()

	;Camera1Proj=0
	Camera3Proj=1
	UpdateCameraProj()

End Function

; Before this function was invented, the level editor camera was originally positioned to approximately be focused on the coordinates 7,10.
Function PositionCameraInLevel(FocusOnTileX,FocusOnTileY)

	PositionEntity Camera1,FocusOnTileX+0.5,EntityY(Camera1),-FocusOnTileY+0.5-4

End Function

Function UpdateButtonGateTexture()

	ButtonTexture=MyLoadTexture("data\graphics\buttons"+Str$(GateKeyVersion)+".bmp",4)
	GateTexture=MyLoadTexture("data\graphics\gates"+Str$(GateKeyVersion)+".bmp",1)

End Function

Function CurrentLevelTextureName$()

	If CurrentLevelTexture=-1
		Return LevelTextureCustomName$
	Else
		Return LevelTextureName$(CurrentLevelTexture)
	EndIf

End Function

Function CurrentWaterTextureName$()

	If CurrentWaterTexture=-1
		Return WaterTextureCustomName$
	Else
		Return WaterTextureName$(CurrentWaterTexture)
	EndIf

End Function

Function LoadLevelTextureDefault()

	LevelTexture=myLoadTexture("data\Leveltextures\"+CurrentLevelTextureName$(),1)

End Function

Function LoadWaterTextureDefault()

	WaterTexture=MyLoadTexture("data\Leveltextures\"+CurrentWaterTextureName$(),1)

End Function

Function UpdateLevelTexture()

	EntityTexture TexturePlane,LevelTexture
	For j=0 To LevelHeight-1
		EntityTexture LevelMesh(j),LevelTexture
	Next

	EntityTexture BrushTextureMesh,LevelTexture

	;ShowMessage("LevelTexture update!",1000)

End Function

Function UpdateLevelTextureDefault()

	LoadLevelTextureDefault()
	UpdateLevelTexture()

End Function

Function UpdateWaterTexture()

	EntityTexture Currentwatertile,WaterTexture
	For j=0 To LevelHeight-1
		EntityTexture WaterMesh(j),WaterTexture
	Next

End Function

Function UpdateWaterTextureDefault()

	LoadWaterTextureDefault()
	UpdateWaterTexture()

End Function

Function FinishDrawing()

	;Color 255,255,255
	;Text 0,0,"Mouse: "+MouseX()+", "+MouseY()

	DrawTooltip(CurrentTooltipStartX,CurrentTooltipStartY,CurrentTooltip$)
	CurrentTooltip$=""

	If CameraParticleProj=1
		CameraParticleProj=0
		UpdateCameraProj()
	EndIf

	If displayfullscreen=True
		DrawImage mouseimg,MouseX(),MouseY()
	EndIf

	Flip

End Function

Function EndApplication()

	Color 0,0,0
	Rect 0,0,GfxWidth,GfxHeight

	Flip

	End

End Function

Function UpdateEditor()

	EditorGlobalControls()

	Select EditorMode

	Case 0,1,2,3
		EditorMainLoop()
	Case 4
		UserSelectScreen()
	Case 5,12
		AdventureSelectScreen()
	Case 6
		AdventureSelectScreen2()
	Case 7
		AdventureSelectScreen3()
	Case 8
		MasterMainLoop()
	Case 9
		DialogMainLoop()
	Case 10
		MasterAdvancedLoop()
	Case 11
		HubMainLoop()
	Case 13
		SettingsMainLoop()
	End Select

End Function

Function StartEditorMainLoop()
	Cls
	SetEditorMode(EditorModeBeforeMasterEdit)
	WireFrame UsingWireFrame

	Camera1Proj=Camera1SavedProjMode
	Camera2Proj=1
	Camera3Proj=0
	Camera4Proj=1
	CameraProj=0
	UpdateCameraProj()

	ClearSurface Textsurface
	For p.letter = Each letter
		Delete p
	Next

End Function

Function EditorMainLoop()

	If displayfullscreen=True Cls

	If EditorMode=0 Or EditorMode=3
		CameraControls()
	EndIf

	RenderToolbar()
	If CameraPanning=False
		EditorLocalControls()
	EndIf
	EditorLocalRendering()

	leveltimer=leveltimer+1

	If KeyPressed(35) ; h key
		HighlightWopAdjusters=Not HighlightWopAdjusters
	EndIf

	MarkerAlpha#=0.3+0.03*Sin((Float(LevelTimer)*6.0) Mod 360)
	;For i=0 To MaxNofObjects-1
	;	EntityAlpha CurrentGrabbedObjectMarkers(i),MarkerAlpha#
	;Next
	For i=0 To 3
		EntityAlpha WorldAdjusterPositionMarker(i),MarkerAlpha#
	Next
	EntityAlpha CurrentObjectMoveXYGoalMarker,MarkerAlpha#

	WhereWeEndedUpAlpha#=WhereWeEndedUpAlpha#-0.002
	EntityAlpha WhereWeEndedUpMarker,WhereWeEndedUpAlpha#

	ControlLight()
	If SimulationLevel>=1
		ControlObjects()
	EndIf
	If SimulationLevel>=3
		ControlWeather()
	EndIf

	If SimulationLevel>=SimulationLevelMusic And EditorMode<>8
		ControlSoundscapes()
		LoopMusic()
		PlayAllSounds()
	EndIf
	ResetSounds()

	ControlParticles()
	RenderParticles()

	;ControlLetters()
	;RenderLetters()

	UpdateWorld
	RenderWorld

	Color TextLevelR,TextLevelG,TextLevelB

	Text 0,5,"ADVENTURE: "+AdventureFileName$
	If CurrentLevelNumber<10 And CurrentLevelNumber>=0
		Line1$="LEVEL: 0"+CurrentLevelNumber
	Else
		Line1$="LEVEL: "+CurrentLevelNumber
	EndIf
	RightAlignedText(LevelViewportWidth,5,Line1$)

	If EditorMode=0 Or EditorMode=3

		; it's a bit less than the viewport size because the text would otherwise overlap with the x/y coordinate listing on the bottom bar as well as the right margin
		ProjectedTextLimitX=LevelViewportWidth-10
		ProjectedTextLimitY=LevelViewportHeight-10

		For i=0 To NofObjects-1
;			If LevelObjects(i)\Attributes\LogicType=90 And LevelObjects(i)\Attributes\LogicSubType=15 ; General Command
;				Command$=LevelObjects(i)\Attributes\Data0
;				Pos.GameObjectPosition=LevelObjects(i)\Position
;				DisplayTextFacingUp(Command$,Pos\X,Pos\Y,Pos\Z+0.05,255,255,0)
;			EndIf

			MyEffectiveId=CalculateEffectiveId(LevelObjects(i)\Attributes)

			HitTargetID=False
			For j=0 To CurrentObjectTargetIDCount-1
				If CurrentObjectTargetIDEnabled(j) And MyEffectiveId=CurrentObjectTargetID(j)
					HitTargetID=True

					CameraProject(Camera1,LevelObjects(i)\Position\X,0.5,-LevelObjects(i)\Position\Y)
					x#=ProjectedX#()
					y#=ProjectedY#()
					If x#<ProjectedTextLimitX And y#<ProjectedTextLimitY
						StringOnObject$=MyEffectiveId
						x#=x#-4*Len(StringOnObject$)

						OutlinedText(x#,y#,StringOnObject$,255,255,0)
					EndIf
				EndIf
			Next

			If Not HitTargetID
				For j=0 To CurrentObjectActivateIDCount-1
					If CurrentObjectActivateIDEnabled(j) And MyEffectiveId=CurrentObjectActivateID(j) And CurrentObjectActivateID(j)>0
						HitTargetID=True

						CameraProject(Camera1,LevelObjects(i)\Position\X,0.5,-LevelObjects(i)\Position\Y)
						x#=ProjectedX#()
						y#=ProjectedY#()
						If x#<ProjectedTextLimitX And y#<ProjectedTextLimitY
							StringOnObject$=MyEffectiveId
							x#=x#-4*Len(StringOnObject$)

							OutlinedText(x#,y#,StringOnObject$,100,255,255)
						EndIf
					EndIf
				Next
			EndIf

			If (Not HitTargetID)
				If (i=CurrentObject\Attributes\Linked And ObjectAdjusterLinked\Absolute) Or (i=CurrentObject\Attributes\LinkBack And ObjectAdjusterLinkBack\Absolute)
					HitTargetID=True

					CameraProject(Camera1,LevelObjects(i)\Position\X,0.5,-LevelObjects(i)\Position\Y)
					x#=ProjectedX#()
					y#=ProjectedY#()

					StringOnObject$="#"+i
					HalfLength=4*Len(StringOnObject$)

					If x#<HalfLength
						x#=HalfLength
					EndIf
					If x#>LevelViewportWidth-HalfLength
						x#=LevelViewportWidth-HalfLength
					EndIf
					If y#<0
						y#=0
					EndIf
					If y#>ProjectedTextLimitY
						y#=ProjectedTextLimitY
					EndIf

					x#=x#-HalfLength

					OutlinedText(x#,y#,StringOnObject$,255,0,0)
				EndIf
			EndIf

			If (Not HitTargetID) And ShowObjectMesh>=2
				CameraProject(Camera1,LevelObjects(i)\Position\X,0.5,-LevelObjects(i)\Position\Y)
				x#=ProjectedX#()
				y#=ProjectedY#()
				If x#<ProjectedTextLimitX And y#<ProjectedTextLimitY
					If ShowObjectMesh=ShowObjectMeshIndices
						; display object indices
						StringOnObject$="#"+i
					ElseIf ShowObjectMesh=ShowObjectMeshIds
						; display object IDs
						StringOnObject$=CalculateEffectiveId(LevelObjects(i)\Attributes)
					ElseIf ShowObjectMesh=ShowObjectMeshCount
						; display object counts
						StringOnObject$="x"+LevelTileObjectCount(LevelObjects(i)\Position\TileX,LevelObjects(i)\Position\TileY)
					EndIf
					OutlinedText(x#-4*Len(StringOnObject$),y#,StringOnObject$,255,255,255)
				EndIf
			EndIf
		Next

	EndIf

	Color TextLevelR,TextLevelG,TextLevelB

	StartX=SidebarX+10
	StartY=20
	If CurrentTileTextureUse=False Text StartX+10,StartY+70,"Not Used"
	If CurrentTileSideTextureUse=False Text StartX+10,StartY+130,"Not Used"
	If CurrentWaterTileTextureUse=False Text StartX+120,StartY+100,"Not Used"

	Text StartX,StartY,"Xtrude: "+CurrentTile\Terrain\Extrusion
	If CurrentTileExtrusionUse=False Text StartX,StartY,"------------"
	If StepSizeTileExtrusion#<>0.0 DrawStepSize(StartX,StartY+60,StepSizeTileExtrusion#)
	Color TextLevelR,TextLevelG,TextLevelB

	Text StartX+48,StartY,"       Height: "+CurrentTile\Terrain\Height
	If CurrentTileHeightUse=False Text StartX+48,StartY,"       ------------"
	If StepSizeTileHeight#<>0.0 DrawStepSize(StartX+160,StartY+60,StepSizeTileHeight#)
	Color TextLevelR,TextLevelG,TextLevelB

	CurrentLogicName$=LogicIdToLogicName$(CurrentTile\Terrain\Logic)
	Text StartX+50,StartY+15,"Logic: "+CurrentLogicName$
	If CurrentTileLogicUse=False Text StartX+50,StartY+15,"  ---------"

	Text StartX+50,StartY+170," Random: "+CurrentTile\Terrain\Random
	If CurrentTileRandomUse=False Text StartX+50,StartY+170,"--------------"
	If StepSizeTileRandom#<>0.0 DrawStepSize(StartX+80,StartY+180,StepSizeTileRandom#)
	Color TextLevelR,TextLevelG,TextLevelB

	If CurrentTile\Terrain\Rounding=0
		Text StartX,StartY+185,"Corner:Squar"
	Else
		Text StartX,StartY+185,"Corner:Round"
	EndIf
	If CurrentTileRoundingUse=False Text StartX,StartY+185,"------------"

	If CurrentTile\Terrain\EdgeRandom=0
		Text StartX+100,StartY+185," Edge:Smooth"
	Else
		Text StartX+100,StartY+185," Edge:Jagged"
	EndIf
	If CurrentTileEdgeRandomUse=False Text StartX+100,StartY+185,"------------"

	Text StartX,StartY+200,"WHeight:"+CurrentTile\Water\Height
	If CurrentWaterTileHeightUse=False Text StartX,StartY+200,"------------"
	If StepSizeWaterTileHeight#<>0.0 DrawStepSize(StartX,StartY+190,StepSizeWaterTileHeight#)
	Color TextLevelR,TextLevelG,TextLevelB

	Text StartX+100,StartY+200," WTurb:"+CurrentTile\Water\Turbulence
	If CurrentWaterTileTurbulenceUse=False Text StartX+100,StartY+200,"------------"
	If StepSizeWaterTileTurbulence#<>0.0 DrawStepSize(StartX+170,StartY+180,StepSizeWaterTileTurbulence#)

	;Color RectToolbarR,RectToolbarG,RectToolbarB
	;Rect 0,520,800,80,True
	;Rect 0,500,800,100,True

	Color GetBrushModeColor(BrushMode,0),GetBrushModeColor(BrushMode,1),GetBrushModeColor(BrushMode,2)

	CenteredText(ToolbarBrushModeX,ToolbarBrushModeY,GetBrushModeName$(BrushMode))

	Color TextLevelR,TextLevelG,TextLevelB

	CenteredText(ToolbarBrushModeX,GfxHeight-50,"WIPE/FLIP")

	If DupeMode<>DupeModeNone
		Color 255,155,0
	EndIf

	CenteredText(ToolbarBrushModeX,GfxHeight-20,GetDupeModeName$(DupeMode))

	Color TextLevelR,TextLevelG,TextLevelB

	CenteredText(ToolbarBrushSizeX,ToolbarBrushSizeY,"BRUSH SIZE")
	CenteredText(ToolbarBrushSizeX,ToolbarBrushSizeY+15,BrushWidth+" x "+BrushHeight)

	If CurrentTexturePrefix=-1
		TexturePrefixTitleName$="0"
		TexturePrefixName$="[ALWAYS NONE]"
	Else
		TexturePrefixTitleName$=CurrentTexturePrefix+1

		TexturePrefixName$=TexturePrefix$(CurrentTexturePrefix)
		If TexturePrefixName$=""
			TexturePrefixName$="[CTRL+CLICK]"
		Else
			Color 255,155,0
		EndIf
	EndIf
	CenteredText(ToolbarTexPrefixX,ToolbarTexPrefixY,"TEX PREFIX "+TexturePrefixTitleName$)
	CenteredText(ToolbarTexPrefixX,ToolbarTexPrefixY+15,TexturePrefixName$)
	;Text 90,565,"  TEX PREFIX"
	;Text 100,580,TexturePrefix$
	Color TextLevelR,TextLevelG,TextLevelB

	If ShowObjectPositions=True
		Line1$="SHOW"
	Else
		Line1$="HIDE"
	EndIf
	CenteredText(ToolbarShowMarkersX,ToolbarShowMarkersY,Line1$)
	CenteredText(ToolbarShowMarkersX,ToolbarShowMarkersY+15,"MARKERS")

	If ShowObjectMesh=0
		Color 255,155,0
		Line1$="HIDE"
	Else
		Line1$="SHOW"
	EndIf
	If ShowObjectMesh=ShowObjectMeshIndices
		Line2$="INDICES"
	ElseIf ShowObjectMesh=ShowObjectMeshIds
		Line2$="IDS"
	ElseIf ShowObjectMesh=ShowObjectMeshCount
		Line2$="COUNTS"
	Else
		Line2$="OBJECTS"
	EndIf
	CenteredText(ToolbarShowObjectsX,ToolbarShowObjectsY,Line1$)
	CenteredText(ToolbarShowObjectsX,ToolbarShowObjectsY+15,Line2$)

	Color TextLevelR,TextLevelG,TextLevelB

	If ShowLogicMesh=True
		Line1$="SHOW"
	Else
		Line1$="HIDE"
	EndIf
	CenteredText(ToolbarShowLogicX,ToolbarShowLogicY,Line1$)
	CenteredText(ToolbarShowLogicX,ToolbarShowLogicY+15,"LOGIC")

	If ShowLevelMesh=ShowLevelMeshShow
		Line1$="SHOW"
	ElseIf ShowLevelMesh=ShowLevelMeshHide
		Line1$="HIDE"
	ElseIf ShowLevelMesh=ShowLevelMeshTransparent
		Line1$="TRANSPARENT"
	EndIf
	CenteredText(ToolbarShowLevelX,ToolbarShowLevelY,Line1$)
	CenteredText(ToolbarShowLevelX,ToolbarShowLevelY+15,"LEVEL")

	CenteredText(ToolbarSimulationLevelX,ToolbarSimulationLevelY,"SIMULATION")
	CenteredText(ToolbarSimulationLevelX,ToolbarSimulationLevelY+15,"LEVEL "+SimulationLevel)

	CenteredText(ToolbarElevateX,ToolbarElevateY,"ELEVATE")

	Select BrushWrap
	Case BrushWrapRelative
		Line1$="RELATIVE"
	Case BrushWrapModulus
		Line1$="MODULUS"
	Case BrushWrapRandom
		Line1$="RANDOM"
	Case BrushWrapMirrorX
		Line1$="MIRROR X"
	Case BrushWrapMirrorY
		Line1$="MIRROR Y"
	Case BrushWrapMirrorXY
		Line1$="MIRROR XY"
	End Select
	CenteredText(ToolbarBrushWrapX,ToolbarBrushWrapY,"BRUSH WRAP")
	CenteredText(ToolbarBrushWrapX,ToolbarBrushWrapY+15,Line1$)

	If StepPer=StepPerPlacement
		Line1$="PLACEMENT"
	ElseIf StepPer=StepPerTile
		Line1$="TILE"
	ElseIf StepPer=StepPerClick
		Line1$="CLICK"
	EndIf
	CenteredText(ToolbarStepPerX,ToolbarStepPerY,"STEP PER")
	CenteredText(ToolbarStepPerX,ToolbarStepPerY+15,Line1$)

	If IDFilterEnabled
		Color 255,155,0
	EndIf
	CenteredText(ToolbarIDFilterX,ToolbarIDFilterY,"ID FILTER")
	If IDFilterEnabled
		Line1$="= "+IDFilterAllow
		If IDFilterInverted
			Line1$="!"+Line1$
		EndIf
	Else
		Line1$="OFF"
	EndIf
	CenteredText(ToolbarIDFilterX,ToolbarIDFilterY+15,Line1$)
	Color TextLevelR,TextLevelG,TextLevelB

	;Text 600,565,"  XTRUDE"
	;Text 600,580,"  LOGICS"

	If PlacementDensity#<1.0
		Color 255,155,0
	EndIf
	CenteredText(ToolbarDensityX,ToolbarDensityY,"DENSITY")
	CenteredText(ToolbarDensityX,ToolbarDensityY+15,PlacementDensity#)

	Line1$="   EXIT   "
	If IsMouseOverToolbarItem(ToolbarExitX,ToolbarExitY) ;MouseX()>700 And MouseY()>515 And MouseY()<555
		Color 255,255,0
		Line1$=">"+Line1$+"<"
	Else
		Color TextLevelR,TextLevelG,TextLevelB
	EndIf
	CenteredText(ToolbarExitX,ToolbarExitY,Line1$)

	UsingCarrots=False
	If UnsavedChanges<>0
		Line1$="SAVE LEVEL"
		Line2$=""
	Else
		If BrushMode=BrushModeTestLevel
			UsingCarrots=True
			Line1$="CLICK TILE"
			Line2$="TO TEST"
		Else
			Line1$="TEST LEVEL"
			Line2$="AT CURSOR"
		EndIf
	EndIf

	ShakeX=0
	ShakeY=0
	If IsMouseOverToolbarItem(ToolbarSaveX,ToolbarSaveY)
		Color 255,255,0
		UsingCarrots=True
	Else
		If UnsavedChanges<20
			Color TextLevelR,TextLevelG,TextLevelB
		ElseIf UnsavedChanges<40
			Color 200,200,0
		ElseIf UnsavedChanges<100
			Color 200,200,0
			ShakeX=Rand(-1,1)
			ShakeY=Rand(-1,1)
			;TheTimer=LevelTimer/8
			;Color GetAnimatedFlashing(TheTimer),GetAnimatedFlashing(TheTimer),0
		Else
			Color GetAnimatedFlashing(LevelTimer),60,60
			ShakeX=Rand(-3,3)
			ShakeY=Rand(-3,3)
		EndIf
	EndIf
	CenteredText(ToolbarSaveX+ShakeX,ToolbarSaveY+ShakeY,Line1$)
	CenteredText(ToolbarSaveX+ShakeX,ToolbarSaveY+ShakeY+15,Line2$)
	If UsingCarrots
		Line1$=">          <"
		CenteredText(ToolbarSaveX,ToolbarSaveY,Line1$)
		If Line2$<>""
			CenteredText(ToolbarSaveX,ToolbarSaveY+15,Line1$)
		EndIf
	EndIf

	Color TextLevelR,TextLevelG,TextLevelB

	UpdateWater()

	AnimateColors(CurrentObject)
	For i=0 To NofObjects-1
		AnimateColors(LevelObjects(i))
	Next

	;DrawTooltip(CurrentTooltipStartX,CurrentTooltipStartY,CurrentTooltip$)
	;CurrentTooltip$=""

	If NofParticles=MaxNofParticles
		MaxParticleWarningTimer=60
	EndIf

	If MaxParticleWarningTimer<>0
		MaxParticleWarningTimer=MaxParticleWarningTimer-1
		ShowLevelEditorWarning("Too many particles! This will most likely MAV in-game!")
	EndIf

	If NofObjectsInstantiated>MaxNofObjects
		ShowLevelEditorWarning("Instantiated shadows/accessories pass the object limit!")
	EndIf

	FinishDrawing()

End Function

Function AnimateColors(Obj.GameObject)

	If Obj\Attributes\LogicType=90 And Obj\Attributes\LogicSubType=15 And Obj\Attributes\Data0=5 ; General Command 5
		EntityColor Obj\Model\Entity,GetAnimatedFlashing(LevelTimer),60,60
	ElseIf Obj\Attributes\LogicType=200 And Obj\Attributes\Data0=8
		; Animate Rainbow Magic
		For i=0 To 3
		    red=GetAnimatedRainbowRed()
		    green=GetAnimatedRainbowGreen()
		    blue=GetAnimatedRainbowBlue()

		    VertexColor GetSurface(Obj\Model\Entity,1),i,red,green,blue
		Next
	EndIf

End Function

Function SetEditorMode(NewMode)

	If NewMode=8
		; prevent garbage input from level editor movement from appearing in adventure description text
		FlushKeys

		WireFrame False

		If EditorMode=EditorModeTile Or EditorMode=EditorModeObject
			EditorModeBeforeMasterEdit=EditorMode
		EndIf
	EndIf

	If NewMode=0 Or NewMode=3 ; If EditorMode=1 Or EditorMode=2
		Camera1Proj=Camera1SavedProjMode
		Camera3Proj=0
		UpdateCameraProj()
	EndIf

	OldEditorMode=EditorMode
	EditorMode=NewMode

	If OldEditorMode=3 And NewMode=0
		SetBrushToCurrentTile()
	ElseIf OldEditorMode=0 And NewMode=3
		SetBrushToCurrentObject()
	EndIf

	UpdateAllSelectedObjectMarkersVisibility()

	; Edge case: the mouse will be hidden when typing in the editor's real-time textboxes. This line accounts for that.
	ShowPointer()

End Function

Function SetBrushWidth(NewBrushWidth)

	BrushWidth=NewBrushWidth

	If BrushWidth<1
		BrushWidth=1
	ElseIf BrushWidth>100
		BrushWidth=100
	EndIf

	BrushCursorStateWasChanged()

End Function

Function SetBrushHeight(NewBrushHeight)

	BrushHeight=NewBrushHeight

	If BrushHeight<1
		BrushHeight=1
	ElseIf BrushHeight>100
		BrushHeight=100
	EndIf

	BrushCursorStateWasChanged()

End Function

Function PassesPlacementDensityTest()

	If Rnd(0.0,1.0)<=PlacementDensity#
		Return True
	Else
		Return False
	EndIf

End Function

Function ReadColors()

	If FileType(ColorsConfig$)=1
		colorsfile=ReadFile(ColorsConfig$)
		ReadColorsWithHandle(colorsfile)
		CloseFile(colorsfile)
	EndIf

End Function

Function ReadColorsWithHandle(colorsfile)

	If ReadLine(colorsfile)="" Then Return
	RectOnR=ReadLine(colorsfile)
	RectOnG=ReadLine(colorsfile)
	RectOnB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	RectOffR=ReadLine(colorsfile)
	RectOffG=ReadLine(colorsfile)
	RectOffB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	RectGlobalsR=ReadLine(colorsfile)
	RectGlobalsG=ReadLine(colorsfile)
	RectGlobalsB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	RectMarginR=ReadLine(colorsfile)
	RectMarginG=ReadLine(colorsfile)
	RectMarginB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	RectToolbarR=ReadLine(colorsfile)
	RectToolbarG=ReadLine(colorsfile)
	RectToolbarB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	TextLevelR=ReadLine(colorsfile)
	TextLevelG=ReadLine(colorsfile)
	TextLevelB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	TextAdjusterR=ReadLine(colorsfile)
	TextAdjusterG=ReadLine(colorsfile)
	TextAdjusterB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	TextAdjusterHighlightedR=ReadLine(colorsfile)
	TextAdjusterHighlightedG=ReadLine(colorsfile)
	TextAdjusterHighlightedB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	TextMenusR=ReadLine(colorsfile)
	TextMenusG=ReadLine(colorsfile)
	TextMenusB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	TextMenuButtonR=ReadLine(colorsfile)
	TextMenuButtonG=ReadLine(colorsfile)
	TextMenuButtonB=ReadLine(colorsfile)
	If ReadLine(colorsfile)="" Then Return
	TextMenuXR=ReadLine(colorsfile)
	TextMenuXG=ReadLine(colorsfile)
	TextMenuXB=ReadLine(colorsfile)

End Function

Function WriteColors()

	colorsfile=WriteFile(ColorsConfig$)

	WriteLine(colorsfile,"// RGB for currently-selected mode")
	WriteLine(colorsfile,RectOnR)
	WriteLine(colorsfile,RectOnG)
	WriteLine(colorsfile,RectOnB)
	WriteLine(colorsfile,"// RGB for currently-deselected mode")
	WriteLine(colorsfile,RectOffR)
	WriteLine(colorsfile,RectOffG)
	WriteLine(colorsfile,RectOffB)
	WriteLine(colorsfile,"// RGB for the GLOBALS section")
	WriteLine(colorsfile,RectGlobalsR)
	WriteLine(colorsfile,RectGlobalsG)
	WriteLine(colorsfile,RectGlobalsB)
	WriteLine(colorsfile,"// RGB for the margins around the TILES and OBJECTS sections")
	WriteLine(colorsfile,RectMarginR)
	WriteLine(colorsfile,RectMarginG)
	WriteLine(colorsfile,RectMarginB)
	WriteLine(colorsfile,"// RGB for the bottom toolbar")
	WriteLine(colorsfile,RectToolbarR)
	WriteLine(colorsfile,RectToolbarG)
	WriteLine(colorsfile,RectToolbarB)
	WriteLine(colorsfile,"// RGB for regular level editor text")
	WriteLine(colorsfile,TextLevelR)
	WriteLine(colorsfile,TextLevelG)
	WriteLine(colorsfile,TextLevelB)
	WriteLine(colorsfile,"// RGB for regular object adjusters")
	WriteLine(colorsfile,TextAdjusterR)
	WriteLine(colorsfile,TextAdjusterG)
	WriteLine(colorsfile,TextAdjusterB)
	WriteLine(colorsfile,"// RGB for highlighted object adjusters")
	WriteLine(colorsfile,TextAdjusterHighlightedR)
	WriteLine(colorsfile,TextAdjusterHighlightedG)
	WriteLine(colorsfile,TextAdjusterHighlightedB)
	WriteLine(colorsfile,"// RGB for menu layout text")
	WriteLine(colorsfile,TextMenusR)
	WriteLine(colorsfile,TextMenusG)
	WriteLine(colorsfile,TextMenusB)
	WriteLine(colorsfile,"// RGB for menu buttons")
	WriteLine(colorsfile,TextMenuButtonR)
	WriteLine(colorsfile,TextMenuButtonG)
	WriteLine(colorsfile,TextMenuButtonB)
	WriteLine(colorsfile,"// RGB for dim menu elements such as the X's")
	WriteLine(colorsfile,TextMenuXR)
	WriteLine(colorsfile,TextMenuXG)
	WriteLine(colorsfile,TextMenuXB)

	CloseFile(colorsfile)

End Function

Function StartupColors()

	ReadColors()
	WriteColors()

End Function

Function ReadControls()

	file=ReadFile(EditorControls$)
	KeyMoveNorth=ReadInt(file)
	KeyMoveWest=ReadInt(file)
	KeyMoveSouth=ReadInt(file)
	KeyMoveEast=ReadInt(file)
	CloseFile file

End Function

Function WriteControls()

	file=WriteFile(EditorControls$)
	WriteInt(file,KeyMoveNorth)
	WriteInt(file,KeyMoveWest)
	WriteInt(file,KeyMoveSouth)
	WriteInt(file,KeyMoveEast)
	CloseFile file

End Function

Function StartupControls()

	If FileExists(EditorControls$)
		ReadControls()
	Else
		WriteControls()
	EndIf

End Function

Function ReadTexturePrefixes()

	file=ReadFile(TexturePrefixesConfig$)

	For i=0 To MaxTexturePrefix
		TexturePrefix$(i)=ReadLine(file)
	Next

	CloseFile file

End Function

Function WriteTexturePrefixes()

	file=WriteFile(TexturePrefixesConfig$)

	For i=0 To MaxTexturePrefix
		WriteLine(file,TexturePrefix$(i))
	Next

	CloseFile file

End Function

Function StartupTexturePrefixes()

	If FileExists(TexturePrefixesConfig$)
		ReadTexturePrefixes()
	Else
		WriteTexturePrefixes()
	EndIf

End Function

Function StartupConfigs()

	StartupColors()
	StartupControls()
	StartupTexturePrefixes()

End Function

Function ReadConfigs()

	ReadColors()
	ReadControls()
	ReadTexturePrefixes()

End Function

Function GetKeyFromUser()

	While True
		For i=0 To 237
			If KeyDown(i)
				Return i
			EndIf
		Next
	Wend

End Function

Function AnyKeyDown()

	For i=0 To 237
		If KeyDown(i)
			Return True
		EndIf
	Next
	Return False

End Function

Function ConfigureKeyboardKey(KeyName$)

	PrintMessageForInstant("Press the key you want to use to "+KeyName$+".")
	Result=GetKeyFromUser()

	Repeat
	Until Not KeyDown(Result)

	Return Result

End Function

Function ConfigureControls()

	If Not GetConfirmation("Do you want to configure your keyboard mappings?")
		Return
	EndIf

	Repeat
	Until Not AnyKeyDown()

	FlushKeys

	KeyMoveNorth=ConfigureKeyboardKey("MOVE NORTH")
	KeyMoveWest=ConfigureKeyboardKey("MOVE WEST")
	KeyMoveSouth=ConfigureKeyboardKey("MOVE SOUTH")
	KeyMoveEast=ConfigureKeyboardKey("MOVE EAST")

	WriteControls()

	ShowMessage("Controls configured!",1000)

End Function

Function GetCenteredTextOffset(Message$)

	Return 4*Len(Message$)

End Function

Function GetTextPixelLength(Message$)

	Return 8*Len(Message$)

End Function

Function CenteredText(StartX,StartY,Message$)

	Text StartX-GetCenteredTextOffset(Message$),StartY,Message$

End Function

Function RightAlignedText(StartX,StartY,Message$)

	Text StartX-GetTextPixelLength(Message$),StartY,Message$

End Function

Function OutlinedText(x#,y#,Message$,InsideColorR,InsideColorG,InsideColorB)

	Color 0,0,0
	Text x#+1,y#+1,Message$
	Text x#+1,y#-1,Message$
	Text x#-1,y#+1,Message$
	Text x#-1,y#-1,Message$
	Color InsideColorR,InsideColorG,InsideColorB
	Text x#,y#,Message$

End Function

Function DrawTextRectangle(StartX,StartY,Message$,RectR,RectG,RectB,TextR,TextG,TextB)

	If Message$="" Then Return

	TextPixelLength=GetTextPixelLength(Message$)
	HorizontalPadding=10
	TotalHorizontalPadding=HorizontalPadding*2
	RectangleWidth=TextPixelLength+TotalHorizontalPadding
	StartX=StartX-TotalHorizontalPadding

	; clamp the rectangle position so that the rectangle doesn't spill outside the window
	If StartX+RectangleWidth>GfxWidth
		StartX=GfxWidth-RectangleWidth
	ElseIf StartX<0
		StartX=0
	EndIf

	Color RectR,RectG,RectB
	Rect StartX,StartY-40,RectangleWidth,30,True
	Color TextR,TextG,TextB
	Text StartX+HorizontalPadding,StartY-30,Message$

End Function

Function DrawTooltip(StartX,StartY,Message$)

	DrawTextRectangle(StartX,StartY,Message$,RectToolbarR,RectToolbarG,RectToolbarB,TextLevelR,TextLevelG,TextLevelB)

End Function

Global CurrentTooltipStartX
Global CurrentTooltipStartY
Global CurrentTooltip$

Function ShowTooltipLeftAligned(StartX,StartY,Message$)

	TheInstr=Instr(Message$,"{PARTICLE}")
	If TheInstr=0
		CurrentTooltipStartX=StartX
		CurrentTooltipStartY=StartY
		CurrentTooltip$=Message$
	Else
		tex=Mid$(Message$,TheInstr+10,Len(Message$)-TheInstr-9)
		ShowParticlePreviewLeftAligned(StartX,StartY,tex)
	EndIf

End Function

Function ShowTooltipRightAligned(StartX,StartY,Message$)

	TheInstr=Instr(Message$,"{PARTICLE}")
	If TheInstr=0
		CurrentTooltipStartX=StartX-GetTextPixelLength(Message$)
		CurrentTooltipStartY=StartY
		CurrentTooltip$=Message$
	Else
		tex=Mid$(Message$,TheInstr+10,Len(Message$)-TheInstr-9)
		ShowParticlePreviewRightAligned(StartX,StartY,tex)
	EndIf

End Function

Function ShowTooltipCenterAligned(StartX,StartY,Message$)

	TheInstr=Instr(Message$,"{PARTICLE}")
	If TheInstr=0
		CurrentTooltipStartX=StartX-GetCenteredTextOffset(Message$)
		CurrentTooltipStartY=StartY
		CurrentTooltip$=Message$
	Else
		tex=Mid$(Message$,TheInstr+10,Len(Message$)-TheInstr-9)
		ShowParticlePreviewCenterAligned(StartX,StartY,tex)
	EndIf

End Function

Function SetDupeMode(NewDupeMode)

	DupeMode=NewDupeMode

	If DupeMode<0
		DupeMode=DupeModeMax
	ElseIf DupeMode>DupeModeMax
		DupeMode=0
	EndIf

	Select DupeMode
	Case DupeModeX
		SetBrushMode(BrushModeSetMirror)
		ShowEntity(MirrorEntityX)
		HideEntity(MirrorEntityY)
	Case DupeModeY
		SetBrushMode(BrushModeSetMirror)
		HideEntity(MirrorEntityX)
		ShowEntity(MirrorEntityY)
	Case DupeModeXPlusY
		SetBrushMode(BrushModeSetMirror)
		ShowEntity(MirrorEntityX)
		ShowEntity(MirrorEntityY)
	Default
		If BrushMode=BrushModeSetMirror
			SetBrushMode(BrushModeNormal)
		EndIf
		HideEntity(MirrorEntityX)
		HideEntity(MirrorEntityY)
	End Select

End Function

Function SetBrushMode(NewBrushMode)

	FloodedElementsClear()
	BrushMode=NewBrushMode
	BlockPlacingMode=BlockPlacingModeNone
	BrushCursorStateWasChanged()

End Function

Function SetBrushCursorPosition(x,y)

	PositionChanged=Not PositionIsEqual(x,y,BrushCursorX,BrushCursorY)

	BrushCursorX=x
	BrushCursorY=y

	;HideEntity BlockModeMesh
	If BrushCursorX<>BrushCursorInvalid And BrushCursorY<>BrushCursorInvalid And BrushMode=BrushModeBlockPlacing
		; show the block
		;ShowEntity BlockModeMesh
		;EntityColor BlockModeMesh,BrushR,BrushG,BrushB
		If BrushCursorX>BlockCornerx
			cornleft=Blockcornerx
			cornright=BrushCursorX
		Else
			cornleft=BrushCursorX
			cornright=Blockcornerx
		EndIf
		If BrushCursorY>BlockCornery
			cornup=BlockCornery
			corndown=BrushCursorY
		Else
			cornup=BrushCursorY
			corndown=blockcornery
		EndIf
		;VertexCoords BlockModeSurface,0,cornleft-0,0.1,-(cornup)
		;VertexCoords BlockModeSurface,1,cornright+1,0.1,-(cornup)
		;VertexCoords BlockModeSurface,2,cornleft-0,0.1,-(corndown+1)
		;VertexCoords BlockModeSurface,3,cornright+1,0.1,-(corndown+1)
	EndIf

	If PositionChanged
		BrushCursorPositionWasChanged()
	EndIf

End Function

Function BrushCursorPositionWasChanged()

	; object dragging ; dragging
	If NofDraggedObjects<>0
		DragChange=True

		OldX=DragSpotX
		DragSpotDeltaX=BrushCursorX-DragSpotX
		If DragSpotDeltaX>0
			If DragMaxTileX+DragSpotDeltaX<=100
				DragSpotX=DragSpotX+DragSpotDeltaX
			Else
				DragSpotX=100-(DragMaxTileX-DragSpotX)
			EndIf
		Else
			If DragMinTileX+DragSpotDeltaX>=0
				DragSpotX=DragSpotX+DragSpotDeltaX
			Else
				DragSpotX=DragSpotX-DragMinTileX
			EndIf
		EndIf
		DragSpotDeltaX=DragSpotX-OldX
		DragMinTileX=DragMinTileX+DragSpotDeltaX
		DragMaxTileX=DragMaxTileX+DragSpotDeltaX

		OldY=DragSpotY
		DragSpotDeltaY=BrushCursorY-DragSpotY
		If DragSpotDeltaY>0
			If DragMaxTileY+DragSpotDeltaY<=100
				DragSpotY=DragSpotY+DragSpotDeltaY
			Else
				DragSpotY=100-(DragMaxTileY-DragSpotY)
			EndIf
		Else
			If DragMinTileY+DragSpotDeltaY>=0
				DragSpotY=DragSpotY+DragSpotDeltaY
			Else
				DragSpotY=DragSpotY-DragMinTileY
			EndIf
		EndIf
		DragSpotDeltaY=DragSpotY-OldY
		DragMinTileY=DragMinTileY+DragSpotDeltaY
		DragMaxTileY=DragMaxTileY+DragSpotDeltaY

		For i=0 To NofDraggedObjects-1
			CurrentDraggedObject=DraggedObjects(i)
			DraggedPosition.GameObjectPosition=LevelObjects(CurrentDraggedObject)\Position
			TileX=DraggedPosition\TileX
			TileY=DraggedPosition\TileY
			DecrementLevelTileObjectCount(TileX,TileY)
			SetObjectPosition(CurrentDraggedObject,TileX+DragSpotDeltaX,TileY+DragSpotDeltaY)
			UpdateObjectPosition(CurrentDraggedObject)
		Next

		ObjectsWereChanged()
	EndIf

	; tile dragging
	If TileDragging=True
		DragChange=True

		PasteLevelFromCopy()

		DeltaX=BrushCursorX-DraggedTilesSpotX
		DeltaY=BrushCursorY-DraggedTilesSpotY

		For i=0 To LevelWidth-1
			For j=0 To LevelHeight-1
				TargetX=i+DeltaX
				TargetY=j+DeltaY
				If IsPositionInLevel(TargetX,TargetY)
					If DraggedTilesEnabled(i,j)
						CopyTile(DraggedTiles(i,j),LevelTiles(TargetX,TargetY))

						;ShowMessage(i+","+j+" to "+TargetX+","+TargetY+" with texture "+DraggedTiles(i,j)\Terrain\Texture,1000)
					EndIf
				EndIf
			Next
		Next

		For i=0 To LevelWidth-1
			For j=0 To LevelHeight-1
				UpdateTile(i,j)
			Next
		Next

		SomeTileWasChanged()
	EndIf

	If BrushMode=BrushModeSetMirror
		MirrorPositionX=BrushCursorX
		MirrorPositionY=BrushCursorY

		PositionEntityInLevel(MirrorEntityX,BrushCursorX+0.5,0.5)
		PositionEntityInLevel(MirrorEntityY,0.5,BrushCursorY+0.5)
	ElseIf MirrorPositionX=BrushCursorInvalid And MirrorPositionY=BrushCursorInvalid
		HideEntity MirrorEntityX
		HideEntity MirrorEntityY
	EndIf

	OnceTilePlacement=True
	BrushCursorStateWasChanged()

End Function

Function BrushCursorOffMap()

	HideCursors()
	HideBrushSurface()
	SetBrushCursorPosition(BrushCursorInvalid,BrushCursorInvalid)

End Function

Function BrushCursorProbablyModifiedTiles()

	ClearBrushSurface()

	FloodedElementsClear()

	BrushCursorStateWasChanged()

	For j=0 To MaxLevelCoordinate
		If DirtyNormals(j)
			DirtyNormals(j)=False
			RecalculateNormals(j)
		EndIf
	Next

End Function

Function BrushCursorStateWasChanged()

	;ShowMessage("Brush cursor state changed",1000)

	CalculateBrushTargets()

End Function

Function CalculateBrushTargets()

	If BrushMode=BrushModeNormal
		FloodedElementsClear()
		BrushXStart=GetBrushXStart()
		BrushYStart=GetBrushYStart()
		For i=0 To BrushWidth-1
			For j=0 To BrushHeight-1
				AddToFloodedStack(BrushXStart+i,BrushYStart+j)
			Next
		Next
		GenerateBrushSurface()
	ElseIf BrushMode=BrushModeBlockPlacing
		FloodedElementsClear()
		For i=cornleft To cornright
			For j=cornup To corndown
				If FloodedElementCount<>MaxTilesPerLevel
					AddToFloodedStack(i,j)
				EndIf
			Next
		Next

		GenerateBrushSurface()
	ElseIf BrushMode=BrushModeFill Or IsBrushInInlineMode() Or IsBrushInOutlineMode() Or BrushMode=BrushModeRow Or BrushMode=BrushModeColumn Or BrushMode=BrushModeDiagonalNE Or BrushMode=BrushModeDiagonalSE
		; Don't redo the flood fill if it is unnecessary.
		If Not FloodedStackHasTile(BrushCursorX,BrushCursorY)
			If BrushMode=BrushModeFill
				FloodFill(BrushCursorX,BrushCursorY)
			ElseIf IsBrushInInlineMode()
				FloodFillInline(BrushCursorX,BrushCursorY,BrushMode=BrushModeInlineHard)
			ElseIf IsBrushInOutlineMode()
				FloodFillOutline(BrushCursorX,BrushCursorY,BrushMode=BrushModeOutlineHard)
			ElseIf BrushMode=BrushModeRow
				FloodFillRow(BrushCursorX,BrushCursorY)
			ElseIf BrushMode=BrushModeColumn
				FloodFillColumn(BrushCursorX,BrushCursorY)
			ElseIf BrushMode=BrushModeDiagonalNE
				FloodFillDiagonalNE(BrushCursorX,BrushCursorY)
			ElseIf BrushMode=BrushModeDiagonalSE
				FloodFillDiagonalSE(BrushCursorX,BrushCursorY)
			EndIf
			GenerateBrushSurface()
		EndIf
	Else
		FloodedElementsClear()
		GenerateBrushSurface()
	EndIf

	GenerateBrushPreview()

End Function

Function GenerateBrushSurface()

	ClearBrushSurface()

	If BrushCursorX<>BrushCursorInvalid And BrushCursorY<>BrushCursorInvalid
		If FloodedElementCount=0
			AddToFloodedStack(BrushCursorX,BrushCursorY)
		EndIf
	EndIf

	BrushSpaceX=LevelSpaceToBrushSpaceX(x,BrushWrap)
	BrushSpaceY=LevelSpaceToBrushSpaceY(y,BrushWrap)

	For i=0 To FloodedElementCount-1
		thisx=FloodedStackX(i)
		thisy=FloodedStackY(i)
		AddTileToBrushSurface(BrushSurface,thisx,thisy,BrushSpaceX,BrushSpaceY,False)
	Next

	FinishBrushSurface()

End Function

Function GenerateBrushPreview()

	ClearBrushPreviewSurface()

	For i=0 To FloodedElementCount-1
		thisx=FloodedStackX(i)
		thisy=FloodedStackY(i)
		AddTileToBrushPreview(thisx,thisy)
	Next

End Function

; Wow! What a great Object-Oriented Programming Constructor I have just written!
Function FloodFillInitializeState(StartX,StartY)

	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			LevelTileVisited(i,j)=False
		Next
	Next

	FloodedElementsClear()

	If IsPositionInLevel(StartX,StartY)
		SetLevelTileAsTarget(StartX,StartY)
		FloodStackX(0)=StartX
		FloodStackY(0)=StartY
		LevelTileVisited(StartX,StartY)=True
		FloodElementCount=1
	Else
		FloodElementCount=0
	EndIf

End Function

Function FloodedElementsClear()

	FloodedElementCount=0

End Function

Function AddToFloodedStack(NewX,NewY)

	FloodedStackX(FloodedElementCount)=NewX
	FloodedStackY(FloodedElementCount)=NewY
	FloodedElementCount=FloodedElementCount+1

End Function

Function FloodFillVisitLevelTile(nextx,nexty)

	If LevelTileMatchesTarget(nextx,nexty)
		If LevelTileVisited(nextx,nexty)=False
			LevelTileVisited(nextx,nexty)=True
			FloodFillPlanToVisitLevelTile(nextx,nexty)
		EndIf
	Else
		FloodOutsideAdjacent=True
	EndIf

End Function

Function FloodFillVisitLevelTileOutline(nextx,nexty)

	If LevelTileVisited(nextx,nexty)=False
		LevelTileVisited(nextx,nexty)=True
		If LevelTileMatchesTarget(nextx,nexty)
			FloodFillPlanToVisitLevelTile(nextx,nexty)
		Else
			AddToFloodedStack(nextx,nexty)
		EndIf
	EndIf

End Function

Function FloodFillPlanToVisitLevelTile(nextx,nexty)

	FloodStackX(FloodElementCount)=nextx
	FloodStackY(FloodElementCount)=nexty
	FloodElementCount=FloodElementCount+1

End Function

Function FloodedStackHasTile(x,y)

	For i=0 To FloodedElementCount-1
		thisx=FloodedStackX(i)
		thisy=FloodedStackY(i)
		If PositionIsEqual(x,y,thisx,thisy)
			Return True
		EndIf
	Next
	Return False

End Function

Function FloodFill(StartX,StartY)

	FloodFillInitializeState(BrushCursorX,BrushCursorY)
	While FloodElementCount<>0
		FloodElementCount=FloodElementCount-1
		thisx=FloodStackX(FloodElementCount)
		thisy=FloodStackY(FloodElementCount)

		FloodFillVisitLevelTile(thisx-1,thisy)
		FloodFillVisitLevelTile(thisx+1,thisy)
		FloodFillVisitLevelTile(thisx,thisy-1)
		FloodFillVisitLevelTile(thisx,thisy+1)

		AddToFloodedStack(thisx,thisy)
	Wend

End Function

Function FloodFillRow(StartX,StartY)

	FloodFillInitializeState(BrushCursorX,BrushCursorY)
	While FloodElementCount<>0
		FloodElementCount=FloodElementCount-1
		thisx=FloodStackX(FloodElementCount)
		thisy=FloodStackY(FloodElementCount)

		FloodFillVisitLevelTile(thisx-1,thisy)
		FloodFillVisitLevelTile(thisx+1,thisy)

		AddToFloodedStack(thisx,thisy)
	Wend

End Function

Function FloodFillColumn(StartX,StartY)

	FloodFillInitializeState(BrushCursorX,BrushCursorY)
	While FloodElementCount<>0
		FloodElementCount=FloodElementCount-1
		thisx=FloodStackX(FloodElementCount)
		thisy=FloodStackY(FloodElementCount)

		FloodFillVisitLevelTile(thisx,thisy-1)
		FloodFillVisitLevelTile(thisx,thisy+1)

		AddToFloodedStack(thisx,thisy)
	Wend

End Function

Function FloodFillDiagonalNE(StartX,StartY)

	FloodFillInitializeState(BrushCursorX,BrushCursorY)
	While FloodElementCount<>0
		FloodElementCount=FloodElementCount-1
		thisx=FloodStackX(FloodElementCount)
		thisy=FloodStackY(FloodElementCount)

		FloodFillVisitLevelTile(thisx-1,thisy+1)
		FloodFillVisitLevelTile(thisx+1,thisy-1)

		AddToFloodedStack(thisx,thisy)
	Wend

End Function

Function FloodFillDiagonalSE(StartX,StartY)

	FloodFillInitializeState(BrushCursorX,BrushCursorY)
	While FloodElementCount<>0
		FloodElementCount=FloodElementCount-1
		thisx=FloodStackX(FloodElementCount)
		thisy=FloodStackY(FloodElementCount)

		FloodFillVisitLevelTile(thisx-1,thisy-1)
		FloodFillVisitLevelTile(thisx+1,thisy+1)

		AddToFloodedStack(thisx,thisy)
	Wend

End Function

Function FloodFillInline(StartX,StartY,IsHard)

	FloodFillInitializeState(StartX,StartY)
	While FloodElementCount<>0
		FloodOutsideAdjacent=False

		FloodElementCount=FloodElementCount-1
		thisx=FloodStackX(FloodElementCount)
		thisy=FloodStackY(FloodElementCount)

		FloodFillVisitLevelTile(thisx-1,thisy)
		FloodFillVisitLevelTile(thisx+1,thisy)
		FloodFillVisitLevelTile(thisx,thisy-1)
		FloodFillVisitLevelTile(thisx,thisy+1)

		If IsHard
			If (Not LevelTileMatchesTarget(thisx-1,thisy-1)) Or (Not LevelTileMatchesTarget(thisx+1,thisy-1)) Or (Not LevelTileMatchesTarget(thisx-1,thisy+1)) Or (Not LevelTileMatchesTarget(thisx+1,thisy+1))
				FloodOutsideAdjacent=True
			EndIf
		EndIf

		If FloodOutsideAdjacent
			AddToFloodedStack(thisx,thisy)
		EndIf
	Wend

End Function

Function FloodFillOutline(StartX,StartY,IsHard)

	FloodFillInitializeState(StartX,StartY)
	While FloodElementCount<>0
		FloodElementCount=FloodElementCount-1
		thisx=FloodStackX(FloodElementCount)
		thisy=FloodStackY(FloodElementCount)

		FloodFillVisitLevelTileOutline(thisx-1,thisy)
		FloodFillVisitLevelTileOutline(thisx+1,thisy)
		FloodFillVisitLevelTileOutline(thisx,thisy-1)
		FloodFillVisitLevelTileOutline(thisx,thisy+1)

		If IsHard
			FloodFillVisitLevelTileOutline(thisx-1,thisy-1)
			FloodFillVisitLevelTileOutline(thisx+1,thisy-1)
			FloodFillVisitLevelTileOutline(thisx-1,thisy+1)
			FloodFillVisitLevelTileOutline(thisx+1,thisy+1)
		EndIf
	Wend

End Function

Function KeyPressed(i)

	If KeyDown(i) And KeyReleased(i)
		KeyReleased(i)=False
		Return True
	Else
		Return False
	EndIf

End Function

Function TrueToYes$(Bool)

	If Bool=True
		Return "Yes"
	Else
		Return "No"
	EndIf

End Function

Function OneToYes$(Value)

	If Value=1
		Return "Yes"
	Else
		Return "No"
	EndIf

End Function

Function ZeroToYes$(Value)

	If Value=0
		Return "Yes"
	Else
		Return "No"
	EndIf

End Function

Function MaybePluralize$(TargetString$,Count)

	If Count=1
		Return TargetString$
	Else
		If TargetString$="has"
			Return "have"
		Else
			Return TargetString$+"s"
		EndIf
	EndIf

End Function

Function ReturnPressed()

	If ReturnKey=True And ReturnKeyReleased=True
		ReturnKeyReleased=False
		Return True
	EndIf
	Return False

End Function

Function MouseDebounceFinished()

	Return MouseDebounceTimer=0

End Function

Function MouseDebounceSet(Timer)

	MouseDebounceTimer=Timer

End Function

Function LeftMouseDown()

	Return MouseDown(1)=True Or KeyDown(2) ; 1 key

End Function

Function RightMouseDown()

	Return MouseDown(2)=True Or KeyDown(3) ; 2 key

End Function

Function EditorGlobalControls()

	ShowingError=False

	If MouseDebounceTimer>0
		MouseDebounceTimer=MouseDebounceTimer-1
	EndIf

	If LeftMouseDown()
		LeftMouse=True
	Else
		If LeftMouse=True
			LeftMouse=False
			MouseDebounceTimer=0
		EndIf

		LeftMouseReleased=True
	EndIf

	If RightMouseDown()
		RightMouse=True
	Else
		If RightMouse=True
			RightMouse=False
			MouseDebounceTimer=0
		EndIf

		RightMouseReleased=True
	EndIf

	MouseScroll=MouseZSpeed()

	If KeyDown(28) Or KeyDown(156)
		ReturnKey=True
	Else
		ReturnKey=False
		ReturnKeyReleased=True
	EndIf

	If KeyDown(211)
		DeleteKey=True
	Else
		DeleteKey=False
		DeleteKeyReleased=True
	EndIf

	For i=1 To KeyCount
		If Not KeyDown(i)
			KeyReleased(i)=True
		EndIf
	Next

	; Disabled indefinitely due to potential instability.
	; Getting this to work would take a huge amount of effort.
	;If KeyPressed(66) And displayfullscreen=False ; F8 key
	;	ResetWindowSize()
	;EndIf

End Function

Function RenderToolbar()

	Color RectToolbarR,RectToolbarG,RectToolbarB
	;Rect 0,500,500,12,True
	Rect 0,LevelViewportHeight,GfxWidth,100,True

End Function

Function DrawStepSize(x,y,StepSize#)

	DrawTextRectangle(x,y,StepSize#, 255,100,0, 0,0,0)

End Function

Function RunStepSize()

	For i=0 To BrushSpaceWidth-1
		For j=0 To BrushSpaceHeight-1
			RunStepSizeForTile(BrushTiles(i,j))
		Next
	Next

End Function

Function RunStepSizeForTile(TheTile.Tile)

	TheTile\Terrain\Random#=TheTile\Terrain\Random#+StepSizeTileRandom#
	TheTile\Terrain\Height#=TheTile\Terrain\Height#+StepSizeTileHeight#
	TheTile\Terrain\Extrusion#=TheTile\Terrain\Extrusion#+StepSizeTileExtrusion#
	TheTile\Water\Height#=TheTile\Water\Height#+StepSizeWaterTileHeight#
	TheTile\Water\Turbulence#=TheTile\Water\Turbulence#+StepSizeWaterTileTurbulence#

End Function

Function SetUseStateOfAllTileAttributes(NewState)

	CurrentTileTextureUse=NewState
	CurrentTileSideTextureUse=NewState
	CurrentTileHeightUse=NewState
	CurrentTileExtrusionUse=NewState
	CurrentTileRandomUse=NewState
	CurrentTileRoundingUse=NewState
	CurrentTileEdgeRandomUse=NewState
	CurrentTileLogicUse=NewState
	CurrentWaterTileTextureUse=NewState
	CurrentWaterTileHeightUse=NewState
	CurrentWaterTileTurbulenceUse=NewState

End Function

Function MaybeUnuseAllTileAttributes(MyState)

	If ShiftDown()
		If MyState=False
			SetUseStateOfAllTileAttributes(True)
			Return False
		Else
			SetUseStateOfAllTileAttributes(False)
			Return True
		EndIf
	EndIf
	Return True

End Function

Function GetNumberOfCursorsInDupeMode(Value)

	Select Value
	Case DupeModeNone
		Return 1
	Case DupeModeX,DupeModeY
		Return 2
	Case DupeModeXPlusY
		Return 4
	End Select

End Function

Function PositionCursorEntity(i,x,y)

	ShowEntity CursorMeshPillar(i)
	ShowEntity CursorMeshOpaque(i)
	PositionEntity CursorMeshPillar(i),x+.5,GetLevelTileTotalHeight(x,y),-y-.5
	PositionEntity CursorMeshOpaque(i),x+.5,0,-y-.5

End Function

Function GetTileTotalHeight#(TheTile.Tile)

	Return TheTile\Terrain\Extrusion+TheTile\Terrain\Height ;+TheTile\Terrain\Random

End Function

Function GetLevelTileTotalHeight#(x,y)

	If IsPositionInLevel(x,y)
		Return GetTileTotalHeight(LevelTiles(x,y))
	Else
		Return 0
	EndIf

End Function

Function HideCursors()

	ClearBrushSurface()

	For i=0 To 3
		HideEntity CursorMeshPillar(i)
		HideEntity CursorMeshOpaque(i)
	Next

End Function

Function SetWhereWeEndedUpMarker(x,y)

	;ShowMessage("Marker ended up at "+x+","+y,1000)
	SetEntityPositionInWorld(WhereWeEndedUpMarker,Float(x)+0.5,Float(y)+0.5,0.0)
	WhereWeEndedUpAlpha#=0.5

End Function

Function EndUpAt(level,x,y)

	AccessLevelAt(level,x,y)
	SetWhereWeEndedUpMarker(x,y)

End Function

; Returns True if the object is at x,y.
Function TryLevelGoto(i,x,y,D1,D2,D3)

	If LevelObjects(i)\Position\TileX=x And LevelObjects(i)\Position\TileY=y
		Attributes.GameObjectAttributes=LevelObjects(i)\Attributes
		ToLevel=GetDataByIndex(Attributes,D1)
		If ToLevel=CurrentLevelNumber
			PositionCameraInLevel(GetDataByIndex(Attributes,D2),GetDataByIndex(Attributes,D3))
			SetWhereWeEndedUpMarker(GetDataByIndex(Attributes,D2),GetDataByIndex(Attributes,D3))
		ElseIf AskToSaveLevelAndExit()
			; Destination level might have changed from possible object update event, so we read from Data1 again.
			EndUpAt(GetDataByIndex(Attributes,D1),GetDataByIndex(Attributes,D2),GetDataByIndex(Attributes,D3))
		EndIf

		Return True
	Else
		Return False
	EndIf

End Function

Function EditorLocalRendering()

	; full window size is 800x600, whereas the level camera viewport is 500x500
	; draw black regions so stray text doesn't linger there
	Color RectMarginR,RectMarginG,RectMarginB
	Rect SidebarX,SidebarY,10,LevelViewportHeight,True ; between level camera and object/tile editors
	Rect SidebarX+10,SidebarY,290,20,True ; backdrop for the labels of TILES and GLOBALS
	Rect SidebarX+210,SidebarY+20,5,220,True ; between TILES and GLOBALS
	Rect SidebarX+215,SidebarY+85,80,15 ; between level dimensions and the rest of GLOBALS
	Rect SidebarX+295,SidebarY+20,5,LevelViewportHeight,True ; to the right of GLOBALS
	Rect SidebarX+10,SidebarY+240,205,5,True ; beneath TILES and to the left of GLOBALS
	Rect SidebarX+10,SidebarY+285,285,20,True ; backdrop for the label of OBJECTS
	Rect SidebarX+10,SidebarY+455,285,5,True ; between the object adjusters and object categories
	Rect SidebarX+195,SidebarY+430,100,5,True ; between object camera and More button

	If EditorMode=0
		TileColorR=RectOnR
		TileColorG=RectOnG
		TileColorB=RectOnB
		ObjectColorR=RectOffR
		ObjectColorG=RectOffG
		ObjectColorB=RectOffB
		UpdateCameraClsColor()
	Else If EditorMode=3
		TileColorR=RectOffR
		TileColorG=RectOffG
		TileColorB=RectOffB
		ObjectColorR=RectOnR
		ObjectColorG=RectOnG
		ObjectColorB=RectOnB
		UpdateCameraClsColor()
	EndIf

	Color TextLevelR,TextLevelG,TextLevelB
	Text SidebarX+90,SidebarY+5,"TILES"

	Color RectGlobalsR,RectGlobalsG,RectGlobalsB
	Rect SidebarX+214,SidebarY+100,81,145,True
	Color TextLevelR,TextLevelG,TextLevelB

	LevelWeatherString$=GetWeatherName$(LevelWeather)
	;If Len(LevelWeatherString$) Mod 2=0
	;	Text 715,100,LevelWeatherString$
	;Else
	;	Text 719,100,LevelWeatherString$
	;EndIf
	CenteredText(SidebarX+254,SidebarY+100,LevelWeatherString$)

	Line1$=GetMusicName$(LevelMusic)
	;Text SidebarX+219,SidebarY+115,Line1$
	CenteredText(SidebarX+254,SidebarY+115,Line1$)

	LevelEdgeStyleString$=GetLevelEdgeStyleChar$(LevelEdgeStyle)

	Text SidebarX+215,SidebarY+133," LevelTex "
	Text SidebarX+215,SidebarY+150," WaterTex "
	Text FlStartX,FlStartY," Fl Tr Gl B"
	Text FlStartX+12,SidebarY+180,Str$(WaterFlow)
	Text FlStartX+36,SidebarY+180,Str$(WaterTransparent)
	Text FlStartX+60,SidebarY+180,Str$(WaterGlow)
	Text FlStartX+80,SidebarY+180,LevelEdgeStyleString$
	Text SidebarX+223,SidebarY+200,"  Light  "
	Text SidebarX+212,SidebarY+215,Str$(LightRed)
	Text SidebarX+241,SidebarY+215,Str$(LightGreen)
	Text SidebarX+270,SidebarY+215,Str$(LightBlue)
	Text SidebarX+212,SidebarY+228,Str$(AmbientRed)
	Text SidebarX+241,SidebarY+228,Str$(AmbientGreen)
	Text SidebarX+270,SidebarY+228,Str$(AmbientBlue)

	StartX=SidebarX+10
	StartY=SidebarY+245
	Color TileColorR,TileColorG,TileColorB
	Rect StartX,StartY,285,40,True
	Color TextLevelR,TextLevelG,TextLevelB
	Text StartX+2,StartY+2,"                                   "
	Text StartX+2+285/2-4*(Len(TilePresetCategoryName$(CurrentTilePresetCategory))+10),StartY,"Category: "+TilePresetCategoryName$(CurrentTilePresetCategory)
	Text StartX+2,StartY+22,"                                   "
	Text StartX+2+285/2-4*(Len(TilePresetTileName$(CurrentTilePresetTile))+2),StartY+22,"Tile: "+Left$(TilePresetTileName$(CurrentTilePresetTile),Len(TilePresetTileName$(CurrentTilePresetTile))-4)

	StartX=SidebarX+195 ;695
	StartY=SidebarY+435

	Color ObjectColorR,ObjectColorG,ObjectColorB
	Rect StartX,StartY,100,20,True
	Color TextLevelR,TextLevelG,TextLevelB

	If NofSelectedObjects<>0 And CurrentGrabbedObjectModified
		Text StartX+50,StartY+2,"Update"
	EndIf

	If NofObjectAdjusters>9
		; formerly the "More" button, located at StartX+6
		; ceiling division would be nice at NofObjectAdjusters...
		Text StartX,StartY+2,"Pg"+(ObjectAdjusterStart/9+1)+"/"+((NofObjectAdjusters-1)/9+1)
	EndIf

	StartX=SidebarX+10 ;510
	StartY=SidebarY+460

	Color ObjectColorR,ObjectColorG,ObjectColorB
	Rect StartX,StartY,285,40,True
	Color TextLevelR,TextLevelG,TextLevelB
	Text StartX+2,StartY+2,"                                   "
	Text StartX+2+285/2-4*(Len(ObjectPresetCategoryName$(CurrentObjectPresetCategory))+10),StartY,"Category: "+ObjectPresetCategoryName$(CurrentObjectPresetCategory)
	Text StartX+2,StartY+22,"                                   "
	Text StartX+2+285/2-4*(Len(ObjectPresetObjectName$(CurrentObjectPresetObject))+4),StartY+22,"Object: "+Left$(ObjectPresetObjectName$(CurrentObjectPresetObject),Len(ObjectPresetObjectName$(CurrentObjectPresetObject))-4)

	StartX=SidebarX+215 ;715
	StartY=SidebarY+20
	Text StartX+4,StartY-15," GLOBALS" ;719,5," GLOBALS"
	Color RectGlobalsR,RectGlobalsG,RectGlobalsB
	Rect StartX,StartY,80,35,True
	Color TextLevelR,TextLevelG,TextLevelB
	Text StartX+20,StartY+2,"Width"
	Text StartX,StartY+15,"<<"
	Text StartX+80-16,StartY+15,">>"
	CenteredText(StartX+40,StartY+15,Str$(LevelWidth))

	StartY=50
	Color RectGlobalsR,RectGlobalsG,RectGlobalsB
	Rect StartX,StartY,80,35,True
	Color TextLevelR,TextLevelG,TextLevelB
	Text StartX+16,StartY+2,"Height"
	Text StartX,StartY+19,"^^" ; Formerly +15
	Text StartX+80-16,StartY+15,"vv"
	CenteredText(StartX+40,StartY+15,Str$(LevelHeight))

	Color TextLevelR,TextLevelG,TextLevelB
	Text SidebarX+150-7*4,SidebarY+290,"OBJECTS"
	StartX=SidebarX+10
	StartY=SidebarY+305
	Color ObjectColorR,ObjectColorG,ObjectColorB
	Rect StartX,StartY,185,150
	Color TextLevelR,TextLevelG,TextLevelB
	Text StartX+92-11*4,StartY,"ADJUSTMENTS"

	If NofSelectedObjects=1
		Text StartX+2,StartY,"#"+SelectedObjects(0)
	ElseIf NofSelectedObjects>1
		Text StartX+2,StartY,"x"+NofSelectedObjects
	EndIf

	For i=ObjectAdjusterStart+0 To ObjectAdjusterStart+8

		DisplayObjectAdjuster(i)

	Next

	; local rendering end

End Function

Function EditorLocalControls()

	If KeyPressed(64) ; F6 key
		; toggle orthographic projection
		If Camera1Proj=1
			; to orthographic
			Camera1Proj=2
			Camera1Zoom#=Camera1OrthographicZoom#
			Camera1PerspectiveY#=EntityY(Camera1) ; save to return to it later
			PositionEntity Camera1,EntityX(Camera1),Camera1StartY*2,EntityZ(Camera1)
		Else
			; to perspective
			Camera1Proj=1
			Camera1Zoom#=Camera1PerspectiveZoom#
			PositionEntity Camera1,EntityX(Camera1),Camera1PerspectiveY#,EntityZ(Camera1)
		EndIf
		UpdateCameraProj()
		CameraZoom Camera1,Camera1Zoom#
		Camera1SavedProjMode=Camera1Proj
	EndIf

	If KeyPressed(65) ; F7 key
		; toggle wireframe mode
		UsingWireFrame=Not UsingWireFrame
		WireFrame UsingWireFrame
	EndIf

	MX=MouseX()
	MY=MouseY()

	Fast=False
	If ShiftDown() Then Fast=True

	; *************************************
	; Placing Tiles and Objects on the Editor Field
	; *************************************

	If EditorMode=EditorModeTile Or EditorMode=EditorModeObject
		If mx>=0 And mx<LevelViewportWidth And my>=0 And my<LevelViewportHeight
			Entity=CameraPick(camera1,MouseX(),MouseY())
			If Entity>0

				SetBrushCursorPosition(Floor(PickedX()),Floor(-PickedZ()))

				; Hide all cursors except for cursor 0.
				For i=1 To 3
					HideEntity CursorMeshPillar(i)
					HideEntity CursorMeshOpaque(i)
				Next

				PositionCursorEntity(0,BrushCursorX,BrushCursorY)
				If BrushMode<>BrushModeSetMirror
					If DupeMode=DupeModeX
						TargetX=MirrorAcrossInt(BrushCursorX,MirrorPositionX)
						PositionCursorEntity(1,TargetX,BrushCursorY)
					EndIf
					If DupeMode=DupeModeY
						TargetY=MirrorAcrossInt(BrushCursorY,MirrorPositionY)
						PositionCursorEntity(1,BrushCursorX,TargetY)
					EndIf
					If DupeMode=DupeModeXPlusY
						TargetX=MirrorAcrossInt(BrushCursorX,MirrorPositionX)
						TargetY=MirrorAcrossInt(BrushCursorY,MirrorPositionY)
						PositionCursorEntity(1,TargetX,BrushCursorY)
						PositionCursorEntity(2,BrushCursorX,TargetY)
						PositionCursorEntity(3,TargetX,TargetY)
					EndIf
				EndIf

				ShowBrushSurface()

				BrushR=GetBrushModeColor(BrushMode,0)
				BrushG=GetBrushModeColor(BrushMode,1)
				BrushB=GetBrushModeColor(BrushMode,2)

				For i=0 To 3
					EntityColor CursorMeshPillar(i),BrushR,BrushG,BrushB
					EntityColor CursorMeshOpaque(i),BrushR,BrushG,BrushB
					EntityColor CursorMeshTexturePicker,BrushR,BrushG,BrushB
					EntityColor BrushMesh,BrushR,BrushG,BrushB
					EntityColor BrushTextureMesh,BrushR,BrushG,BrushB
				Next

				Color TextLevelR,TextLevelG,TextLevelB
				Text LevelViewportWidth/2-4.5*8,LevelViewportHeight,"X:"+Str$(BrushCursorX)+", Y:"+Str$(BrushCursorY)

				If LeftMouse=False
					If BlockPlacingMode=BlockPlacingModePlace ; Release left mouse button to place block.
						If EditorMode=0
							For i=cornleft To cornright
								For j=cornup To corndown
									ChangeLevelTile(i,j,True)
								Next
							Next
							TilesWereChanged()
							AddUnsavedChange()
						ElseIf EditorMode=3
							PrepareObjectSelection()
							For i=cornleft To cornright
								For j=cornup To corndown
									PlaceObject(i,j)
								Next
							Next
							ObjectsWereChanged()
							AddUnsavedChange()
						EndIf
						SetBrushMode(BrushModeBlock)
					EndIf

					OnceTilePlacement=True
					DidStepPerClick=False
				ElseIf DidStepPerClick=False And LeftMouseReleased=True
					DidStepPerClick=True
					If StepPer=StepPerClick
						RunStepSize()
					EndIf

					If BrushMode<>BrushModeBlock And BrushMode<>BrushModeTestLevel And BrushMode<>BrushModeSetMirror
						AddUnsavedChange()
					EndIf
				EndIf

				If LeftMouse=True And LeftMouseReleased=True And (EditorMode<>0 Or OnceTilePlacement=True)
					OnceTilePlacement=False

					If EditorMode=EditorModeTile And BrushMode<>BrushModeBlock ; Don't want to run a step size when just trying to select a block region.
						If StepPer=StepPerPlacement
							RunStepSize()
						EndIf
					EndIf

					If BrushMode=BrushModeBlock
						LeftMouseReleased=False
						StartBlockModeBlock(BlockPlacingModePlace)
					ElseIf BrushMode=BrushModeTestLevel
						If AskToSaveLevelAndExit()
							; Just in case the user is cheeky and decides to use Test Level At Brush in a brand new wlv.
							If Not LevelExists(CurrentLevelNumber)
								SaveLevel()
							EndIf

							StartTestModeAt(CurrentLevelNumber,BrushCursorX,BrushCursorY)
						EndIf
					ElseIf BrushMode=BrushModeSetMirror
						SetBrushMode(BrushModeNormal)
					Else
						PrepareObjectSelection()
						For i=0 To FloodedElementCount-1
							thisx=FloodedStackX(i)
							thisy=FloodedStackY(i)
							PlaceObjectOrChangeLevelTile(thisx,thisy)
						Next

						If EditorMode=EditorModeObject
							ObjectsWereChanged()
						ElseIf EditorMode=EditorModeTile
							TilesWereChanged()
						EndIf
					EndIf

					If EditorMode=EditorModeObject
						LeftMouseReleased=False
					EndIf

					If EditorMode=EditorModeTile
						BrushCursorProbablyModifiedTiles()
					EndIf
				EndIf
				If RightMouse=True
					If RightMouseReleased=True
						RightMouseReleased=False

						; grab object or tile

						If EditorMode=0
							GrabLevelTile(BrushCursorX,BrushCursorY)

							CopyLevel()

							DraggedTilesSpotX=BrushCursorX
							DraggedTilesSpotY=BrushCursorY

							For i=0 To MaxLevelCoordinate
								For j=0 To MaxLevelCoordinate
									DraggedTilesEnabled(i,j)=False
								Next
							Next

							ClearObjectDrag()
							For i=0 To FloodedElementCount-1
								thisx=FloodedStackX(i)
								thisy=FloodedStackY(i)

								If IsPositionInLevelArrayBounds(thisx,thisy)
									DraggedTilesEnabled(thisx,thisy)=True
									CopyTile(LevelTiles(thisx,thisy),DraggedTiles(thisx,thisy))
									CopyTile(CurrentTile,CopyLevelTiles(thisx,thisy))

									If LevelTileObjectCount(thisx,thisy)<>0
										For j=0 To NofObjects-1
											If ObjectIsAtFloat(LevelObjects(j),thisx,thisy)
												AddDragObject(j)
											EndIf
										Next
									EndIf
								EndIf
							Next
							StartObjectDrag()

							TileDragging=True
						ElseIf EditorMode=3
							If BrushMode=BrushModeBlock
								PrepareObjectSelection()
								StartBlockModeBlock(BlockPlacingModeCopy)
							ElseIf BrushMode<>BrushModeBlockPlacing
								PrepareObjectSelection()
								If KeyDown(41) ; tilde key
									If LevelTileObjectCount(BrushCursorX,BrushCursorY)<>0
										GrabObject(BrushCursorX,BrushCursorY,False)
										TargetObject.GameObject=LevelObjects(SelectedObjects(NofSelectedObjects-1))
										TargetAttributes.GameObjectAttributes=TargetObject\Attributes
										For i=0 To NofObjects-1
											Obj.GameObject=LevelObjects(i)
											Attributes.GameObjectAttributes=Obj\Attributes
											If Attributes\LogicType=TargetAttributes\LogicType And Attributes\ModelName=TargetAttributes\ModelName
												AddOrToggleSelectObject(i)
											EndIf
										Next
									EndIf
								Else
									If BrushMode=BrushModeNormal And (Not ShiftDown())
										GrabObject(BrushCursorX,BrushCursorY,False)
									Else
										For i=0 To FloodedElementCount-1
											thisx=FloodedStackX(i)
											thisy=FloodedStackY(i)
											GrabObject(thisx,thisy,True)
										Next
									EndIf
								EndIf
								FinishObjectSelection()
							EndIf
						EndIf
					EndIf
				Else
					If BlockPlacingMode=BlockPlacingModeCopy
						For i=0 To FloodedElementCount-1
							thisx=FloodedStackX(i)
							thisy=FloodedStackY(i)
							GrabObject(thisx,thisy,True)
						Next
						FinishObjectSelection()
						SetBrushMode(BrushModeBlock)
					Else
						If DragChange
							DragChange=False
							AddUnsavedChange()
						EndIf
					EndIf

					TileDragging=False
					NofDraggedObjects=0
				EndIf

				If MouseDown(3) ; middle click / ; middle mouse
					SetCurrentObjectTargetLocation(BrushCursorX,BrushCursorY)
					SetEditorMode(3)
				EndIf
			Else
				BrushCursorOffMap()
			EndIf

			If DeleteKey=True
				If DeleteKeyReleased=True
					DeleteKeyReleased=False
					If BrushMode=BrushModeBlock
						StartBlockModeBlock(BlockPlacingModeDelete)
					ElseIf BrushMode<>BrushModeBlockPlacing
						For i=0 To FloodedElementCount-1
							thisx=FloodedStackX(i)
							thisy=FloodedStackY(i)
							DeleteObjectAt(thisx,thisy)
						Next

						ObjectsWereChanged()
						AddUnsavedChange()
					EndIf

					RecalculateDragSize()
				EndIf
			Else
				If BlockPlacingMode=BlockPlacingModeDelete
					For i=cornleft To cornright
						For j=cornup To corndown
							DeleteObjectAt(i,j)
						Next
					Next
					SetBrushMode(BrushModeBlock)

					ObjectsWereChanged()
					AddUnsavedChange()
				EndIf
			EndIf

			If ReturnKey=True And ReturnKeyReleased=True
				ReturnKeyReleased=False

				; set custom brush
				BrushXStart=GetBrushXStart()
				BrushYStart=GetBrushYStart()
				BrushXEnd=GetBrushXEnd(BrushXStart)
				BrushYEnd=GetBrushYEnd(BrushYStart)
				BrushSpaceOriginX=BrushCursorX
				BrushSpaceOriginY=BrushCursorY
				BrushSpaceWidth=BrushWidth
				BrushSpaceHeight=BrushHeight
				If EditorMode=0
					For i=BrushXStart To BrushXEnd
						For j=BrushYStart To BrushYEnd
							BrushSpaceX=LevelSpaceToBrushSpaceX(i,BrushWrapModulus)
							BrushSpaceY=LevelSpaceToBrushSpaceY(j,BrushWrapModulus)
							CopyLevelTileToBrush(i,j,BrushSpaceX,BrushSpaceY)
						Next
					Next
					GenerateBrushPreview()
				ElseIf EditorMode=3
					NofBrushObjects=0

					For k=0 To NofObjects-1
						ObjTileX=LevelObjects(k)\Position\TileX
						ObjTileY=LevelObjects(k)\Position\TileY
						If ObjTileX>=BrushXStart And ObjTileX<BrushXStart+BrushWidth And ObjTileY>=BrushYStart And ObjTileY<BrushYStart+BrushHeight
							BrushSpaceX=LevelSpaceToBrushSpaceX(ObjTileX,BrushWrapModulus)
							BrushSpaceY=LevelSpaceToBrushSpaceY(ObjTileY,BrushWrapModulus)
							CopyObjectToBrush(LevelObjects(k),NofBrushObjects,BrushSpaceX,BrushSpaceY)
							NofBrushObjects=NofBrushObjects+1
						EndIf
					Next

					If NofBrushObjects=0
						SetBrushToCurrentObject()
					Else
						GenerateBrushPreview()
						;ShowMessage(NofBrushObjects+" objects found in brush.",1000)
					EndIf
				EndIf
			EndIf

		Else
			BrushCursorOffMap()
		EndIf
	EndIf

	; *************************************
	; Selecting A Texture / Picking a Texture / Texture Picker / Tile Picker
	; *************************************
	If EditorMode=1 Or EditorMode=2
		If mx>=0 And mx<LevelViewportWidth And my>=0 And my<LevelViewportHeight
			nmx=LevelViewportX(mx)
			nmy=my
			DivisorNumerator=LevelViewportHeight
			DivisorX#=Float#(DivisorNumerator)/8.0
			DivisorY#=Float#(DivisorNumerator)/8.0
			StepSize#=1.0/8.0
			ScaleEntity CursorMeshPillar(0),0.0325,0.01,0.0325
			ScaleEntity CursorMeshOpaque(0),0.0325,0.01,0.0325
			ScaleEntity CursorMeshTexturePicker,0.0325,0.01,0.0325
			tilepickx=Floor(nmx/DivisorX#)
			tilepicky=Floor(nmy/DivisorY#)
			tilepickx=ClampInt(0,7,tilepickx)
			tilepicky=ClampInt(0,7,tilepicky)
			PositionEntity CursorMeshPillar(0),tilepickx*StepSize#+StepSize#/2.0,200,-tilepicky*StepSize#-StepSize#/2.0,200
			PositionEntity CursorMeshOpaque(0),tilepickx*StepSize#+StepSize#/2.0,200,-tilepicky*StepSize#-StepSize#/2.0,200
			PositionEntity CursorMeshTexturePicker,tilepickx*StepSize#+StepSize#/2.0,200,-tilepicky*StepSize#-StepSize#/2.0,200
			ShowEntity CursorMeshPillar(0)
			ShowEntity CursorMeshOpaque(0)
			ShowEntity CursorMeshTexturePicker
			If LeftMouse=True
				If editormode=1
					; main texture
					CurrentTile\Terrain\Texture=tilepickx+tilepicky*8
				Else If editormode=2
					; main texture
					CurrentTile\Terrain\SideTexture=tilepickx+tilepicky*8
				EndIf
				SetEditorMode(0)
				LeftMouseReleased=False
				BuildCurrentTileModel()
				SetBrushToCurrentTile()
				ScaleEntity CursorMeshPillar(0),1,1,1
			EndIf
		Else
			HideCursors()
		EndIf
	EndIf

	; *************************************
	; Change the CurrentTile
	; *************************************

	StartX=SidebarX+10
	StartY=SidebarY+20

	DelayTime=10

	If MX>=StartX And MX<StartX+200 And MY<StartY+220
		If LeftMouse=True Or RightMouse=True Or MouseScroll<>0 Or ReturnKey=True
			SetEditorMode(0)
		EndIf
		RotationSpeed=4
		If KeyDown(23) ; I, formerly A (30)
			TurnEntity CurrentMesh,0,RotationSpeed,0
		EndIf
		If KeyDown(24) ; O, formerly D (32)
			TurnEntity CurrentMesh,0,-RotationSpeed,0
		EndIf
	EndIf

	If MX>=StartX And MX<StartX+100 And MY>=StartY+35 And MY<StartY+100
		If RightMouse=True And RightMouseReleased=True
			; CurrentTile\Terrain\Rotation
			RightMouseReleased=False
			CurrentTile\Terrain\Rotation=(CurrentTile\Terrain\Rotation+1) Mod 8
			BuildCurrentTileModel()
			SetBrushToCurrentTile()
			SetEditorMode(0)
		EndIf
		If LeftMouse=True And LeftMouseReleased=True
			; Texture
			If CtrlDown()
				CurrentTile\Terrain\Texture=InputInt("Enter Texture ID: ")
				BuildCurrentTileModel()
				SetBrushToCurrentTile()
			Else
				Camera1To3Proj()
				SetEditorMode(1)
			EndIf
		ElseIf MouseScroll<>0
			CurrentTile\Terrain\Texture=CurrentTile\Terrain\Texture+MouseScroll
			BuildCurrentTileModel()
			SetBrushToCurrentTile()
		EndIf
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If MaybeUnuseAllTileAttributes(CurrentTileTextureUse)
				CurrentTileTextureUse=1-CurrentTileTextureUse
			EndIf
			SetEditorMode(0)
		EndIf
		ShowTooltipRightAligned(StartX,StartY+95,"Texture ID: "+CurrentTile\Terrain\Texture)
	EndIf

	; CurrentTile\Terrain\SideRotation/Texture
	If MX>=StartX And MX<StartX+100 And MY>=StartY+100 And MY<StartY+155
		If RightMouse=True And RightMouseReleased=True
			; SideRotation
			RightMouseReleased=False
			CurrentTile\Terrain\SideRotation=(CurrentTile\Terrain\SideRotation+1) Mod 8
			BuildCurrentTileModel()
			SetBrushToCurrentTile()
			SetEditorMode(0)
		EndIf
		If LeftMouse=True And LeftMouseReleased=True
			; SideTexture
			If CtrlDown()
				CurrentTile\Terrain\SideTexture=InputInt("Enter SideTexture ID: ")
				BuildCurrentTileModel()
				SetBrushToCurrentTile()
			Else
				Camera1To3Proj()
				SetEditorMode(2)
			EndIf
		ElseIf MouseScroll<>0
			CurrentTile\Terrain\SideTexture=CurrentTile\Terrain\SideTexture+MouseScroll
			BuildCurrentTileModel()
			SetBrushToCurrentTile()
		EndIf
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If MaybeUnuseAllTileAttributes(CurrentTileSideTextureUse)
				CurrentTileSideTextureUse=1-CurrentTileSideTextureUse
			EndIf
			SetEditorMode(0)
		EndIf
		ShowTooltipRightAligned(StartX,StartY+140,"SideTexture ID: "+CurrentTile\Terrain\SideTexture)
	EndIf

	; WaterTexture/Rotation
	If MX>=StartX+100 And MX<StartX+200 And MY>=StartY+40 And MY<StartY+115
		If RightMouse=True And RightMouseReleased=True
			CurrentTile\Water\Rotation=(CurrentTile\Water\Rotation+1) Mod 8
			RightMouseReleased=False
			BuildCurrentTileModel()
			SetBrushToCurrentTile()
			SetEditorMode(0)
		EndIf
		If LeftMouse=True And LeftMouseReleased=True
			CurrentTile\Water\Texture=(CurrentTile\Water\Texture+1) Mod 8
			LeftMouseReleased=False
			BuildCurrentTileModel()
			SetBrushToCurrentTile()
			SetEditorMode(0)
		EndIf
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If MaybeUnuseAllTileAttributes(CurrentWaterTileTextureUse)
				CurrentWaterTileTextureUse=1-CurrentWaterTileTextureUse
			EndIf
			SetEditorMode(0)
		EndIf

	EndIf

	; CurrentTile\Terrain\Extrusion
	If MX>=StartX And MX<StartX+100 And MY>=StartY And MY<StartY+15
		CurrentTile\Terrain\Extrusion#=AdjustFloat#("Enter Xtrude: ", CurrentTile\Terrain\Extrusion#, 0.1, 1.0, DelayTime)

		If WasAdjusted()
			SetBrushToCurrentTile()
		EndIf

		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If CtrlDown()
				If StepSizeTileExtrusion#=0.0
					StepSizeTileExtrusion#=InputFloat#("Enter step size for Xtrude: ")
				Else
					StepSizeTileExtrusion#=0.0
				EndIf
			Else
				If MaybeUnuseAllTileAttributes(CurrentTileExtrusionUse)
					CurrentTileExtrusionUse=1-CurrentTileExtrusionUse
				EndIf
			EndIf
			SetEditorMode(0)
		EndIf
 	EndIf
	; CurrentTile\Terrain\Height
	If MX>=StartX+100 And MX<StartX+200 And MY>=StartY And MY<StartY+15
		CurrentTile\Terrain\Height#=AdjustFloat#("Enter Height: ", CurrentTile\Terrain\Height#, 0.1, 1.0, DelayTime)

		If WasAdjusted()
			SetBrushToCurrentTile()
		EndIf

		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If CtrlDown()
				If StepSizeTileHeight#=0.0
					StepSizeTileHeight#=InputFloat#("Enter step size for Height: ")
				Else
					StepSizeTileHeight#=0.0
				EndIf
				SetBrushToCurrentTile()
			Else
				If MaybeUnuseAllTileAttributes(CurrentTileHeightUse)
					CurrentTileHeightUse=1-CurrentTileHeightUse
				EndIf
			EndIf
			SetEditorMode(0)
		EndIf
 	EndIf
	; CurrentTile\Terrain\Logic
	If MX>=StartX And MX<StartX+200 And MY>=StartY+15 And MY<StartY+30
		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			If CtrlDown()
				CurrentTile\Terrain\Logic=InputInt("Enter Logic: ")
				ReturnKey=False
				ReturnKeyReleased=False
			Else
				Select CurrentTile\Terrain\Logic
					Case 0
						CurrentTile\Terrain\Logic=1
					Case 1
						CurrentTile\Terrain\Logic=2
					Case 2
						CurrentTile\Terrain\Logic=5
					Case 5
						CurrentTile\Terrain\Logic=11
					Case 11
						CurrentTile\Terrain\Logic=12
					Case 12
						CurrentTile\Terrain\Logic=13
					Default
						CurrentTile\Terrain\Logic=0
				End Select
			EndIf
			SetBrushToCurrentTile()

			LeftMouseReleased=False
			RightMouseReleased=False
			SetEditorMode(0)
		EndIf
		If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			Select CurrentTile\Terrain\Logic
				Case 2
					CurrentTile\Terrain\Logic=1
				Case 5
					CurrentTile\Terrain\Logic=2
				Case 11
					CurrentTile\Terrain\Logic=5
				Case 12
					CurrentTile\Terrain\Logic=11
				Case 13
					CurrentTile\Terrain\Logic=12
				Case 0
					CurrentTile\Terrain\Logic=13
				Default
					CurrentTile\Terrain\Logic=0
			End Select
			SetBrushToCurrentTile()

			LeftMouseReleased=False
			RightMouseReleased=False
			SetEditorMode(0)
		EndIf
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If MaybeUnuseAllTileAttributes(CurrentTileLogicUse)
				CurrentTileLogicUse=1-CurrentTileLogicUse
			EndIf
			SetEditorMode(0)
		EndIf

 	EndIf

	; CurrentTile\Terrain\Random
	If MX>=StartX And MX<StartX+200 And MY>=StartY+170 And MY<StartY+185
		CurrentTile\Terrain\Random#=AdjustFloat#("Enter Random: ", CurrentTile\Terrain\Random#, 0.01, 0.1, DelayTime)

		If WasAdjusted()
			SetBrushToCurrentTile()
		EndIf

		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If CtrlDown()
				If StepSizeTileRandom#=0.0
					StepSizeTileRandom#=InputFloat#("Enter step size for Random: ")
				Else
					StepSizeTileRandom#=0.0
				EndIf
			Else
				If MaybeUnuseAllTileAttributes(CurrentTileRandomUse)
					CurrentTileRandomUse=1-CurrentTileRandomUse
				EndIf
			EndIf
			SetEditorMode(0)
		EndIf

		SetBrushToCurrentTile()
 	EndIf
	; CurrentTile\Terrain\Rounding
	If MX>=StartX And MX<StartX+100 And MY>=StartY+185 And MY<StartY+200
		If (LeftMouse=True And LeftMouseReleased=True) Or (RightMouse=True And RightMouseReleased=True) Or MouseScroll<>0
			LeftMouseReleased=False
			RightMouseReleased=False
			CurrentTile\Terrain\Rounding=1-CurrentTile\Terrain\Rounding
			SetEditorMode(0)
			SetBrushToCurrentTile()
		EndIf
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If MaybeUnuseAllTileAttributes(CurrentTileRoundingUse)
				CurrentTileRoundingUse=1-CurrentTileRoundingUse
			EndIf
			SetEditorMode(0)
		EndIf
	EndIf
	; CurrentTile\Terrain\EdgeRandom
	If MX>=StartX+100 And MX<StartX+200 And MY>=StartY+185 And MY<StartY+200
		If (LeftMouse=True And LeftMouseReleased=True) Or (RightMouse=True And RightMouseReleased=True) Or MouseScroll<>0
			LeftMouseReleased=False
			RightMouseReleased=False
			CurrentTile\Terrain\EdgeRandom=1-CurrentTile\Terrain\EdgeRandom
			SetEditorMode(0)
			SetBrushToCurrentTile()
		EndIf
		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If MaybeUnuseAllTileAttributes(CurrentTileEdgeRandomUse)
				CurrentTileEdgeRandomUse=1-CurrentTileEdgeRandomUse
			EndIf
			SetEditorMode(0)
		EndIf
	EndIf

	; CurrentTile\Water\Height
	If MX>=StartX And MX<StartX+100 And MY>=StartY+200 And MY<StartY+215
		CurrentTile\Water\Height#=AdjustFloat#("Enter WHeight: ", CurrentTile\Water\Height#, 0.1, 1.0, DelayTime)

		If WasAdjusted()
			SetBrushToCurrentTile()
		EndIf

		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If CtrlDown()
				If StepSizeWaterTileHeight#=0.0
					StepSizeWaterTileHeight#=InputFloat#("Enter step size for WHeight: ")
				Else
					StepSizeWaterTileHeight#=0.0
				EndIf
			Else
				If MaybeUnuseAllTileAttributes(CurrentWaterTileHeightUse)
					CurrentWaterTileHeightUse=1-CurrentWaterTileHeightUse
				EndIf
			EndIf
			SetEditorMode(0)
		EndIf

		SetBrushToCurrentTile()
 	EndIf
	; CurrentTile\Water\Turbulence
	If MX>=StartX+100 And MX<StartX+200 And MY>=StartY+200 And MY<StartY+215
		CurrentTile\Water\Turbulence#=AdjustFloat#("Enter WTurb: ", CurrentTile\Water\Turbulence#, 0.1, 1.0, DelayTime)

		If WasAdjusted()
			SetBrushToCurrentTile()
		EndIf

		If ReturnKey=True And ReturnKeyReleased=True
			ReturnKeyReleased=False
			If CtrlDown()
				If StepSizeWaterTileTurbulence#=0.0
					StepSizeWaterTileTurbulence#=InputFloat#("Enter step size for WTurb: ")
				Else
					StepSizeWaterTileTurbulence#=0.0
				EndIf
			Else
				If MaybeUnuseAllTileAttributes(CurrentWaterTileTurbulenceUse)
					CurrentWaterTileTurbulenceUse=1-CurrentWaterTileTurbulenceUse
				EndIf
			EndIf
			SetEditorMode(0)
		EndIf

 	EndIf

	; *************************************
	; Textures and global settings
	; *************************************

	If mx>=SidebarX+215

		If my>=100 And my<115 And ((leftmouse=True And leftmousereleased=True) Or MouseScroll>0)
			leftmousereleased=False
			LevelWeather=LevelWeather+1
			If levelweather=18 Then levelweather=0
			LightingWasChanged() ; Necessary for if alarm weather was being used.
			AddUnsavedChange()
		EndIf
		If my>=100 And my<115 And ((rightmouse=True And rightmousereleased=True) Or MouseScroll<0)
			rightmousereleased=False
			LevelWeather=LevelWeather-1
			If levelweather=-1 Then levelweather=17
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
		OldValue=LevelMusic
		If my>=115 And my<130 And ((leftmouse=True And leftmousereleased=True) Or MouseScroll>0)
			leftmousereleased=False
			If CtrlDown()
				LevelMusic=InputInt("Enter music ID: ")
			Else
				If Fast
					Adj=10
				Else
					Adj=1
				EndIf
				levelmusic=levelmusic+Adj
				If levelmusic=22 Then levelmusic=-1
			EndIf
			UpdateMusic()
		EndIf
		If my>=115 And my<130 And ((rightmouse=True And rightmousereleased=True) Or MouseScroll<0)
			rightmousereleased=False
			If Fast
				Adj=10
			Else
				Adj=1
			EndIf
			levelmusic=levelmusic-Adj
			If levelmusic=-2 Then levelmusic=20
			UpdateMusic()
		EndIf
		If LevelMusic<>OldValue
			AddUnsavedChange()
		EndIf

		; level/water textures

		; LevelTexture
		If my>133 And my<148
			If CtrlDown() And leftmouse=True And leftmousereleased=True
				FlushKeys
				Locate 0,0
				Color 0,0,0
				Rect 0,0,500,40,True
				Color TextLevelR,TextLevelG,TextLevelB
				LevelTextureCustomName$=Input$( "Custom Texture Name (e.g. 'customtemplate'):")

				If FileType (globaldirname$+"\custom\leveltextures\leveltex "+leveltexturecustomname$+".bmp")<>1 And FileType (globaldirname$+"\custom content\Model\Textures\backgroundtex "+leveltexturecustomname$+"1.bmp")<>1 And FileType (globaldirname$+"\custom content\Model\Textures\backgroundtex "+leveltexturecustomname$+"2.bmp")<>1
					Locate 0,0
					Color 0,0,0
					Rect 0,0,500,40,True
					Color 255,255,0
					Print "FILE(S) NOT FOUND!"
					Delay 2000

				Else
					FreeTexture LevelTexture
					LevelTexture=0
					LevelTexture=myLoadTexture(globaldirname$+"\custom\leveltextures\leveltex "+leveltexturecustomname$+".bmp",1)
					If LevelTexture=0
						Locate 0,0
						Color 0,0,0
						Rect 0,0,500,40,True
						Color 255,255,0
						Print "TEXTURE COULDN'T LOAD!"
						UpdateLevelTextureDefault()

						Delay 2000
					Else
						currentleveltexture=-1
						UpdateLevelTexture()
					EndIf
				EndIf

				leftmousereleased=False
				buildcurrenttilemodel()
				AddUnsavedChange()
			Else
				If (leftmouse=True And leftmousereleased=True) Or MouseScroll>0
					CurrentLevelTexture=CurrentLevelTexture+1
					If CurrentLevelTexture=NofLevelTextures Then currentleveltexture=0

					FreeTexture LevelTexture
					UpdateLevelTextureDefault()

					For j=0 To LevelHeight-1
						EntityTexture LevelMesh(j),LevelTexture
					Next

					leftmousereleased=False
					buildcurrenttilemodel()
					AddUnsavedChange()
				ElseIf (rightmouse=True And rightmousereleased=True) Or MouseScroll<0
					CurrentLevelTexture=CurrentLevelTexture-1
					If CurrentLevelTexture<0 Then currentleveltexture=NofLevelTextures-1

					FreeTexture LevelTexture
					UpdateLevelTextureDefault()

					For j=0 To LevelHeight-1
						EntityTexture LevelMesh(j),LevelTexture
					Next

					rightmousereleased=False
					buildcurrenttilemodel()
					AddUnsavedChange()
				EndIf
			EndIf

			ShowTooltipRightAligned(SidebarX+210,163,CurrentLevelTextureName$())
		EndIf

		; WaterTexture
		If my>150 And my<163
			If CtrlDown() And leftmouse=True And leftmousereleased=True
				FlushKeys
				Locate 0,0
				Color 0,0,0
				Rect 0,0,500,40,True
				Color TextLevelR,TextLevelG,TextLevelB
				WaterTextureCustomName$=Input$( "Custom WaterTexture Name (e.g. 'customtemplate'):")

				If FileType (globaldirname$+"\custom\leveltextures\watertex "+watertexturecustomname$+".jpg")<>1
					Locate 0,0
					Color 0,0,0
					Rect 0,0,500,40,True
					Color 255,255,0
					Print "FILE NOT FOUND!"
					Delay 2000

				Else
					FreeTexture WaterTexture
					WaterTexture=0
					WaterTexture=myLoadTexture(globaldirname$+"\custom\leveltextures\watertex "+watertexturecustomname$+".jpg",1)
					If WaterTexture=0
						Locate 0,0
						Color 0,0,0
						Rect 0,0,500,40,True
						Color 255,255,0
						Print "TEXTURE COULDN'T LOAD!"
						UpdateWaterTextureDefault()

						Delay 2000
					Else
						currentwatertexture=-1
						UpdateWaterTexture()
					EndIf
				EndIf

				leftmousereleased=False
				buildcurrenttilemodel()
				AddUnsavedChange()
			Else
				If (leftmouse=True And leftmousereleased=True) Or MouseScroll>0
					CurrentWaterTexture=CurrentWaterTexture+1

					If CurrentWaterTexture=NofWaterTextures Then currentWatertexture=0

					FreeTexture WaterTexture
					UpdateWaterTextureDefault()

					For j=0 To LevelHeight-1
						EntityTexture WaterMesh(j),WaterTexture
					Next

					leftmousereleased=False
					buildcurrenttilemodel()
					AddUnsavedChange()
				ElseIf (rightmouse=True And rightmousereleased=True) Or MouseScroll<0
					CurrentWaterTexture=CurrentWaterTexture-1

					If CurrentWaterTexture<0 Then currentWatertexture=NofWaterTextures-1

					FreeTexture WaterTexture
					UpdateWaterTextureDefault()

					For j=0 To LevelHeight-1
						EntityTexture WaterMesh(j),WaterTexture
					Next

					rightmousereleased=False
					buildcurrenttilemodel()
					AddUnsavedChange()
				EndIf
			EndIf

			ShowTooltipRightAligned(SidebarX+210,180,CurrentWaterTextureName$())
		EndIf

		If my>165 And my<195 ;my<185
			If mx>FlStartX+8 And mx<FlStartX+24
				OldValue=WaterFlow
				Waterflow=AdjustInt("Enter Waterflow: ", Waterflow, 1, 10, DelayTime)
				ShowTooltipCenterAligned(FlStartX+16,FlStartY+8,"Water Flow Speed: "+Waterflow)
				If OldValue<>WaterFlow
					AddUnsavedChange()
				EndIf
			EndIf
			If mx>FlStartX+32 And mx<FlStartX+48
				If (leftmouse=True And leftmousereleased=True) Or (rightmouse=True And rightmousereleased=True) Or MouseScroll<>0
					WaterTransparent=1-WaterTransparent
					UpdateAllWaterMeshTransparent()
					leftmousereleased=False
					rightmousereleased=False
					AddUnsavedChange()
				EndIf
				ShowTooltipCenterAligned(FlStartX+40,FlStartY+8,"Water is Transparent: "+TrueToYes$(WaterTransparent))
			EndIf
			If mx>FlStartX+56 And mx<FlStartX+72
				If (leftmouse=True And leftmousereleased=True) Or (rightmouse=True And rightmousereleased=True) Or MouseScroll<>0
					WaterGlow=1-WaterGlow
					UpdateAllWaterMeshGlow()
					leftmousereleased=False
					rightmousereleased=False
					AddUnsavedChange()
				EndIf
				ShowTooltipCenterAligned(FlStartX+64,FlStartY+8,"Water Glows: "+TrueToYes$(WaterGlow))
			EndIf
			If mx>FlStartX+80 And mx<FlStartX+100
				OldValue=LevelEdgeStyle
				LevelEdgeStyle=AdjustInt("Enter LevelEdgeStyle: ", LevelEdgeStyle, 1, 10, DelayTime)
				If LevelEdgeStyle<1
					LevelEdgeStyle=4
				ElseIf LevelEdgeStyle>4
					LevelEdgeStyle=1
				EndIf
				ShowTooltipCenterAligned(FlStartX+90,FlStartY+8,"Level Edge Style: "+GetLevelEdgeStyleName$(LevelEdgeStyle))
				If OldValue<>LevelEdgeStyle
					AddUnsavedChange()
				EndIf
			EndIf
		EndIf
	EndIf

	If Fast
		ChangeSpeed=10
	Else
		ChangeSpeed=1
	EndIf

	StartX=SidebarX+212

	If mx>StartX And my>215-13 And my<228-13
		If leftmouse=True And CtrlDown()
			TheString$=InputString$("Enter color for all light values (or leave blank to cancel): ")
			If TheString$<>""
				LightValue=TheString$
				LightRed=LightValue
				LightGreen=LightValue
				LightBlue=LightValue
				AmbientRed=LightValue
				AmbientGreen=LightValue
				AmbientBlue=LightValue
				LightingWasChanged()
				AddUnsavedChange()
			EndIf
		EndIf
	EndIf
	If mx>StartX And my>215 And mx<StartX+24 And my<228
		If leftmouse=True Or MouseScroll>0
			If CtrlDown()
				LightRed=InputInt("Enter LightRed: ")
			Else
				LightRed=LightRed+ChangeSpeed
				;If lightred>=256 Then lightred=lightred-256
			EndIf
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
		If rightmouse=True Or MouseScroll<0
			LightRed=LightRed-ChangeSpeed
			;If lightred=-1 Then lightred=255
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
	EndIf
	If mx>StartX+29 And my>215 And mx<StartX+24+29 And my<228
		If leftmouse=True Or MouseScroll>0
			If CtrlDown()
				LightGreen=InputInt("Enter LightGreen: ")
			Else
				LightGreen=LightGreen+ChangeSpeed
				;If LightGreen=256 Then LightGreen=0
			EndIf
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
		If rightmouse=True Or MouseScroll<0
			LightGreen=LightGreen-ChangeSpeed
			;If LightGreen=-1 Then LightGreen=255
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
	EndIf
	If mx>StartX+29+29 And my>215 And mx<StartX+24+29+29 And my<228
		If leftmouse=True  Or MouseScroll>0
			If CtrlDown()
				LightBlue=InputInt("Enter LightBlue: ")
			Else
				LightBlue=LightBlue+ChangeSpeed
				;If LightBlue=256 Then LightBlue=0
			EndIf
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
		If rightmouse=True Or MouseScroll<0
			LightBlue=LightBlue-ChangeSpeed
			;If LightBlue=-1 Then LightBlue=255
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
	EndIf

	If mx>StartX And my>215+13 And mx<StartX+24 And my<228+13
		If leftmouse=True Or MouseScroll>0
			If CtrlDown()
				AmbientRed=InputInt("Enter AmbientRed: ")
			Else
				AmbientRed=AmbientRed+ChangeSpeed
				;If Ambientred=256 Then ambientred=0
			EndIf
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
		If rightmouse=True Or MouseScroll<0
			AmbientRed=AmbientRed-ChangeSpeed
			;If Ambientred=-1 Then ambientred=255
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
	EndIf
	If mx>StartX+29 And my>215+13 And mx<StartX+24+29 And my<228+13
		If leftmouse=True Or MouseScroll>0
			If CtrlDown()
				AmbientGreen=InputInt("Enter AmbientGreen: ")
			Else
				AmbientGreen=AmbientGreen+ChangeSpeed
				;If AmbientGreen=256 Then AmbientGreen=0
			EndIf
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
		If rightmouse=True Or MouseScroll<0
			AmbientGreen=AmbientGreen-ChangeSpeed
			;If AmbientGreen=-1 Then AmbientGreen=255
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
	EndIf
	If mx>StartX+29+29 And my>215+13 And mx<StartX+24+29+29 And my<228+13
		If leftmouse=True Or MouseScroll>0
			If CtrlDown()
				AmbientBlue=InputInt("Enter AmbientBlue: ")
			Else
				AmbientBlue=AmbientBlue+ChangeSpeed
				;If AmbientBlue=256 Then AmbientBlue=0
			EndIf
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
		If rightmouse=True Or MouseScroll<0
			AmbientBlue=AmbientBlue-ChangeSpeed
			;If AmbientBlue=-1 Then AmbientBlue=255
			LightingWasChanged()
			AddUnsavedChange()
		EndIf
	EndIf

	; *************************************
	; Preset Tiles
	; *************************************

	StartX=SidebarX+10
	StartY=SidebarY+245

	If mx>=startx And mx<startx+285 And my>=StartY+0 And my<StartY+20
		If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			CurrentTilePresetCategory=CurrentTilePresetCategory-1
			If CurrentTilePresetCategory=-1 Then CurrentTilePresetCategory=NofTilePresetCategories-1
			RightMouseReleased=False
			CurrentTilePresetTile=0
			i=CurrentTilePresetCategory
			Repeat
				NofTilePresetTiles=0
				Dir=ReadDir("Data\Editor\TilePresets\"+TilePresetCategoryName$(i))
				file$=NextFile$(Dir)
				While file$<>""
					If file$<>"." And file$<>".." And FileType("Data\Editor\TilePresets\"+TilePresetCategoryName$(i)+"\"+file$)=1 And Lower$(Right$(file$,4))=".tp1"
						TilePresetTileName$(NofTilePresetTiles)=file$
						NofTilePresetTiles=NofTilePresetTiles+1
					EndIf
					file$=NextFile$(Dir)
				Wend
				CloseDir dir
				CurrentTilePresetCategory=i
				i=i-1
				If i=-1 Then i=NofTilePresetCategories-1

			Until NofTilePresetTiles>0
			LoadTilePreset()
			SetEditorMode(0)

		EndIf
	EndIf
	If mx>=startx And mx<startx+285 And my>=StartY+0 And my<StartY+20
		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			CurrentTilePresetCategory=CurrentTilePresetCategory+1
			If CurrentTilePresetCategory=NofTilePresetCategories Then CurrentTilePresetCategory=0
			LeftMouseReleased=False
			CurrentTilePresetTile=0
			i=CurrentTilePresetCategory
			Repeat
				NofTilePresetTiles=0
				Dir=ReadDir("Data\Editor\TilePresets\"+TilePresetCategoryName$(i))
				file$=NextFile$(Dir)
				While file$<>""
					If file$<>"." And file$<>".." And FileType("Data\Editor\TilePresets\"+TilePresetCategoryName$(i)+"\"+file$)=1 And Lower$(Right$(file$,4))=".tp1"
						TilePresetTileName$(NofTilePresetTiles)=file$
						NofTilePresetTiles=NofTilePresetTiles+1
					EndIf
					file$=NextFile$(Dir)
				Wend
				CloseDir dir
				CurrentTilePresetCategory=i
				i=i+1
				If i=NofTilePresetCategories Then i=0

			Until NofTilePresetTiles>0
			LoadTilePreset()
			SetEditorMode(0)

		EndIf
	EndIf

	If mx>=startx And mx<startx+285 And my>=StartY+20 And my<StartY+40
		If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			CurrentTilePresetTile=CurrentTilePresetTile-1
			If CurrentTilePresetTile=-1 Then CurrentTilePresetTile=NofTilePresetTiles-1
			RightMouseReleased=False
			LoadTilePreset()
			SetEditorMode(0)

		EndIf
	EndIf
	If mx>=startx And mx<startx+285 And my>=StartY+20 And my<StartY+40
		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			CurrentTilePresetTile=CurrentTilePresetTile+1
			If CurrentTilePresetTile=NofTilePresetTiles Then CurrentTilePresetTile=0
			LeftMouseReleased=False
			LoadTilePreset()
			SetEditorMode(0)

		EndIf
	EndIf

	; *************************************
	; LevelSize
	; *************************************

	StartX=SidebarX+215 ;715
	StartY=SidebarY+20

	If ShiftDown()
		Adj=10
	Else
		Adj=1
	EndIf

	If mx>=StartX And mx<StartX+80 And my>=StartY+15 And my<80

		If MouseScroll<>0
			BorderExpandOption=Not BorderExpandOption
		EndIf

		If BorderExpandOption=0
			ResizeName$="Current"
		Else
			ResizeName$="Duplicate"
		EndIf

		; Formerly StartX,StartY+60
		ShowTooltipRightAligned(GfxWidth,StartY+105,"Scroll the mouse wheel to change the resize setting: Resize "+ResizeName$)

		If mx<StartX+40 And my<StartY+30
			If LeftMouse=True And LeftMouseReleased=True
				LeftMouseReleased=False
				If CtrlDown()
					TheString$=InputString$("Enter Width: ")
					If TheString$<>""
						NewWidth=TheString$
						DeltaWidth=NewWidth-LevelWidth
						WidthLeftChange=DeltaWidth
						ReSizeLevel()
					EndIf
				Else
					WidthLeftChange=Adj
					ReSizeLevel()
				EndIf
			EndIf
			If RightMouse=True And RightMouseReleased=True
				If MaybeGetResizeConfirmation(Adj)
					WidthLeftChange=-Adj
					RightMouseReleased=False
					ReSizeLevel()
				EndIf
			EndIf
		EndIf
		If mx>=StartX+40 And my<StartY+30
			If LeftMouse=True And LeftMouseReleased=True
				LeftMouseReleased=False
				If CtrlDown()
					TheString$=InputString$("Enter Width: ")
					If TheString$<>""
						NewWidth=TheString$
						DeltaWidth=NewWidth-LevelWidth
						WidthRightChange=DeltaWidth
						ReSizeLevel()
					EndIf
				Else
					WidthRightChange=Adj
					ReSizeLevel()
				EndIf
			EndIf
			If RightMouse=True And RightMouseReleased=True
				If MaybeGetResizeConfirmation(Adj)
					WidthRightChange=-Adj
					RightMouseReleased=False
					ReSizeLevel()
				EndIf
			EndIf
		EndIf

		StartY=50

		If mx<StartX+40 And my>=StartY+15 And my<StartY+30
			If LeftMouse=True And LeftMouseReleased=True
				LeftMouseReleased=False
				If CtrlDown()
					TheString$=InputString$("Enter Height: ")
					If TheString$<>""
						NewHeight=TheString$
						DeltaHeight=NewHeight-LevelHeight
						HeightTopChange=DeltaHeight
						ResizeLevel()
					EndIf
				Else
					HeightTopChange=Adj
					ReSizeLevel()
				EndIf
			EndIf
			If RightMouse=True And RightMouseReleased=True
				If MaybeGetResizeConfirmation(Adj)
					HeightTopChange=-Adj
					RightMouseReleased=False
					ReSizeLevel()
				EndIf
			EndIf
		EndIf
		If mx>=StartX+40 And my>=StartY+15 And my<StartY+30
			If LeftMouse=True And LeftMouseReleased=True
				LeftMouseReleased=False
				If CtrlDown()
					TheString$=InputString$("Enter Height: ")
					If TheString$<>""
						NewHeight=TheString$
						DeltaHeight=NewHeight-LevelHeight
						HeightBottomChange=DeltaHeight
						ResizeLevel()
					EndIf
				Else
					HeightBottomChange=Adj
					ReSizeLevel()
				EndIf
			EndIf
			If RightMouse=True And RightMouseReleased=True
				If MaybeGetResizeConfirmation(Adj)
					HeightBottomChange=-Adj
					RightMouseReleased=False
					ReSizeLevel()
				EndIf
			EndIf
		EndIf
	EndIf

	; *************************************
	; OBJECTS
	; *************************************

	If mx>LevelViewportWidth And my>SidebarY+285
		If (LeftMouse=True Or RightMouse=True) And my<LevelViewportHeight
			SetEditorMode(3)
		EndIf
		If my<SidebarY+455
			If DeleteKey=True And DeleteKeyReleased=True And NofSelectedObjects<>0
				DeleteKeyReleased=False

				While NofSelectedObjects<>0
					DeleteObject(SelectedObjects(0))
				Wend

				RecalculateDragSize()
				SetEditorMode(3)

				ObjectsWereChanged()
				AddUnsavedChange()
			EndIf
		EndIf
	EndIf

	StartX=SidebarX+10 ;510
	StartY=SidebarY+305

	If mx>=StartX And mx<=StartX+185
		If my>=StartY And my<StartY+15
			TooltipLeftY=StartY+30
			Percent#=Float#(NofObjects)/Float#(MaxNofObjects)*100.0
			ShowTooltipRightAligned(StartX,TooltipLeftY,NofObjects+"/"+MaxNofObjects+" ("+Percent#+"%) level object slots used")
			If NofSelectedObjects=1
				If LeftMouse=True And LeftMouseReleased=True And CtrlDown()
					LeftMouseReleased=False
					TakenString$=InputString$("Set object index: ")
					If TakenString$<>""
						TargetIndex=TakenString$
						If TargetIndex>NofObjects-1
							TargetIndex=NofObjects-1
						ElseIf TargetIndex<0
							TargetIndex=0
						EndIf
						SetObjectIndex(SelectedObjects(0),TargetIndex)
						RemoveSelectObject(SelectedObjects(0))
						AddSelectObject(TargetIndex)
						AddUnsavedChange()
					EndIf
				ElseIf MouseScroll<>0
					SetEditorMode(3)
					TargetIndex=SelectedObjects(0)+MouseScroll*Adj
					If TargetIndex>NofObjects-1
						TargetIndex=NofObjects-1
					ElseIf TargetIndex<0
						TargetIndex=0
					EndIf
					RemoveSelectObject(SelectedObjects(0))
					AddSelectObject(TargetIndex)
				EndIf
			EndIf
		EndIf

		; Adjust object adjusters
		For i=ObjectAdjusterStart+0 To ObjectAdjusterStart+8
			If my>=StartY+15+(i-ObjectAdjusterStart)*15 And my<StartY+15+(i-ObjectAdjusterStart)*15+15
				If LeftMouse=True Or RightMouse=True Or MouseScroll<>0 Or ReturnKey=True
					AdjustObjectAdjuster(i)
					SetEditorMode(3)
				EndIf
				HoverOverObjectAdjuster(i)
			EndIf
		Next
	EndIf

	; *************************************
	; Preset Objects
	; *************************************

	StartX=SidebarX+195 ;695
	StartY=SidebarY+435

	If CtrlDown()
		If KeyPressed(49) ; Ctrl+N
			SetBrushMode(BrushModeNormal)
		EndIf
		If KeyPressed(48) ; Ctrl+B
			ToggleBlockMode()
		EndIf
		If KeyPressed(33) ; Ctrl+F
			ToggleFillMode()
		EndIf
		If KeyPressed(23) ; Ctrl+I
			ToggleInlineHardMode()
		EndIf
		If KeyPressed(22) ; Ctrl+U
			ToggleInlineSoftMode()
		EndIf
		If KeyPressed(37) ; Ctrl+K
			ToggleOutlineHardMode()
		EndIf
		If KeyPressed(36) ; Ctrl+J
			ToggleOutlineSoftMode()
		EndIf

		If KeyPressed(46) ; Ctrl+C
			CopySelectedObjectsToBrush()
		EndIf
	EndIf

	; Placed in code before the adjuster page switch button to eat the click before that.
	If NofSelectedObjects<>0 And CurrentGrabbedObjectModified
		; Update button
		If mx>=StartX+44 And Mx<StartX+100 And my>=StartY And my<StartY+20
			If LeftMouse=True And LeftMouseReleased=True
				LeftMouseReleased=False
				UpdateSelectedObjects()
			EndIf
		EndIf
		If KeyDown(19) ; R key
			UpdateSelectedObjects()
		EndIf
	EndIf

	If KeyPressed(20) ; T key
		If CtrlDown() ; Ctrl+T
			If BrushMode=BrushModeTestLevel
				SetBrushMode(BrushModeNormal)
			Else
				UpdateSelectedObjectsIfExists()
				SaveLevel()
				SetBrushMode(BrushModeTestLevel)
			EndIf
		Else
			If GetConfirmation("Give the current object its default TrueMovement values?")
				If RetrieveDefaultTrueMovement()
					SetEditorMode(3)
				Else
					ShowMessage("No default values exist for the current object Type!", 2000)
				EndIf
			EndIf
		EndIf
	EndIf

	If KeyPressed(34) ; G key
		For i=0 To NofObjects-1
			TheType=LevelObjects(i)\Attributes\LogicType
			TheSubType=LevelObjects(i)\Attributes\LogicSubType
			If TheType=90 And (TheSubType=10 Or (TheSubType=15 And LevelObjects(i)\Attributes\Data0=7)) ; LevelExit or CMD 7
				If TryLevelGoto(i,BrushCursorX,BrushCursorY,1,2,3)
					Exit
				EndIf
			ElseIf TheType=242 And LevelObjects(i)\Attributes\Data2=7 ; Cuboid with CMD 7
				If TryLevelGoto(i,BrushCursorX,BrushCursorY,3,4,5)
					Exit
				EndIf
			EndIf
		Next
	EndIf

	If KeyPressed(33) ; F key
		; Flip brush horizontally
		BrushSpaceStartX=GetBrushSpaceXStart()
		BrushSpaceStartY=GetBrushSpaceYStart()
		BrushSpaceEndX=GetBrushSpaceXEnd(BrushSpaceStartX)
		If EditorMode=0
			For i=0 To BrushSpaceWidth/2-1
				For j=0 To BrushSpaceHeight-1
					X1=BrushSpaceWrapX(BrushSpaceStartX+i)
					Y=BrushSpaceWrapY(BrushSpaceStartY+j)
					X2=BrushSpaceWrapX(BrushSpaceEndX-i)

					SwapTiles(BrushTiles(X1,Y),BrushTiles(X2,Y))
				Next
			Next
		ElseIf EditorMode=3
			For k=0 To NofBrushObjects-1
				TheThingy=(BrushWidth+1) Mod 2 ; what the fuck?
				X2=BrushSpaceWrapX(BrushSpaceWidth-TheThingy-BrushObjectTileXOffset(k))
				BrushObjectTileXOffset(k)=X2
			Next
		EndIf

		GenerateBrushPreview()
	EndIf

	If KeyPressed(47) ; V key
		; Rotate brush 90 degrees
		; TODO: Write this code.
		BrushSpaceStartX=GetBrushSpaceXStart()
		BrushSpaceStartY=GetBrushSpaceYStart()
		BrushSpaceEndX=GetBrushSpaceXEnd(BrushSpaceStartX)
		If EditorMode=0
			For i=0 To BrushSpaceWidth-1
				For j=0 To BrushSpaceHeight-1
					X1=BrushSpaceWrapX(BrushSpaceStartX+i)
					Y=BrushSpaceWrapY(BrushSpaceStartY+j)
					X2=BrushSpaceWrapX(BrushSpaceEndX-i)

					SwapTiles(BrushTiles(X1,Y),BrushTiles(X2,Y))
				Next
			Next
		ElseIf EditorMode=3
			For k=0 To NofBrushObjects-1
				TheThingy=(BrushWidth+1) Mod 2 ; what the fuck?
				X2=BrushSpaceWrapX(BrushSpaceWidth-TheThingy-BrushObjectTileXOffset(k))
				BrushObjectTileXOffset(k)=X2
			Next
		EndIf

		Temp=BrushSpaceWidth
		BrushSpaceWidth=BrushSpaceHeight
		BrushSpaceHeight=Temp

		Temp=BrushWidth
		SetBrushWidth(BrushHeight)
		SetBrushHeight(Temp)

		GenerateBrushPreview()
	EndIf

	If KeyPressed(15) ; tab key
		If EditorMode=EditorModeTile
			SetEditorMode(EditorModeObject)
		ElseIf EditorMode=EditorModeObject
			SetEditorMode(EditorModeTile)
		EndIf
	EndIf

	If HotkeySave()
		SaveLevel()
	ElseIf HotkeyOpen()
		If AskToSaveLevelAndExit()
			OpenTypedLevel()
		EndIf
	EndIf

	If CtrlDown()
		If KeyPressed(14) ; Ctrl+Backspace, formerly Ctrl+[ (26)
			TryPopPreviousLevel()
		ElseIf KeyPressed(209) ; Ctrl+PageDown
			If AskToSaveLevelAndExit()
				AccessLevelAtCenter(CurrentLevelNumber+1)
			EndIf
		ElseIf KeyPressed(201) ; Ctrl+PageUp
			If AskToSaveLevelAndExit()
				AccessLevelAtCenter(CurrentLevelNumber-1)
			EndIf
		EndIf
	EndIf

	; More button / Page switch button
	If mx>=StartX And Mx<StartX+80 And my>=StartY And my<StartY+20
		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			LeftMouseReleased=False
			SetEditorMode(3)
			NextObjectAdjusterPage()
		Else If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			RightMouseReleased=False
			SetEditorMode(3)
			PreviousObjectAdjusterPage()
		EndIf
	EndIf

	If KeyPressed(51) ; , key
		PreviousObjectAdjusterPage()
		SetEditorMode(3)
	ElseIf KeyPressed(52) ; . key
		NextObjectAdjusterPage()
		SetEditorMode(3)
	EndIf

	StartX=SidebarX+10
	StartY=SidebarY+460

	If mx>=startx And mx<startx+285 And my>=StartY+0 And my<StartY+20
		If CtrlDown() And LeftMouse=True And LeftMouseReleased=True
			LeftMouseReleased=False
			Query$=InputString$("Enter category name (or part of the name): ")
			For i=0 To NofObjectPresetCategories-1
				If SubstringMatchesAnywhere(Query$,ObjectPresetCategoryName$(i))
					CurrentObjectPresetCategory=i

					CurrentObjectPresetObject=0
					i=CurrentObjectPresetCategory

					ReadObjectPresetDirectory(i)

					SetEditorMode(3)
					LoadObjectPreset()

					Exit
				EndIf
			Next
		EndIf

		If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			CurrentObjectPresetCategory=CurrentObjectPresetCategory-1
			If CurrentObjectPresetCategory=-1 Then CurrentObjectPresetCategory=NofObjectPresetCategories-1
			RightMouseReleased=False
			CurrentObjectPresetObject=0
			i=CurrentObjectPresetCategory
			Repeat
				ReadObjectPresetDirectory(i)
				CurrentObjectPresetCategory=i
				i=i-1
				If i=-1 Then i=NofObjectPresetCategories-1

			Until NofObjectPresetObjects>0
			SetEditorMode(3)
			LoadObjectPreset()

		EndIf

		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			CurrentObjectPresetCategory=CurrentObjectPresetCategory+1
			If CurrentObjectPresetCategory=NofObjectPresetCategories Then CurrentObjectPresetCategory=0
			LeftMouseReleased=False
			CurrentObjectPresetObject=0
			i=CurrentObjectPresetCategory
			Repeat
				ReadObjectPresetDirectory(i)
				CurrentObjectPresetCategory=i
				i=i+1
				If i=NofObjectPresetCategories Then i=0

			Until NofObjectPresetObjects>0
			SetEditorMode(3)
			LoadObjectPreset()

		EndIf
	EndIf

	If mx>=startx And mx<startx+285 And my>=StartY+20 And my<StartY+40
		If CtrlDown() And LeftMouse=True And LeftMouseReleased=True
			LeftMouseReleased=False
			Query$=InputString$("Enter object name (or part of the name): ")

			FormerCategory=CurrentObjectPresetCategory
			FormerObject=CurrentObjectPresetObject

			; do two passes: first checking from the start, and then checking from anywhere

			For i=0 To NofObjectPresetCategories-1
				ReadObjectPresetDirectory(i)
				CurrentObjectPresetCategory=i
				For j=0 To NofObjectPresetObjects-1
					If SubstringMatchesStart(Query$,ObjectPresetObjectName$(j))
						CurrentObjectPresetObject=j

						SetEditorMode(3)
						LoadObjectPreset()

						Return
					EndIf
				Next
			Next

			For i=0 To NofObjectPresetCategories-1
				ReadObjectPresetDirectory(i)
				CurrentObjectPresetCategory=i
				For j=0 To NofObjectPresetObjects-1
					If SubstringMatchesAnywhere(Query$,ObjectPresetObjectName$(j))
						CurrentObjectPresetObject=j

						SetEditorMode(3)
						LoadObjectPreset()

						Return
					EndIf
				Next
			Next

			CurrentObjectPresetCategory=FormerCategory
			ReadObjectPresetDirectory(CurrentObjectPresetCategory)
			CurrentObjectPresetObject=FormerObject
		EndIf

		If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			CurrentObjectPresetObject=CurrentObjectPresetObject-1
			If CurrentObjectPresetObject=-1 Then CurrentObjectPresetObject=NofObjectPresetObjects-1
			RightMouseReleased=False

			SetEditorMode(3)
			LoadObjectPreset()
		EndIf

		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			CurrentObjectPresetObject=CurrentObjectPresetObject+1
			If CurrentObjectPresetObject=NofObjectPresetObjects Then CurrentObjectPresetObject=0
			LeftMouseReleased=False

			SetEditorMode(3)
			LoadObjectPreset()
		EndIf
	EndIf

	; *************************************
	; load/SAVE/ETC
	; *************************************

	If IsMouseOverToolbarItem(ToolbarBrushModeX,ToolbarBrushModeY-10)
		; brush mode
		BrushChange=0
		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			LeftMouseReleased=False

			ChangeBrushModeByDelta(1)
		EndIf
		If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			RightMouseReleased=False

			ChangeBrushModeByDelta(-1)
		EndIf
	EndIf

	If IsMouseOverToolbarItem(ToolbarBrushModeX,ToolbarBrushModeY+20)
		If LeftMouse=True And LeftMouseReleased=True
			LeftMouseReleased=False
			; wipe ; flip

			SetupPrompt()
			ReturnKeyReleased=False
			Print("Type W to wipe, or type X, Y, or XY to flip across the chosen")
			DesiredAction$=Upper$(Input$("axes: "))

			If DesiredAction$="W"
				For i=0 To LevelWidth-1
					For j=0 To LevelHeight-1
						ChangeLevelTile(i,j,True)
					Next
				Next
				AddUnsavedChange()
			ElseIf DesiredAction$="X"
				FlipLevelX()
				AddUnsavedChange()
			ElseIf DesiredAction="Y"
				FlipLevelY()
				AddUnsavedChange()
			ElseIf DesiredAction="XY"
				FlipLevelXY()
				AddUnsavedChange()
			EndIf
		EndIf
	EndIf

	If IsMouseOverToolbarItem(ToolbarBrushModeX,ToolbarBrushModeY+50)
		; dupe mode

		NewValue=AdjustInt("Enter dupe mode: ",DupeMode,1,1,10)
		If NewValue<>DupeMode
			SetDupeMode(NewValue)
		EndIf
	EndIf

	If IsMouseOverToolbarItem(ToolbarBrushSizeX,ToolbarBrushSizeY) And MouseDebounceFinished()
		; brush size
		If ShiftDown()
			Adj=10
		Else
			Adj=1
		EndIf
		ShouldChangeBrushWidth=my<ToolbarBrushSizeY+15 Or mx<ToolbarBrushSizeX+10
		ShouldChangeBrushHeight=my<ToolbarBrushSizeY+15 Or mx>ToolbarBrushSizeX-10
		If (LeftMouse=True And LeftMouseReleased=True) Or MouseScroll>0
			If ShouldChangeBrushWidth
				SetBrushWidth(BrushWidth+Adj)
			EndIf
			If ShouldChangeBrushHeight
				SetBrushHeight(BrushHeight+Adj)
			EndIf
			If MouseScroll=0 Then MouseDebounceSet(10)
		EndIf
		If (RightMouse=True And RightMouseReleased=True) Or MouseScroll<0
			If ShouldChangeBrushWidth
				SetBrushWidth(BrushWidth-Adj)
			EndIf
			If ShouldChangeBrushHeight
				SetBrushHeight(BrushHeight-Adj)
			EndIf
			If MouseScroll=0 Then MouseDebounceSet(10)
		EndIf
	EndIf

	If IsMouseOverToolbarItem(ToolbarTexPrefixX,ToolbarTexPrefixY)
		;texture prefix
		If CtrlDown()
			If LeftMouse=True And LeftMouseReleased=True
				If CurrentTexturePrefix=-1
					ShowMessage("Only nonzero texture prefixes can be set.",1000)
				Else
					FlushKeys
					Locate 0,0
					Color 0,0,0
					Rect 0,0,500,40,True
					Color 255,255,255
					Print "Enter texture prefix: "
					TexturePrefix$(CurrentTexturePrefix)=Input$("")
					ReturnKeyReleased=False

					WriteTexturePrefixes()
				EndIf
			EndIf
		Else
			CurrentTexturePrefix=AdjustInt("Enter texture prefix ID: ", CurrentTexturePrefix, 1, 10, DelayTime)
			If CurrentTexturePrefix<-1
				CurrentTexturePrefix=-1
			ElseIf CurrentTexturePrefix>MaxTexturePrefix
				CurrentTexturePrefix=MaxTexturePrefix
			EndIf
		EndIf
	EndIf

	If IsMouseOverToolbarItem(ToolbarShowMarkersX,ToolbarShowMarkersY) And MouseDebounceFinished()
		; show/hide markers
		If (LeftMouse=True And LeftMouseReleased=True) Or (RightMouse=True And RightMouseReleased=True) Or MouseScroll<>0
			ShowObjectPositions=Not ShowObjectPositions
			If ShowObjectPositions=True
				For j=0 To NofObjects-1
					ShowEntity ObjectPositionMarker(j)
				Next
			EndIf
			If ShowObjectPositions=False
				For j=0 To NofObjects-1
					HideEntity ObjectPositionMarker(j)
				Next
			EndIf

			If MouseScroll=0 Then MouseDebounceSet(10)
		EndIf
	EndIf

	If IsMouseOverToolbarItem(ToolbarShowObjectsX,ToolbarShowObjectsY)
		; show/hide objects
		NewValue=AdjustInt("Enter object mesh visibility level: ", ShowObjectMesh, 1, 10, DelayTime)
		If NewValue>ShowObjectMeshMax
			NewValue=0
		ElseIf NewValue<0
			NewValue=ShowObjectMeshMax
		EndIf
		WasChanged=ShowObjectMesh<>NewValue
		If WasChanged
			ShowObjectMesh=NewValue
			For j=0 To NofObjects-1
				UpdateObjectVisibility(LevelObjects(j))
			Next
		EndIf
	EndIf

	If IsMouseOverToolbarItem(ToolbarShowLogicX,ToolbarShowLogicY) And MouseDebounceFinished()
		; show/hide logic
		If (LeftMouse=True And LeftMouseReleased=True) Or (RightMouse=True And RightMouseReleased=True) Or MouseScroll<>0
			ShowLogicMesh=Not ShowLogicMesh
			If ShowLogicMesh=True
				For j=0 To Levelheight-1
					ShowEntity LogicMesh(j)
				Next
			EndIf
			If ShowLogicMesh=False
				For j=0 To Levelheight-1
					HideEntity LogicMesh(j)
				Next
			EndIf

			If MouseScroll=0 Then MouseDebounceSet(10)
		EndIf
	EndIf

	If IsMouseOverToolbarItem(ToolbarShowLevelX,ToolbarShowLevelY) And MouseDebounceFinished()
 		 ; show/hide level
		NewValue=AdjustInt("Enter level mesh visibility level: ", ShowLevelMesh, 1, 10, DelayTime)
		If NewValue>ShowLevelMeshMax
			NewValue=0
		ElseIf NewValue<0
			NewValue=ShowLevelMeshMax
		EndIf
		WasChanged=ShowLevelMesh<>NewValue
		If WasChanged
			ShowLevelMesh=NewValue
			If ShowLevelMesh=ShowLevelMeshShow
				For j=0 To LevelHeight-1
					ShowEntity LevelMesh(j)
					EntityAlpha LevelMesh(j),1.0
				Next
			ElseIf ShowLevelMesh=ShowLevelMeshHide
				For j=0 To LevelHeight-1
					HideEntity LevelMesh(j)
				Next
			ElseIf ShowLevelMesh=ShowLevelMeshTransparent
				For j=0 To LevelHeight-1
					ShowEntity LevelMesh(j)
					EntityAlpha LevelMesh(j),0.5
				Next
			EndIf

			If MouseScroll=0 Then MouseDebounceSet(10)
		EndIf
	EndIf

	If IsMouseOverToolbarItem(ToolbarSimulationLevelX,ToolbarSimulationLevelY)
		; simulation level
		NewValue=AdjustInt("Enter Simulation Level: ", SimulationLevel, 1, 10, DelayTime)
		If NewValue>SimulationLevelMax
			NewValue=0
		ElseIf NewValue<0
			NewValue=SimulationLevelMax
		EndIf
		WasChanged=NewValue<>SimulationLevel
		SimulationLevel=NewValue
		If WasChanged
			; move objects back to their default positions
			ResetSimulatedQuantities()
			For i=0 To NofObjects-1
				SimulateObjectPosition(i)
				SimulateObjectRotation(i)
				SimulateObjectScale(i)

				UpdateObjectVisibility(LevelObjects(i))
				UpdateObjectAnimation(LevelObjects(i))
			Next

			If SimulationLevel>=3
				For j=0 To LevelHeight-1
					RecalculateNormals(j)
				Next
			EndIf

			ResetSounds()
			UpdateMusic()

			LightingWasChanged()
		EndIf
	EndIf

	If IsMouseOverToolbarItem(ToolbarBrushWrapX,ToolbarBrushWrapY)
		; brush wrap
		BrushWrap=AdjustInt("Enter Brush Wrap: ", BrushWrap, 1, 10, DelayTime)
		If BrushWrap>BrushWrapMax
			BrushWrap=0
		ElseIf BrushWrap<0
			BrushWrap=BrushWrapMax
		EndIf
	EndIf

	If IsMouseOverToolbarItem(ToolbarStepPerX,ToolbarStepPerY)
		; step per
		StepPer=AdjustInt("Enter Step Per: ", StepPer, 1, 10, DelayTime)
		If StepPer>StepPerMax
			StepPer=0
		ElseIf StepPer<0
			StepPer=StepPerMax
		EndIf
	EndIf

	If IsMouseOverToolbarItem(ToolbarElevateX,ToolbarElevateY)
		; elevate
		If LeftMouse=True And LeftMouseReleased=True
			SetupPrompt()
			Print("Amount to shift level up/down (or type X to skip to Xtrude")
			Amount$=Input$("Logics): ")
			ReturnKeyReleased=False
			Adjustment#=Amount
			If Amount$="x" Or Amount$="X"
				XtrudeLogics()

				AddUnsavedChange()
			ElseIf Adjustment#<>0.0
				For i=0 To LevelWidth-1
					For j=0 To LevelHeight-1
						LevelTiles(i,j)\Terrain\Extrusion=LevelTiles(i,j)\Terrain\Extrusion+Adjustment
						LevelTiles(i,j)\Water\Height=LevelTiles(i,j)\Water\Height+Adjustment
						;UpdateLevelTile(i,j)
						;UpdateWaterTile(i,j)
					Next
				Next
				;ReBuildLevelModel()
				For i=0 To LevelWidth-1
					For j=0 To LevelHeight-1
						UpdateTile(i,j)
					Next
				Next
				;SomeTileWasChanged()
				For i=0 To NofObjects-1
					LevelObjects(i)\Attributes\ZAdjust=LevelObjects(i)\Attributes\ZAdjust+Adjustment
					UpdateObjectPosition(i)
				Next
				CurrentObject\Attributes\ZAdjust=CurrentObject\Attributes\ZAdjust+Adjustment
				BuildCurrentObjectModel()

				If GetConfirmation("Do you want to set Xtrude logics?")
					XtrudeLogics()
				EndIf

				ObjectsWereChanged()
				AddUnsavedChange()
			EndIf
		EndIf
	EndIf

	If IsMouseOverToolbarItem(ToolbarIDFilterX,ToolbarIDFilterY) And MouseDebounceFinished()
		; id filter
		If LeftMouse=True And LeftMouseReleased=True
			If IDFilterEnabled=False Or CtrlDown()
				IDFilterAllow=InputInt("Enter the ID to filter for: ")
				IDFilterEnabled=True
			Else
				IDFilterEnabled=False
				IDFilterInverted=False
			EndIf
			For j=0 To NofObjects-1
				UpdateObjectVisibility(LevelObjects(j))
			Next
			MouseDebounceSet(10)
		EndIf
		If IDFilterEnabled
			If MouseScroll<>0
				If ShiftDown()
					Adj=50
				Else
					Adj=1
				EndIf
				IDFilterAllow=IDFilterAllow+Adj*MouseScroll
				For j=0 To NofObjects-1
					UpdateObjectVisibility(LevelObjects(j))
				Next
			EndIf
			If RightMouse=True And RightMouseReleased=True
				IDFilterInverted=Not IDFilterInverted
				For j=0 To NofObjects-1
					UpdateObjectVisibility(LevelObjects(j))
				Next
				MouseDebounceSet(10)
			EndIf
		EndIf
	EndIf

	If IsMouseOverToolbarItem(ToolbarDensityX,ToolbarDensityY)
		; placement density
		Value#=AdjustFloat#("Enter placement density (0.0 to 1.0): ",PlacementDensity#,0.05,0.2,DelayTime)
		SetPlacementDensity(Value#)
	EndIf

	If KeyPressed(1) ; Esc key
		If AskToSaveLevelAndExit()
			ResumeMaster()
		EndIf
	EndIf

	If LeftMouse=True And LeftMouseReleased=True
		If IsMouseOverToolbarItem(ToolbarExitX,ToolbarExitY)
			; exit ; cancel and exit
			If AskToSaveLevelAndExit()
				ResumeMaster()
			EndIf

			Repeat
			Until LeftMouseDown()=False
		EndIf

		If IsMouseOverToolbarItem(ToolbarSaveX,ToolbarSaveY)
			; save ; save and exit
			;If SaveLevelAndExit()
			;	ResumeMaster()
			;EndIf
			If UnsavedChanges
				SaveLevel()
			Else
				If BrushMode=BrushModeTestLevel
					SetBrushMode(BrushModeNormal)
				Else
					SetBrushMode(BrushModeTestLevel)
				EndIf
			EndIf

			Repeat
			Until LeftMouseDown()=False

		EndIf
	EndIf

End Function

; Returns True if the user chose to save.
Function SaveLevelAndExit()

	If CurrentObjectCanBeUpdated()
		FlushKeys
		SetupWarning()
		Print("You have not hit the Update button on the selected object.")
		Print("Type R to update the object and save and exit.")
		Confirm$=Upper$(Input$("Type E to save and exit without updating: "))
		ReturnKeyReleased=False
		If Confirm="E"
			SaveLevel()
			Return True
		ElseIf Confirm="R"
			UpdateSelectedObjects()
			SaveLevel()
			Return True
		Else
			Return False
		EndIf
	Else
		SaveLevel()
		Return True
	EndIf

End Function

; Returns True if the user chooses to proceed, with or without saving.
Function AskToSaveLevelAndExit()

	If UnsavedChanges
		FlushKeys
		SetupWarning()
		Print("This level has unsaved changes. Type R to save and exit.")
		Typed$=Upper$(Input$("Type E to exit without saving: "))
		ReturnKeyReleased=False
		If Typed$="R"
			If SaveLevelAndExit()
				Return True
			Else
				Return False
			EndIf
		ElseIf Typed$="E"
			Return True
		Else
			Return False
		EndIf
	ElseIf CurrentObjectCanBeUpdated()
		FlushKeys
		SetupWarning()
		Print("You have not hit the Update button on the selected object.")
		Print("Type R to update the object and save and exit.")
		Confirm$=Upper$(Input$("Type E to exit without updating: "))
		ReturnKeyReleased=False
		If Confirm="E"
			Return True
		ElseIf Confirm="R"
			UpdateSelectedObjects()
			SaveLevel()
			Return True
		Else
			Return False
		EndIf
	Else
		Return True
	EndIf

End Function

Function SaveDialogAndExit()

	SaveDialogFile()
	ClearDialogFile()
	ResumeMaster()

End Function

; Returns True if the user chooses to proceed, with or without saving.
Function AskToSaveDialogAndExit()

	If UnsavedChanges
		FlushKeys
		SetupWarning()
		Print("This dialog has unsaved changes. Type R to save and exit.")
		Typed$=Upper$(Input$("Type E to exit without saving: "))
		ReturnKeyReleased=False
		If Typed$="R"
			SaveDialogFile()
			Return True
		ElseIf Typed$="E"
			Return True
		Else
			Return False
		EndIf
	Else
		Return True
	EndIf

End Function

Function CurrentObjectCanBeUpdated()

	Return NofSelectedObjects<>0 And CurrentGrabbedObjectModified

End Function

Function PositionIsEqual(x1,y1,x2,y2)

	Return x1=x2 And y1=y2

End Function

Function GetBrushSpaceXStart()

	; The parenthesis are necessary because otherwise BASIC will evaluate this differently for no reason. Great!
	Return -(BrushSpaceWidth/2)

End Function

Function GetBrushSpaceYStart()

	Return -(BrushSpaceHeight/2)

End Function

Function GetBrushSpaceXEnd(BrushXStart)

	Return BrushXStart+BrushSpaceWidth-1

End Function

Function GetBrushSpaceYEnd(BrushYStart)

	Return BrushYStart+BrushSpaceHeight-1

End Function

Function GetBrushXStart()

	Return BrushCursorX-BrushWidth/2

End Function

Function GetBrushYStart()

	Return BrushCursorY-BrushHeight/2

End Function

Function GetBrushXEnd(BrushXStart)

	Return BrushXStart+BrushWidth-1

End Function

Function GetBrushYEnd(BrushYStart)

	Return BrushYStart+BrushHeight-1

End Function

Function BrushSpaceWrapX(X)

	Return EuclideanRemainderInt(X,BrushSpaceWidth)

End Function

Function BrushSpaceWrapY(Y)

	Return EuclideanRemainderInt(Y,BrushSpaceHeight)

End Function

Function LevelSpaceToBrushSpaceX(X,WrapMode)

	Select WrapMode
	Case BrushWrapModulus
		Return BrushSpaceWrapX(X-BrushSpaceOriginX)
	Case BrushWrapRelative
		Return BrushSpaceWrapX(X-BrushCursorX)
	Case BrushWrapRandom
		Return Rand(0,BrushSpaceWidth-1)
	End Select

End Function

Function LevelSpaceToBrushSpaceY(Y,WrapMode)

	Select WrapMode
	Case BrushWrapModulus
		Return BrushSpaceWrapY(Y-BrushSpaceOriginY)
	Case BrushWrapRelative
		Return BrushSpaceWrapY(Y-BrushCursorY)
	Case BrushWrapRandom
		Return Rand(0,BrushSpaceHeight-1)
	End Select

End Function

Function XtrudeLogics()

	Prompt$=InputString$("Enter logic for Xtrude < 0 (leave blank for water): ")
	Logic=LogicNameToLogicId(Prompt$)
	If Logic=-1
		LessThanZero=2
	Else
		LessThanZero=Logic
	EndIf
	Prompt$=InputString$("Enter logic for Xtrude == 0 (leave blank for floor): ")
	Logic=LogicNameToLogicId(Prompt$)
	If Logic=-1
		EqualToZero=0
	Else
		EqualToZero=Logic
	EndIf
	Prompt$=InputString$("Enter logic for Xtrude > 0 (leave blank for wall): ")
	Logic=LogicNameToLogicId(Prompt$)
	If Logic=-1
		GreaterThanZero=1
	Else
		GreaterThanZero=Logic
	EndIf
	SetXtrudeLogics(LessThanZero,EqualToZero,GreaterThanZero)

End Function

Function ToggleBlockMode()

	If IsBrushInBlockMode()
		SetBrushMode(BrushModeNormal)
	Else
		SetBrushMode(BrushModeBlock)
	EndIf

End Function

Function ToggleFillMode()

	ToggleFromNormalBrush(BrushModeFill)

End Function

Function ToggleInlineSoftMode()

	ToggleFromNormalBrush(BrushModeInlineSoft)

End Function

Function ToggleInlineHardMode()

	ToggleFromNormalBrush(BrushModeInlineHard)

End Function

Function ToggleOutlineSoftMode()

	ToggleFromNormalBrush(BrushModeOutlineSoft)

End Function

Function ToggleOutlineHardMode()

	ToggleFromNormalBrush(BrushModeOutlineHard)

End Function

Function ToggleFromNormalBrush(TargetBrushMode)

	If BrushMode=TargetBrushMode
		SetBrushMode(BrushModeNormal)
	Else
		SetBrushMode(TargetBrushMode)
	EndIf

End Function

Function SetPlacementDensity(Value#)

	PlacementDensity#=Value#
	If PlacementDensity#<0.0
		PlacementDensity#=0.0
	ElseIf PlacementDensity#>1.0
		PlacementDensity#=1.0
	EndIf
	PlacementDensity#=ZeroRoundFloat#(PlacementDensity#)

End Function

Function ReadObjectPresetDirectory(index)

	NofObjectPresetObjects=0
	Dir=ReadDir("Data\Editor\ObjectPresets\"+ObjectPresetCategoryName$(index))
	file$=NextFile$(Dir)
	While file$<>""
		If file$<>"." And file$<>".." And FileType("Data\Editor\ObjectPresets\"+ObjectPresetCategoryName$(index)+"\"+file$)=1 And Lower$(Right$(file$,4))=".wop"
			ObjectPresetObjectName$(NofObjectPresetObjects)=file$
			NofObjectPresetObjects=NofObjectPresetObjects+1
		EndIf
		file$=NextFile$(Dir)
	Wend
	CloseDir dir

End Function

Function AddOrToggleSelectObject(i)

	If CtrlDown() And (Not ShiftDown())
		ToggleSelectObject(i)
	Else
		AddSelectObject(i)
	EndIf

End Function

Function ShowSelectedObjectMarker(i)

	ShowEntity CurrentGrabbedObjectMarkers(i)
	UpdateCurrentGrabbedObjectMarkerPosition(i)

End Function

Function HideSelectedObjectMarker(i)

	HideEntity CurrentGrabbedObjectMarkers(i)

End Function

Function UpdateSelectedObjectMarkerVisibility(i)

	If IsObjectSelected(i) And EditorMode=3
		ShowSelectedObjectMarker(i)
	Else
		HideSelectedObjectMarker(i)
	EndIf

End Function

Function UpdateAllSelectedObjectMarkersVisibility()

	For i=0 To MaxNofObjects-1
		UpdateSelectedObjectMarkerVisibility(i)
	Next

End Function

; these could potentially be made smarter
Function SubstringMatchesStart(Query$,Subject$)

	; make case insensitive
	Query$=Upper$(Query$)
	Subject$=Upper$(Subject$)

	; truncate subject to be length of query
	Subject$=Left$(Subject$,Len(Query$))

	Return Query$=Subject$

End Function

Function SubstringMatchesAnywhere(Query$,Subject$)

	; make case insensitive
	Query$=Upper$(Query$)
	Subject$=Upper$(Subject$)

	Return Instr(Subject$,Query$)<>0

End Function

Function CtrlDown()

	Return KeyDown(29) Or KeyDown(157) ; left ctrl or right ctrl

End Function

Function ShiftDown()

	Return KeyDown(42) Or KeyDown(54) ; left shift or right shift

End Function

; DO NOT USE!!! Alt-tabbing out of the application will make it think alt is still being pressed when you return.
;Function AltDown()
;
;	Return KeyDown(56) Or KeyDown(184) ; left alt or right alt
;
;End Function

Function FindAndReplaceKeyDown()

	Return KeyDown(41) ; tilde key

End Function

Function HotkeySave()

	Return CtrlDown() And KeyPressed(31) ; Ctrl+S

End Function

Function HotkeyOpen()

	Return CtrlDown() And KeyPressed(24) ; Ctrl+O

End Function

Function SetupWarning()

	Locate 0,0
	Color 0,0,0
	Rect 0,0,500,40,True
	Color TextWarningR,TextWarningG,TextWarningB

End Function

Function GetConfirmation(Message$)

	FlushKeys
	SetupWarning()
	Print Message$
	Confirm$=Input$("Type Y to confirm: ")
	ReturnKeyReleased=False
	Return Confirm="Y" Or Confirm="y"

End Function

Function MaybeGetResizeConfirmation(Adj)

	If NofObjects<>0 And Adj>1
		Return GetConfirmation("Are you sure you want to reduce the level size by "+Adj+"?")
	Else
		Return True
	EndIf

End Function

Function ForceKeyRelease(keycode, keyname$)

	If KeyDown(keycode)
		SetupWarning()
		Print "Press "+keyname$+" to proceed."
		Print "I am doing this to protect you and your family."
		Repeat Until Not KeyDown(keycode)
	EndIf

End Function

Function LogicIdToLogicName$(TileLogic)

	If TileLogic>=0 And TileLogic<=14
		Return LogicName$(TileLogic)
	Else
		Return TileLogic
	EndIf

End Function

; returns -1 if no matching name is found
Function LogicNameToLogicId(Name$)

	For i=0 To 14
		If Upper$(LogicName$(i))=Upper$(Name$)
			Return i
		EndIf
	Next

	Return -1

End Function

Function LevelTileLogicHasVisuals(i,j)

	Return LevelTiles(i,j)\Terrain\Logic>0 And LevelTiles(i,j)\Terrain\Logic<15

End Function

Function RemoveEntityTexture(Entity)

	NewEntity=CopyMesh(Entity)
	FreeEntity(Entity)
	Return NewEntity

End Function

Function RemoveMD2Texture(Entity,Path$)

	NewEntity=myLoadMD2(Path$)
	FreeEntity(Entity)
	Return NewEntity

End Function

Function ObstacleNameToObstacleId(ModelName$)

	FirstDigit=Asc(Mid$(ModelName$,10,1))-48
	SecondDigit=Asc(Mid$(ModelName$,11,1))-48
	Return FirstDigit*10+SecondDigit

End Function

Function TryGetObstacleMesh(ObstacleId)

	If ObstacleId>0 And ObstacleId<>14 And ObstacleId<>49 And ObstacleId<63
		Return CopyEntity(ObstacleMesh(ObstacleId))
	Else
		Return CreateErrorMesh()
	EndIf

End Function

Function TryUseObstacleTexture(Entity,ObstacleId)

	If ObstacleId>0 And ObstacleId<>10 And ObstacleId<>14 And (ObstacleId<36 Or ObstacleId>42) And ObstacleId<>49 And ObstacleId<63
		EntityTexture Entity,ObstacleTexture(ObstacleId)
	Else
		Entity=RemoveEntityTexture(Entity)

		UseErrorColor(Entity)
	EndIf

	Return Entity

End Function

Function ReplyFunctionToName$(Fnc)

	Select Fnc
	Case 1
		Return "End Dialog"
	Case 2
		Return "Continue Dialog"
	Case 3
		Return "Open AskAbout"
	Case 4
		Return "Consume Coins (+1 Interchange if player doesn't have enough, +2 otherwise)"
	Case 5
		Return "Consume Item (+1 Interchange if player doesn't have item, +2 otherwise)"
	Case 6
		Return "Check For Item (+1 Interchange if player doesn't have item, +2 otherwise)"
	Default
		Return "Do Nothing"
	End Select

End Function

Function ReplyFunctionToDataName$(Fnc)

	Select Fnc
	Case 1,2
		Return "Destination Interchange"
	Case 4
		Return "Number of Coins"
	Case 5
		Return "Item FNC ID"
	Case 6
		Return "Item FNC ID"
	Default
		Return "N/A"
	End Select

End Function

Function GetAskAboutActiveName$(Active)

	Select Active
	Case -2
		Return "Active"
	Case -1
		Return "Inactive (must be activated with CMD 23, 25, or 26)"
	Default
		If Active>-1
			Return "Active if MasterAskAbout "+Active+" is active (modify with CMD 28, 29, or 30)"
		Else
			Return "???"
		EndIf
	End Select

End Function

Function GetAskAboutActiveNameShort$(Active)

	Select Active
	Case -2
		Return "Active"
	Case -1
		Return "Inactive"
	Default
		If Active>-1
			Return "MasterAA "+Active
		Else
			Return Active
		EndIf
	End Select

End Function

Function GetAskAboutRepeatName$(Value)

	If Value<0
		Return "Can be used infinitely"
	ElseIf Value=0
		Return "Never available"
	Else
		Return "Can be used "+Value+" "+MaybePluralize$("time",Value)
	EndIf

End Function

Function ObjectTypeCollisionBitToName$(BitIndex)

	Select BitIndex
	Case 1
		Return "Player"
	Case 2
		Return "Talkable NPCs and Signs"
	Case 3
		Return "Wee Stinkers and Baby Boomers"
	Case 4
		Return "Items"
	Case 5
		Return "Scritters and Tentacles"
	Case 6
		Return "Sunken Turtles and (if TTC has Bridge) Transporters"
	Case 7
		Return "Untalkable NPCs, Signs, Unsunken Turtles, FireFlowers, Ducks, Moobots, Burstflowers"
	Case 8
		Return "Chompers, Spikeyballs, Ghosts, and Retro Monsters"
	Case 9
		Return "Barrels, Cuboids, Boxes, and GrowFlowers"
	Case 10
		Return "Frozen Objects"
	Default
		Return "???"
	End Select

End Function

Function BitPositionIndexToBitIndex(BitPositionIndex)

	BitIndex=BitPositionIndex
	If BitIndex>10
		BitIndex=BitIndex-1
	EndIf
	If BitIndex>5
		BitIndex=BitIndex-1
	EndIf
	Return BitIndex

End Function

Function BitIndexIsValid(BitIndex)

	Return BitIndex>-1 And BitIndex<15

End Function

Function BitPositionIndexIsValid(BitPositionIndex)

	Return BitPositionIndex<>5 And BitPositionIndex<>11

End Function

Function GetBitPositionIndex(BitStartX)

	Return (MouseX()-BitStartX)/8

End Function

Function ColorLevelTileLogic(i,j)

	Select LevelTiles(i,j)\Terrain\Logic
	Case 1 ; wall
		red=255
		green=0
		blue=0
	Case 2; water
		red=0
		green=0
		blue=255
	Case 3; teleporter
		red=140
		green=100
		blue=0
	Case 4; bridge
		red=70
		green=70
		blue=15
	Case 5; lava
		red=140
		green=0
		blue=255
	Case 6 ; 06
		red=40
		green=40
		blue=40
	Case 7 ; 07
		red=80
		green=80
		blue=80
	Case 8 ; 08
		red=120
		green=120
		blue=120
	Case 9; button
		red=255
		green=255
		blue=0
	Case 10; stinker exit
		red=0
		green=255
		blue=0
	Case 11,12,13; ice
		red=0
		green=255
		blue=255
	Case 14; ice float
		red=255
		green=255
		blue=255
	Default
		red=0
		green=0
		blue=0
	End Select

	For k=0 To LogicVerticesPerTile-1
		VertexColor LogicSurface(j),i*LogicVerticesPerTile+k,red,green,blue;,.5
	Next

End Function

Function ReBuildLevelModel()

;	For i=0 To MaxLevelCoordinate
;		If LevelMesh(i)>0
;			FreeEntity LevelMesh(i)
;			LevelMesh(i)=0
;		EndIf
;		If WaterMesh(i)>0
;			FreeEntity WaterMesh(i)
;			WaterMesh(i)=0
;		EndIf
;		If LogicMesh(i)>0
;			FreeEntity LogicMesh(i)
;			LogicMesh(i)=0
;		EndIf
;	Next

	BuildLevelModel()

End Function

Function CreateLevelTileTop(i,j)
	mySurface=LevelSurface(j)

	; do each tile with subdivision of detail level
	; First, create the vertices
	For j2=0 To LevelDetail
		For i2=0 To LevelDetail

			xoverlap#=0
			yoverlap#=0
			zoverlap#=0
			If j2=0 Or j2=LevelDetail Or i2=0 Or i2=LevelDetail
				If i2=0
					xoverlap#=-0.005
				;	zoverlap#=+0.005
				EndIf
				If j2=0
					yoverlap#=0.005
				;	zoverlap#=+0.005
				EndIf
				height=0
			EndIf

			CalculateUV(LevelTiles(i,j)\Terrain\Texture,i2,j2,LevelTiles(i,j)\Terrain\Rotation,8,LevelDetail)

			AddVertex(mySurface,i+Float(i2)/Float(LevelDetail)+xoverlap,height+zoverlap,-(j+Float(j2)/Float(LevelDetail))+yoverlap,ChunkTileu,ChunkTilev)
		Next
	Next
	; Now create the triangles
	For j2=0 To LevelDetail-1
		For i2=0 To LevelDetail-1
			AddTriangle (mySurface,GetLevelVertex(i,j,i2,j2),GetLevelVertex(i,j,i2+1,j2),GetLevelVertex(i,j,i2,j2+1))
			AddTriangle (mySurface,GetLevelVertex(i,j,i2+1,j2),GetLevelVertex(i,j,i2+1,j2+1),GetLevelVertex(i,j,i2,j2+1))
		Next
	Next

	ShiftLevelTileByRandom(i,j)
	ShiftLevelTileByHeight(i,j)
	ShiftLevelTileByExtrude(i,j)
	ShiftLevelTileEdges(i,j)

End Function

Function CreateLevelTileSides(i,j)
	mySurface=LevelSurface(j)

	; here we also calculate how much the bottom edge of the side wall should be pushed "out"
	; the maxfactor is the maximum (corners are not pushed out)
	If LevelTiles(i,j)\Terrain\EdgeRandom=1
		randommax#=0.2
	Else
		randommax#=0.0
	EndIf

	overhang#=0.0

	; north side
	random#=0 ; this is the random for the lower edge - set to zero and only caclulate for the second pixel,
				; that way, the first pixel of the next square will have the same random factor
	If j>0
		;If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,j-1)\Terrain\Extrusion
			; yep, add two triangles per LevelDetail connecting the two bordering coordinates
			For i2=0 To LevelDetail-1
				vertex=GetLevelVertex(i,j,LevelDetail-i2,0)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,VertexX(mySurface,vertex),VertexY(mySurface,vertex),VertexZ(mySurface,vertex)-overhang,ChunkTileU,ChunkTileV)

				vertex=GetLevelVertex(i,j,LevelDetail-i2-1,0)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2+1,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,VertexX(mySurface,vertex),VertexY(mySurface,vertex),VertexZ(mySurface,vertex)-overhang,ChunkTileU,ChunkTileV)

				vertex=GetLevelVertex(i,j,LevelDetail-i2,0)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,i+Float(LevelDetail-i2)/Float(LevelDetail),VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i,j-1)\Terrain\Extrusion,-j+random,ChunkTileU,ChunkTileV)

				vertex=GetLevelVertex(i,j,LevelDetail-i2-1,0)
				If i2<LevelDetail-1
					random#=Rnd(0,randommax)
				Else
					random#=0
				EndIf

				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2+1,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,i+Float(LevelDetail-i2-1)/Float(LevelDetail),VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i,j-1)\Terrain\Extrusion,-j+random,ChunkTileU,ChunkTileV)

				AddTriangle (mySurface,currentvertex,currentvertex+1,currentvertex+2)
				AddTriangle (mySurface,currentvertex+1,currentvertex+3,currentvertex+2)

				currentvertex=currentvertex+4
			Next
		;EndIf
	EndIf

	; east side
	random#=0
	If i<LevelWidth-1
		;If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i+1,j)\Terrain\Extrusion
			; yep, add two triangles per LevelDetail connecting the two bordering coordinates
			For j2=0 To LevelDetail-1
				vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,VertexX(mySurface,vertex)-overhang,VertexY(mySurface,vertex),VertexZ(mySurface,vertex),ChunkTileU,ChunkTileV)

				vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2-1)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2+1,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,VertexX(mySurface,vertex)-overhang,VertexY(mySurface,vertex),VertexZ(mySurface,vertex),ChunkTileU,ChunkTileV)

				vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,i+1+random,VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i+1,j)\Terrain\Extrusion,-(j+Float(LevelDetail-j2)/Float(LevelDetail)),ChunkTileU,ChunkTileV)

				vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2-1)
				If j2<LevelDetail-1
					random#=Rnd(0,randommax)
				Else
					random#=0
				EndIf

				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2+1,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,i+1+random,VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i+1,j)\Terrain\Extrusion,-(j+Float(LevelDetail-j2-1)/Float(LevelDetail)),ChunkTileU,ChunkTileV)

				AddTriangle (mySurface,currentvertex,currentvertex+1,currentvertex+2)
				AddTriangle (mySurface,currentvertex+1,currentvertex+3,currentvertex+2)

				currentvertex=currentvertex+4
			Next
		;EndIf
	EndIf

	; south side
	random#=0
	If j<LevelHeight-1
		;If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,j+1)\Terrain\Extrusion
			; yep, add two triangles per LevelDetail connecting the two bordering coordinates
			For i2=0 To LevelDetail-1
				vertex=GetLevelVertex(i,j,i2,LevelDetail)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,VertexX(mySurface,vertex),VertexY(mySurface,vertex),VertexZ(mySurface,vertex)+overhang,ChunkTileU,ChunkTileV)
				vertex=GetLevelVertex(i,j,i2+1,LevelDetail)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2+1,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,VertexX(mySurface,vertex),VertexY(mySurface,vertex),VertexZ(mySurface,vertex)+overhang,ChunkTileU,ChunkTileV)

				vertex=GetLevelVertex(i,j,i2,LevelDetail)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,i+Float(i2)/Float(LevelDetail),VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i,j+1)\Terrain\Extrusion,-(j+1+random),ChunkTileU,ChunkTileV)

				vertex=GetLevelVertex(i,j,i2+1,LevelDetail)
				If i2<LevelDetail-1
					random#=Rnd(0,randommax)
				Else
					random#=0
				EndIf

				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2+1,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,i+Float(i2+1)/Float(LevelDetail),VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i,j+1)\Terrain\Extrusion,-(j+1+random),ChunkTileU,ChunkTileV)

				AddTriangle (mySurface,currentvertex,currentvertex+1,currentvertex+2)
				AddTriangle (mySurface,currentvertex+1,currentvertex+3,currentvertex+2)

				currentvertex=currentvertex+4
			Next
		;EndIf
	EndIf

	; west side
	random#=0
	If i>0
		;If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i-1,j)\Terrain\Extrusion
			; yep, add two triangles per LevelDetail connecting the two bordering coordinates
			For j2=0 To LevelDetail-1
				vertex=GetLevelVertex(i,j,0,j2)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,VertexX(mySurface,vertex)+overhang,VertexY(mySurface,vertex),VertexZ(mySurface,vertex),ChunkTileU,ChunkTileV)

				vertex=GetLevelVertex(i,j,0,j2+1)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2+1,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,VertexX(mySurface,vertex)+overhang,VertexY(mySurface,vertex),VertexZ(mySurface,vertex),ChunkTileU,ChunkTileV)

				vertex=GetLevelVertex(i,j,0,j2)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,i-random,VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i-1,j)\Terrain\Extrusion,-(j+Float(j2)/Float(LevelDetail)),ChunkTileU,ChunkTileV)

				vertex=GetLevelVertex(i,j,0,j2+1)
				If j2<LevelDetail-1
					random#=Rnd(0,randommax)
				Else
					random#=0
				EndIf

				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2+1,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				AddVertex (mySurface,i-random,VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i-1,j)\Terrain\Extrusion,-(j+Float(j2+1)/Float(LevelDetail)),ChunkTileU,ChunkTileV)

				AddTriangle (mySurface,currentvertex,currentvertex+1,currentvertex+2)
				AddTriangle (mySurface,currentvertex+1,currentvertex+3,currentvertex+2)

				currentvertex=currentvertex+4
			Next
		;EndIf
	EndIf

End Function

Const SideNotInLevelZ=-10000 ;-100

Function UpdateLevelTileSides(i,j)
	mySurface=LevelSurface(j)

	; here we also calculate how much the bottom edge of the side wall should be pushed "out"
	; the maxfactor is the maximum (corners are not pushed out)
	If LevelTiles(i,j)\Terrain\EdgeRandom=1
		randommax#=0.2
	Else
		randommax#=0.0
	EndIf

	overhang#=0.0

	CurrentIndex=0

	; north side
	random#=0 ; this is the random for the lower edge - set to zero and only caclulate for the second pixel,
				; that way, the first pixel of the next square will have the same random factor
	If j>0
		;If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,j-1)\Terrain\Extrusion
			z2=0
			If LevelTiles(i,j-1)\Terrain\Extrusion>=LevelTiles(i,j)\Terrain\Extrusion z2=SideNotInLevelZ
			; yep, add two triangles per LevelDetail connecting the two bordering coordinates
			For i2=0 To LevelDetail-1
				vertex=GetLevelVertex(i,j,LevelDetail-i2,0)
				;vertexside=GetLevelSideVertex(i,j,LevelDetail-i2,0)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+z2,VertexZ(mySurface,vertex)-overhang
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV

				vertex=GetLevelVertex(i,j,LevelDetail-i2-1,0)
				;vertexside=GetLevelSideVertex(i,j,LevelDetail-i2-1,0)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+1)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2+1,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+z2,VertexZ(mySurface,vertex)-overhang
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV

				vertex=GetLevelVertex(i,j,LevelDetail-i2,0)
				;vertexside=GetLevelSideVertex(i,j,LevelDetail-i2,0)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+2)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,i+Float(LevelDetail-i2)/Float(LevelDetail),VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i,j-1)\Terrain\Extrusion+z2,-j+random
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV

				vertex=GetLevelVertex(i,j,LevelDetail-i2-1,0)
				;vertexside=GetLevelSideVertex(i,j,LevelDetail-i2-1,0)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+3)
				If i2<LevelDetail-1
					random#=Rnd(0,randommax)
				Else
					random#=0
				EndIf

				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2+1,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,i+Float(LevelDetail-i2-1)/Float(LevelDetail),VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i,j-1)\Terrain\Extrusion+z2,-j+random
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV

				CurrentIndex=CurrentIndex+4
			Next
		;EndIf
	EndIf

	; east side
	random#=0
	If i<LevelWidth-1
		;If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i+1,j)\Terrain\Extrusion
			z2=0
			If LevelTiles(i+1,j)\Terrain\Extrusion>=LevelTiles(i,j)\Terrain\Extrusion z2=SideNotInLevelZ
			; yep, add two triangles per LevelDetail connecting the two bordering coordinates
			For j2=0 To LevelDetail-1
				vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2)
				;vertexside=GetLevelSideVertex(i,j,LevelDetail,LevelDetail-j2)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,VertexX(mySurface,vertex)-overhang,VertexY(mySurface,vertex)+z2,VertexZ(mySurface,vertex)
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV

				vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2-1)
				;vertexside=GetLevelSideVertex(i,j,LevelDetail,LevelDetail-j2-1)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+1)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2+1,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,VertexX(mySurface,vertex)-overhang,VertexY(mySurface,vertex)+z2,VertexZ(mySurface,vertex)
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV

				vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2)
				;vertexside=GetLevelSideVertex(i,j,LevelDetail,LevelDetail-j2)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+2)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,i+1+random,VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i+1,j)\Terrain\Extrusion+z2,-(j+Float(LevelDetail-j2)/Float(LevelDetail))
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV

				vertex=GetLevelVertex(i,j,LevelDetail,LevelDetail-j2-1)
				;vertexside=GetLevelSideVertex(i,j,LevelDetail,LevelDetail-j2-1)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+3)
				If j2<LevelDetail-1
					random#=Rnd(0,randommax)
				Else
					random#=0
				EndIf

				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2+1,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,i+1+random,VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i+1,j)\Terrain\Extrusion+z2,-(j+Float(LevelDetail-j2-1)/Float(LevelDetail))
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV

				CurrentIndex=CurrentIndex+4
			Next
		;EndIf
	EndIf

	; south side
	random#=0
	If j<LevelHeight-1
		;If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,j+1)\Terrain\Extrusion
			z2=0
			If LevelTiles(i,j+1)\Terrain\Extrusion>=LevelTiles(i,j)\Terrain\Extrusion z2=SideNotInLevelZ
			; yep, add two triangles per LevelDetail connecting the two bordering coordinates
			For i2=0 To LevelDetail-1
				vertex=GetLevelVertex(i,j,i2,LevelDetail)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+z2,VertexZ(mySurface,vertex)+overhang
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV

				vertex=GetLevelVertex(i,j,i2+1,LevelDetail)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+1)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2+1,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+z2,VertexZ(mySurface,vertex)+overhang
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV

				vertex=GetLevelVertex(i,j,i2,LevelDetail)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+2)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,i+Float(i2)/Float(LevelDetail),VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i,j+1)\Terrain\Extrusion+z2,-(j+1+random)
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV

				vertex=GetLevelVertex(i,j,i2+1,LevelDetail)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+3)
				If i2<LevelDetail-1
					random#=Rnd(0,randommax)
				Else
					random#=0
				EndIf

				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2+1,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,i+Float(i2+1)/Float(LevelDetail),VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i,j+1)\Terrain\Extrusion+z2,-(j+1+random)
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV

				CurrentIndex=CurrentIndex+4
			Next
		;Else
		;	For i2=0 To LevelDetail-1
		;		vertex=GetLevelVertex(i,j,i2,LevelDetail)
		;		vertexside=GetLevelSideVertex(i,j,CurrentIndex)
		;		CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,i2,0,LevelTiles(i,j)\Terrain\SideRotation,8)
		;		VertexCoords mySurface,vertexside,VertexX(mySurface,vertex),VertexY(mySurface,vertex),VertexZ(mySurface,vertex)
		;		VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV
		;	Next
		;EndIf
	EndIf

	; west side
	random#=0
	If i>0
		;If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i-1,j)\Terrain\Extrusion
			z2=0
			If LevelTiles(i-1,j)\Terrain\Extrusion>=LevelTiles(i,j)\Terrain\Extrusion z2=SideNotInLevelZ
			; yep, add two triangles per LevelDetail connecting the two bordering coordinates
			For j2=0 To LevelDetail-1
				vertex=GetLevelVertex(i,j,0,j2)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,VertexX(mySurface,vertex)+overhang,VertexY(mySurface,vertex)+z2,VertexZ(mySurface,vertex)
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV

				vertex=GetLevelVertex(i,j,0,j2+1)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+1)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2+1,0,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,VertexX(mySurface,vertex)+overhang,VertexY(mySurface,vertex)+z2,VertexZ(mySurface,vertex)
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV

				vertex=GetLevelVertex(i,j,0,j2)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+2)
				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,i-random,VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i-1,j)\Terrain\Extrusion+z2,-(j+Float(j2)/Float(LevelDetail))
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV

				vertex=GetLevelVertex(i,j,0,j2+1)
				vertexside=GetLevelSideVertex(i,j,CurrentIndex+3)
				If j2<LevelDetail-1
					random#=Rnd(0,randommax)
				Else
					random#=0
				EndIf

				CalculateUV(LevelTiles(i,j)\Terrain\SideTexture,j2+1,LevelDetail,LevelTiles(i,j)\Terrain\SideRotation,8,LevelDetail)
				VertexCoords mySurface,vertexside,i-random,VertexY(mySurface,vertex)-LevelTiles(i,j)\Terrain\Extrusion+LevelTiles(i-1,j)\Terrain\Extrusion+z2,-(j+Float(j2+1)/Float(LevelDetail))
				VertexTexCoords mySurface,vertexside,ChunkTileU,ChunkTileV

				CurrentIndex=CurrentIndex+4
			Next
		;EndIf
	EndIf

End Function

Function ClampInt(min,max,value)

	If value<min
		Return min
	ElseIf value>max
		Return max
	Else
		Return value
	EndIf

End Function

Function ClampToLevelWidth(value)

	Return ClampInt(0,LevelWidth-1,value)

End Function

Function ClampToLevelHeight(value)

	Return ClampInt(0,LevelHeight-1,value)

End Function

Function ShiftLevelTileByRandom(i,j)
	mySurface=LevelSurface(j)

	iMinusOne=Maximum2(i-1,0)
	iPlusOne=Minimum2(i+1,LevelWidth-1)
	jMinusOne=Maximum2(j-1,0)
	jPlusOne=Minimum2(j+1,LevelHeight-1)

	For i2=0 To LevelDetail
		For j2=0 To LevelDetail
			If i2=0 And j2=0
				random#=Minimum4(LevelTiles(IMinusOne,jMinusOne)\Terrain\Random,LevelTiles(i,jMinusOne)\Terrain\Random,LevelTiles(iMinusOne,j)\Terrain\Random,LevelTiles(i,j)\Terrain\Random)
			Else If j2=0 And i2=LevelDetail
				random#=Minimum4(LevelTiles(IPlusOne,jMinusOne)\Terrain\Random,LevelTiles(i,jMinusOne)\Terrain\Random,LevelTiles(IPlusOne,j)\Terrain\Random,LevelTiles(i,j)\Terrain\Random)
			Else If j2=LevelDetail And i2=0
				random#=Minimum4(LevelTiles(IMinusOne,jPlusOne)\Terrain\Random,LevelTiles(iMinusOne,j)\Terrain\Random,LevelTiles(i,jPlusOne)\Terrain\Random,LevelTiles(i,j)\Terrain\Random)
			Else If i2=LevelDetail And j2=LevelDetail
				random#=Minimum4(LevelTiles(IPlusOne,jPlusOne)\Terrain\Random,LevelTiles(i,jPlusOne)\Terrain\Random,LevelTiles(IPlusOne,j)\Terrain\Random,LevelTiles(i,j)\Terrain\Random)
			Else If j2=0
				random#=Minimum2(LevelTiles(i,jMinusOne)\Terrain\Random,LevelTiles(i,j)\Terrain\Random)
			Else If j2=LevelDetail
				random#=Minimum2(LevelTiles(i,jPlusOne)\Terrain\Random,LevelTiles(i,j)\Terrain\Random)
			Else If i2=0
				random#=Minimum2(LevelTiles(IMinusOne,j)\Terrain\Random,LevelTiles(i,j)\Terrain\Random)
			Else If i2=LevelDetail
				random#=Minimum2(LevelTiles(IPlusOne,j)\Terrain\Random,LevelTiles(i,j)\Terrain\Random)
			Else
				random#=LevelTiles(i,j)\Terrain\Random
			EndIf

			vertex=GetLevelVertex(i,j,i2,j2)
			random2#=random*LevelVertexRandom(Float(i2),Float(j2))

			VertexCoords mySurface,vertex,VertexX(mysurface,vertex),VertexY(mysurface,vertex)+random2,VertexZ(mysurface,vertex)

		Next
	Next

End Function

Function HeightAtRowVertex#(i,j,i2)

	If i2<LevelDetail/2
		; first half of tile, compare with left neighbour
		If i=0
			OtherHeight#=LevelTiles(i,j)\Terrain\Height ;0.0
		Else
			OtherHeight#=LevelTiles(i-1,j)\Terrain\Height
		EndIf
		Return OtherHeight+(LevelTiles(i,j)\Terrain\Height-OtherHeight)*Float(i2+Float(LevelDetail)/2.0)/Float(LevelDetail)
	Else
		; second half of tile, compare with right neighbour
		If i=LevelWidth-1
			OtherHeight#=LevelTiles(i,j)\Terrain\Height ;0.0
		Else
			OtherHeight#=LevelTiles(i+1,j)\Terrain\Height
		EndIf
		Return LevelTiles(i,j)\Terrain\Height+(OtherHeight-LevelTiles(i,j)\Terrain\Height)*Float(i2-LevelDetail/2)/Float(LevelDetail)

	EndIf

End Function

Function ShiftLevelTileByHeight(i,j)
;	If LevelDetail<2 Or Floor(LevelDetail/2)*2<>LevelDetail
;		; must be divisible by two, or disable height function
;		Return
;	EndIf

	mySurface=LevelSurface(j)

	For i2=0 To LevelDetail

		NewHeight#=HeightAtRowVertex#(i,j,i2)

		vertex=GetLevelVertex(i,j,i2,LevelDetail/2)
		VertexCoords mySurface,vertex,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+NewHeight,VertexZ(mySurface,vertex)

		If j=LevelHeight-1
			OtherHeight#=LevelTiles(i,j)\Terrain\Height
		Else
			OtherHeight#=HeightAtRowVertex#(i,j+1,i2)
		EndIf

		; as of second row, build vertical bridge to first row
		For j2=LevelDetail/2+1 To LevelDetail
			; first half is actually 2nd half of previous row
			; (also no need to lift first vertex of that part, that's already the center of
			;  the row and hence lifted above)
			ThisVertexesHeight#=NewHeight#+(OtherHeight-NewHeight)*Float(j2-LevelDetail/2)/Float(LevelDetail)
			If i>=0 And j<=LevelHeight-1 And i<=LevelWidth-1
				vertex=GetLevelVertex(i,j,i2,j2)
				VertexCoords mySurface,vertex,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+ThisVertexesHeight,VertexZ(mySurface,vertex)
			EndIf
		Next

		If j=0
			OtherHeight#=LevelTiles(i,j)\Terrain\Height
		Else
			OtherHeight#=HeightAtRowVertex#(i,j-1,i2)
		EndIf

		For j2=0 To LevelDetail/2-1
			; 2nd half (we're now in the top half of this row)
			ThisVertexesHeight#=OtherHeight#+(NewHeight-OtherHeight)*Float(j2+LevelDetail/2)/Float(LevelDetail)
			vertex=GetLevelVertex(i,j,i2,j2)
			VertexCoords mySurface,vertex,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+ThisVertexesHeight,VertexZ(mySurface,vertex)
		Next

	Next

End Function

Function ShiftLevelTileByExtrude(i,j)
	mySurface=LevelSurface(j)

	For j2=0 To LevelDetail
		For i2=0 To LevelDetail
			vertex=GetLevelVertex(i,j,i2,j2)
			VertexCoords mySurface,vertex,VertexX(mySurface,vertex),VertexY(mySurface,vertex)+LevelTiles(i,j)\Terrain\Extrusion,VertexZ(mySurface,vertex)
			;VertexCoords mySurface,vertex,VertexX(mySurface,vertex),LevelTiles(i,j)\Terrain\Extrusion,VertexZ(mySurface,vertex)
		Next
	Next

End Function

Function ResetLevelTile(i,j)
	mySurface=LevelSurface(j)

	For j2=0 To LevelDetail
		For i2=0 To LevelDetail
			xoverlap#=0
			yoverlap#=0
			zoverlap#=0
			If j2=0 Or j2=LevelDetail Or i2=0 Or i2=LevelDetail
				If i2=0
					xoverlap#=-0.005
				;	zoverlap#=+0.005
				EndIf
				If j2=0
					yoverlap#=0.005
				;	zoverlap#=+0.005
				EndIf
				height=0
			EndIf

			;CalculateUV(LevelTiles(i,j)\Terrain\Texture,i2,j2,LevelTiles(i,j)\Terrain\Rotation,8)

			vertex=GetLevelVertex(i,j,i2,j2)
			VertexCoords mySurface,vertex,i+Float(i2)/Float(LevelDetail)+xoverlap,height+zoverlap,-(j+Float(j2)/Float(LevelDetail))+yoverlap
		Next
	Next

End Function

Function ShiftLevelTileEdges(i,j)
	mySurface=LevelSurface(j)

	iMinusOne=Maximum2(i-1,0)
	iPlusOne=Minimum2(i+1,LevelWidth-1)
	jMinusOne=Maximum2(j-1,0)
	jPlusOne=Minimum2(j+1,LevelHeight-1)

	If LevelTiles(i,j)\Terrain\Rounding=1

		; is there a drop-off NE corner:
		If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(iPlusOne,j)\Terrain\Extrusion And LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,jMinusOne)\Terrain\Extrusion And LevelTiles(iPlusOne,j)\Terrain\Extrusion=LevelTiles(i,jMinusOne)\Terrain\Extrusion
			; yep: round-off
			For i2=(LevelDetail/2)+1 To LevelDetail
				For j2=(LevelDetail/2)+1 To Leveldetail
					vertex=GetLevelVertex(i,j,i2,LevelDetail-j2)
					; convert (i2,j2) to (0...1)
					a#=Float(i2-(LevelDetail/2))/Float(LevelDetail/2)
					b#=Float(j2-(LevelDetail/2))/Float(LevelDetail/2)
					r#=Float(maximum2(i2,j2)-(LevelDetail/2))/Float(LevelDetail/2)
					x#=r/Sqr(1+b^2/a^2)
					y#=Sqr(r^2-x^2)

					VertexCoords mySurface,vertex,i+0.5+x#/2.0,VertexY(mySurface,vertex),-(j+0.5-y#/2.0)
				Next
			Next

		EndIf

		; is there a drop-off SE corner:
		If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(iPlusOne,j)\Terrain\Extrusion And LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,jPlusOne)\Terrain\Extrusion And LevelTiles(iPlusOne,j)\Terrain\Extrusion=LevelTiles(i,jPlusOne)\Terrain\Extrusion
			; yep: round-off
			For i2=(LevelDetail/2)+1 To LevelDetail
				For j2=(LevelDetail/2)+1 To Leveldetail
					vertex=GetLevelVertex(i,j,i2,j2)
					; convert (i2,j2) to (0...1)
					a#=Float(i2-(LevelDetail/2))/Float(LevelDetail/2)
					b#=Float(j2-(LevelDetail/2))/Float(LevelDetail/2)
					r#=Float(maximum2(i2,j2)-(LevelDetail/2))/Float(LevelDetail/2)
					x#=r/Sqr(1+b^2/a^2)
					y#=Sqr(r^2-x^2)

					VertexCoords mySurface,vertex,i+0.5+x#/2.0,VertexY(mySurface,vertex),-(j+0.5+y#/2.0)
				Next
			Next

		EndIf
		; SW corner
		If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(iMinusOne,j)\Terrain\Extrusion And LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,jPlusOne)\Terrain\Extrusion And LevelTiles(iMinusOne,j)\Terrain\Extrusion=LevelTiles(i,jPlusOne)\Terrain\Extrusion
			; yep: round-off
			For i2=(LevelDetail/2)+1 To LevelDetail
				For j2=(LevelDetail/2)+1 To Leveldetail
					vertex=GetLevelVertex(i,j,LevelDetail-i2,j2)
					; convert (i2,j2) to (0...1)
					a#=Float(i2-(LevelDetail/2))/Float(LevelDetail/2)
					b#=Float(j2-(LevelDetail/2))/Float(LevelDetail/2)
					r#=Float(maximum2(i2,j2)-(LevelDetail/2))/Float(LevelDetail/2)
					x#=r/Sqr(1+b^2/a^2)
					y#=Sqr(r^2-x^2)

					VertexCoords mySurface,vertex,i+0.5-x#/2.0,VertexY(mySurface,vertex),-(j+0.5+y#/2.0)
				Next
			Next

		EndIf

		; is there a drop-off NW corner:
		If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(iMinusOne,j)\Terrain\Extrusion And LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,jMinusOne)\Terrain\Extrusion And LevelTiles(iMinusOne,j)\Terrain\Extrusion=LevelTiles(i,jMinusOne)\Terrain\Extrusion
			; yep: round-off
			For i2=(LevelDetail/2)+1 To LevelDetail
				For j2=(LevelDetail/2)+1 To Leveldetail
					vertex=GetLevelVertex(i,j,LevelDetail-i2,LevelDetail-j2)
					; convert (i2,j2) to (0...1)
					a#=Float(i2-(LevelDetail/2))/Float(LevelDetail/2)
					b#=Float(j2-(LevelDetail/2))/Float(LevelDetail/2)
					r#=Float(maximum2(i2,j2)-(LevelDetail/2))/Float(LevelDetail/2)
					x#=r/Sqr(1+b^2/a^2)
					y#=Sqr(r^2-x^2)

					VertexCoords mySurface,vertex,i+0.5-x#/2.0,VertexY(mySurface,vertex),-(j+0.5-y#/2.0)
				Next
			Next

		EndIf

	EndIf

	randommax#=0.1

	If LevelTiles(i,j)\Terrain\EdgeRandom=1
		; north side
		If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,jMinusOne)\Terrain\Extrusion

			j2=0
			For i2=0 To LevelDetail
				vertex=GetLevelVertex(i,j,i2,j2)
				If i2=0
					If LevelTiles(iMinusOne,j)\Terrain\Extrusion=LevelTiles(i,jMinusOne)\Terrain\Extrusion
						random#=1.0
					Else
						random#=0
					EndIf
				Else If i2=LevelDetail
					If LevelTiles(iPlusOne,j)\Terrain\Extrusion=LevelTiles(i,jMinusOne)\Terrain\Extrusion
						random#=1.0
					Else
						random#=0
					EndIf
				Else
					random#=Rnd(0,1)
				EndIf

				VertexCoords mySurface,vertex,VertexX(mySurface,vertex),VertexY(mySurface,vertex),VertexZ(mySurface,vertex)-random*randommax
			Next

		EndIf
		; east side
		If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(iPlusOne,j)\Terrain\Extrusion

			i2=LevelDetail
			For j2=0 To LevelDetail
				vertex=GetLevelVertex(i,j,i2,j2)
				If j2=0
					If LevelTiles(iPlusOne,j)\Terrain\Extrusion=LevelTiles(i,jMinusOne)\Terrain\Extrusion
						random#=1.0
					Else
						random#=0
					EndIf
				Else If j2=LevelDetail
					If LevelTiles(iPlusOne,j)\Terrain\Extrusion=LevelTiles(i,jPlusOne)\Terrain\Extrusion
						random#=1.0
					Else
						random#=0
					EndIf
				Else
					random#=Rnd(0,1)
				EndIf
				VertexCoords mySurface,vertex,VertexX(mySurface,vertex)-random*randommax,VertexY(mySurface,vertex),VertexZ(mySurface,vertex)
			Next

		EndIf
		; south side
		If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(i,jPlusOne)\Terrain\Extrusion

			j2=LevelDetail
			For i2=0 To LevelDetail
				vertex=GetLevelVertex(i,j,i2,j2)
				If i2=0
					If LevelTiles(iMinusOne,j)\Terrain\Extrusion=LevelTiles(i,jPlusOne)\Terrain\Extrusion
						random#=1.0
					Else
						random#=0
					EndIf
				Else If i2=LevelDetail
					If LevelTiles(iPlusOne,j)\Terrain\Extrusion=LevelTiles(i,jPlusOne)\Terrain\Extrusion
						random#=1.0
					Else
						random#=0
					EndIf
				Else
					random#=Rnd(0,1)
				EndIf
				VertexCoords mySurface,vertex,VertexX(mySurface,vertex),VertexY(mySurface,vertex),VertexZ(mySurface,vertex)+random*randommax
			Next

		EndIf
		; west side
		If LevelTiles(i,j)\Terrain\Extrusion>LevelTiles(iMinusOne,j)\Terrain\Extrusion

			i2=0
			For j2=0 To LevelDetail
				vertex=GetLevelVertex(i,j,i2,j2)
				If j2=0
					If LevelTiles(iMinusOne,j)\Terrain\Extrusion=LevelTiles(i,jMinusOne)\Terrain\Extrusion
						random#=1.0
					Else
						random#=0
					EndIf
				Else If j2=LevelDetail
					If LevelTiles(iMinusOne,j)\Terrain\Extrusion=LevelTiles(i,jPlusOne)\Terrain\Extrusion
						random#=1.0
					Else
						random#=0
					EndIf
				Else
					random#=Rnd(0,1)
				EndIf
				VertexCoords mySurface,vertex,VertexX(mySurface,vertex)+random*randommax,VertexY(mySurface,vertex),VertexZ(mySurface,vertex)
			Next

		EndIf
	EndIf

End Function

Function UpdateLevelTileTexture(i,j)

	For j2=0 To LevelDetail
		For i2=0 To LevelDetail
			CalculateUV(LevelTiles(i,j)\Terrain\Texture,i2,j2,LevelTiles(i,j)\Terrain\Rotation,8,LevelDetail)
			vertex=GetLevelVertex(i,j,i2,j2)
			VertexTexCoords LevelSurface(j),vertex,ChunkTileU,ChunkTileV
		Next
	Next

End Function

Function GetLevelVertex(i,j,i2,j2)
	; Gets the index number of the vertex at chunk tile (i,j) with detail subdivision (i2,j2)
	; in the currentchunk

	; since the chunk has a border around it, we decrease i and j by 1, and reduce width by 2
	;i=i-1
	;j=j-1
	;n=(i+j*(LevelWidth))*(LevelDetail+1)*(LevelDetail+1) ; get to start of tile
	n=i*(LevelDetail+1)*(LevelDetail+1)
	n=n+j2*(LevelDetail+1)+i2
	Return n
End Function

Function GetLevelSideVertex(i,j,index)
	; edge vertex generation order: north -> east -> south -> west
	; start at the end of all the regular vertices
	n=LevelWidth*(LevelDetail+1)*(LevelDetail+1)
	If j=0 Or j=LevelHeight-1
		VerticesPerTile=12*LevelDetail
	Else
		VerticesPerTile=16*LevelDetail
	EndIf
	If i>0
		; account for westmost face being missing on level border
		index=index-4*LevelDetail
	EndIf
	n=n+i*VerticesPerTile+index
	Return n

End Function

Function LevelVertexRandom#(x#,y#)
	; creates a random number between 0 and 1 based on two input numbers from 0 to LevelDetail (i.e.i2/j2)
	; used to create random pertubations in vertices in order to ensure that neighbouring vertices
	; (i.e. same x/y coordinates, but in neightbouring chunks) get the same perturbation

	If Floor(x)>0 And Floor(y)>0 And Floor(x)<LevelDetail And Floor(y)<LevelDetail
		; in interior of tile - do true random
		Return Rnd(0,1)
	EndIf

	x#=Abs(x-LevelDetail/2) ; take the i2/j2 and rework as "distance from center"
	y#=Abs(y-LevelDetail/2) ; so that opposing sides are treated equally
							; (they must, since a right side of tile i is the left side of i+1)

	random#=(x+.59)*(y+.73)*241783
	intrandom=Int(random)
	intrandom=(intrandom Mod 700) + (intrandom Mod 300)
	random=Float(intrandom)/1000.0
	Return Random#
End Function

Function Minimum2#(x#,y#)
	If x<y
		Return x
	Else
		Return y
	EndIf
End Function
Function Minimum4#(x#,y#,z#,w#)
	If x<=y And x<=z And x<=w
		Return x
	Else If y<=x And y<=z And y<=W
		Return y
	Else If z<=x And z<=y And z<=w
		Return z
	Else
		Return w
	EndIf
End Function
Function Maximum2#(x#,y#)
	If x>y
		Return x
	Else
		Return y
	EndIf
End Function

Function DeltaTo(Start,Destination)
	Return Destination-Start
End Function

Function MirrorAcrossInt(MyPosition, MirrorPosition)
	Delta=DeltaTo(MyPosition,MirrorPosition)
	Return Delta+MirrorPosition
End Function
Function MirrorAcrossFloat#(MyPosition#, MirrorPosition#)
	Delta#=DeltaTo(MyPosition#,MirrorPosition#)
	Return Delta#+MirrorPosition#
End Function

Function EuclideanRemainderInt(Value,Divisor)

	Result=Value Mod Divisor
	If Result<0
		Result=Result+Divisor
	EndIf
	Return Result

End Function

Function EuclideanRemainderFloat#(Value#,Divisor#)

	Result#=Value# Mod Divisor#
	If Result#<0
		Result#=Result#+Divisor#
	EndIf
	Return Result#

End Function

Function UpdateWaterMeshGlow(Entity)

	If WaterGlow=True
		EntityBlend Entity,3
	Else
		EntityBlend Entity,1
	EndIf

End Function

Function UpdateWaterMeshTransparent(Entity)

	If WaterTransparent=True
		EntityAlpha Entity,.5
	Else
		EntityAlpha Entity,1
	EndIf

End Function

Function UpdateAllWaterMeshGlow()

	For i=0 To MaxLevelCoordinate
		UpdateWaterMeshGlow(WaterMesh(i))
	Next
	UpdateWaterMeshGlow(CurrentWaterTile)

End Function

Function UpdateAllWaterMeshTransparent()

	For i=0 To MaxLevelCoordinate
		UpdateWaterMeshTransparent(WaterMesh(i))
	Next
	UpdateWaterMeshTransparent(CurrentWaterTile)

End Function

Function RecalculateNormals(j)

	UpdateNormals LevelMesh(j)
	For i=0 To LevelWidth-1
		For i2=0 To LevelDetail
			For j2=0 To LevelDetail
				If i2=0 Or i2=LevelDetail Or j2=0 Or j2=LevelDetail
					vertex=GetLevelVertex(i,j,i2,j2)
					VertexNormal LevelSurface(j),vertex,0.0,1.0,0.0
				EndIf
			Next
		Next
	Next

End Function

Function InitializeLevelModel()

	For i=0 To MaxLevelCoordinate
		;FreeEntity LevelMesh(i)
		LevelMesh(i)=CreateMesh()
		LevelSurface(i)=CreateSurface(LevelMesh(i))
		;EntityFX LevelMesh(i),2

		;FreeEntity WaterMesh(i)
		Watermesh(i)=CreateMesh()
		Watersurface(i)=CreateSurface(Watermesh(i))
		;EntityAlpha WaterMesh(i),.5
		;EntityFX WaterMesh(i),2
		;UpdateWaterMeshGlow(i)
		;UpdateWaterMeshTransparent(i)

		;FreeEntity LogicMesh(i)
		Logicmesh(i)=CreateMesh()
		Logicsurface(i)=CreateSurface(Logicmesh(i))
		EntityFX LogicMesh(i),2
	Next

End Function

Function BuildLevelModel()

	UpdateAllWaterMeshGlow()
	UpdateAllWaterMeshTransparent()

	For j=0 To MaxLevelCoordinate
		ClearSurface LevelSurface(j)
		ClearSurface WaterSurface(j)
		ClearSurface LogicSurface(j)
	Next

	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			CreateLevelTileTop(i,j)
		Next
		;UpdateNormals LevelMesh(j)
		EntityTexture LevelMesh(j),LevelTexture
	Next

	For j=0 To LevelHeight-1
		; get the newest one, and increment from there
		currentvertex=GetLevelVertex(LevelWidth-1,0,LevelDetail,LevelDetail)+1

		For i=0 To LevelWidth-1
			CreateLevelTileSides(i,j)
		Next
	Next

	; and point all edge vertex normals "up" (to smooth lighting)

	For j=0 To LevelHeight-1
		RecalculateNormals(j)
	Next

	; water
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			; top face
			CalculateUV(LevelTiles(i,j)\Water\Texture,0,0,LevelTiles(i,j)\Water\Rotation,4,1)
			AddVertex (WaterSurface(j),i,LevelTiles(i,j)\Water\Height,-j,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTiles(i,j)\Water\Texture,1,0,LevelTiles(i,j)\Water\Rotation,4,1)
			AddVertex (WaterSurface(j),i+1,LevelTiles(i,j)\Water\Height,-j,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTiles(i,j)\Water\Texture,0,1,LevelTiles(i,j)\Water\Rotation,4,1)
			AddVertex (WaterSurface(j),i,LevelTiles(i,j)\Water\Height,-j-1,ChunkTileU,ChunkTileV)
			CalculateUV(LevelTiles(i,j)\Water\Texture,1,1,LevelTiles(i,j)\Water\Rotation,4,1)
			AddVertex (WaterSurface(j),i+1,LevelTiles(i,j)\Water\Height,-j-1,ChunkTileU,ChunkTileV)

			AddTriangle (WaterSurface(j),i*4+0,i*4+1,i*4+2)
			AddTriangle (WaterSurface(j),i*4+1,i*4+3,i*4+2)

			;VertexColor WaterSurface(j),i*4+0,0,0,0
			;VertexColor WaterSurface(j),i*4+1,0,0,0
			;VertexColor WaterSurface(j),i*4+2,0,0,0
			;VertexColor WaterSurface(j),i*4+3,0,0,0
		Next
		UpdateNormals WaterMesh(j)
		EntityTexture WaterMesh(j),WaterTexture

		PositionEntity WaterMesh(j),0,-0.04,0
	Next

	; logic
	For j=0 To LevelHeight-1
		ClearSurface LogicSurface(j)
		For i=0 To LevelWidth-1

			;If LevelTiles(i,j)\Terrain\Logic=1 Or LevelTiles(i,j)\Terrain\Logic=2 Or LevelTiles(i,j)\Terrain\Logic=11 Or LevelTiles(i,j)\Terrain\Logic=12 Or LevelTiles(i,j)\Terrain\Logic=13
			If LevelTileLogicHasVisuals(i,j)
				nologicshow=0
			Else
				nologicshow=-300
			EndIf
			; top face
			; pick height of logic mesh as just over maxi(water,tile)
			If LevelTiles(i,j)\Water\Height>LevelTiles(i,j)\Terrain\Extrusion
				height#=LevelTiles(i,j)\Water\Height+0.05
			Else
				height#=LevelTiles(i,j)\Terrain\Extrusion+0.05
			EndIf

			AddVertex (LogicSurface(j),i+nologicshow,height,-j)
			AddVertex (LogicSurface(j),i+1+nologicshow,height,-j)
			AddVertex (LogicSurface(j),i+nologicshow,height,-j-1)
			AddVertex (LogicSurface(j),i+1+nologicshow,height,-j-1)

			AddTriangle (LogicSurface(j),i*LogicVerticesPerTile+0,i*LogicVerticesPerTile+1,i*LogicVerticesPerTile+2)
			AddTriangle (LogicSurface(j),i*LogicVerticesPerTile+1,i*LogicVerticesPerTile+3,i*LogicVerticesPerTile+2)

			height#=0.05

			AddVertex (LogicSurface(j),i+nologicshow,height,-j)
			AddVertex (LogicSurface(j),i+1+nologicshow,height,-j)
			AddVertex (LogicSurface(j),i+nologicshow,height,-j-1)
			AddVertex (LogicSurface(j),i+1+nologicshow,height,-j-1)

			AddTriangle (LogicSurface(j),i*LogicVerticesPerTile+4,i*LogicVerticesPerTile+5,i*LogicVerticesPerTile+6)
			AddTriangle (LogicSurface(j),i*LogicVerticesPerTile+5,i*LogicVerticesPerTile+7,i*LogicVerticesPerTile+6)

			ColorLevelTileLogic(i,j)

		Next
		UpdateNormals LogicMesh(j)

		If ShowLogicMesh=True
			ShowEntity LogicMesh(j)
		Else
			HideEntity LogicMesh(j)
		EndIf

		If ShowLevelMesh=ShowLevelMeshShow
			ShowEntity LevelMesh(j)
			EntityAlpha LevelMesh(j),1.0
		ElseIf ShowLevelMesh=ShowLevelMeshHide
			HideEntity LevelMesh(j)
		ElseIf ShowLevelMesh=ShowLevelMeshTransparent
			ShowEntity LevelMesh(j)
			EntityAlpha LevelMesh(j),0.5
		EndIf
	Next

End Function

Function ChangeLevelTile(i,j,update)

	If Not PassesPlacementDensityTest()
		Return
	EndIf

	BrushSpaceX=LevelSpaceToBrushSpaceX(i,BrushWrap)
	BrushSpaceY=LevelSpaceToBrushSpaceY(j,BrushWrap)

	If StepPer=StepPerTile
		RunStepSize()
		;RunStepSize(BrushSpaceX,BrushSpaceY)
	EndIf

	GrabLevelTileFromBrush(BrushSpaceX,BrushSpaceY)

	ChangeLevelTileActual(i,j,update)

	If DupeMode=DupeModeX
		TargetX=MirrorAcrossInt(i,MirrorPositionX)
		ChangeLevelTileActual(TargetX,j,update)
	ElseIf DupeMode=DupeModeY
		TargetY=MirrorAcrossInt(j,MirrorPositionY)
		ChangeLevelTileActual(i,TargetY,update)
	ElseIf DupeMode=DupeModeXPlusY
		TargetX=MirrorAcrossInt(i,MirrorPositionX)
		TargetY=MirrorAcrossInt(j,MirrorPositionY)
		ChangeLevelTileActual(TargetX,j,update)
		ChangeLevelTileActual(i,TargetY,update)
		ChangeLevelTileActual(TargetX,TargetY,update)
	EndIf

End Function

Function ChangeLevelTileActual(i,j,update)

	If i<0
		Return
	ElseIf i>LevelWidth-1
		Return
	EndIf
	If j<0
		Return
	ElseIf j>LevelHeight-1
		Return
	EndIf

	HeightWasChanged=False

	; The Tile
	If CurrentTileTextureUse=True
		LevelTiles(i,j)\Terrain\Texture=CurrentTile\Terrain\Texture ; corresponding to squares in LevelTexture
		LevelTiles(i,j)\Terrain\Rotation=CurrentTile\Terrain\Rotation ; 0-3 , and 4-7 for "flipped"
	EndIf
	If CurrentTileSideTextureUse=True
		LevelTiles(i,j)\Terrain\SideTexture=CurrentTile\Terrain\SideTexture ; texture for extrusion walls
		LevelTiles(i,j)\Terrain\SideRotation=CurrentTile\Terrain\SideRotation ; 0-3 , and 4-7 for "flipped"
	EndIf
	If CurrentTileRandomUse=True
		LevelTiles(i,j)\Terrain\Random=CurrentTile\Terrain\Random ; random height pertubation of tile
	EndIf
	If CurrentTileHeightUse=True
		If LevelTiles(i,j)\Terrain\Height<>CurrentTile\Terrain\Height
			HeightWasChanged=True
		EndIf
		LevelTiles(i,j)\Terrain\Height=CurrentTile\Terrain\Height ; height of "center" - e.g. to make ditches and hills
	EndIf
	If CurrentTileExtrusionUse=True
		LevelTiles(i,j)\Terrain\Extrusion=CurrentTile\Terrain\Extrusion; extrusion with walls around it
	EndIf
	If CurrentTileRoundingUse=True
		LevelTiles(i,j)\Terrain\Rounding=CurrentTile\Terrain\Rounding; 0-no, 1-yes: are floors rounded if on a drop-off corner
	EndIf
	If CurrentTileEdgeRandomUse=True
		LevelTiles(i,j)\Terrain\EdgeRandom=CurrentTile\Terrain\EdgeRandom; 0-no, 1-yes: are edges rippled
	EndIf
	If CurrentTileLogicUse=True
		LevelTiles(i,j)\Terrain\Logic=CurrentTile\Terrain\Logic
	EndIf
	If update=True
		UpdateLevelTile(i,j)

		HasWest=i>0
		HasEast=i<LevelWidth-1
		HasNorth=j>0
		HasSouth=j<LevelHeight-1

		; Possibly update surrounding tiles (Height also needs to update diagonals)
		If HasWest
			If LevelTiles(i-1,j)\Terrain\Extrusion>=LevelTiles(i,j)\Terrain\Extrusion ;Or HeightWasChanged
				UpdateLevelTile(i-1,j)
			EndIf
			If HeightWasChanged
				If HasNorth
					UpdateLevelTile(i-1,j-1)
				EndIf
				If HasSouth
					UpdateLevelTile(i-1,j+1)
				EndIf
			EndIf
		EndIf
		If HasEast
			If LevelTiles(i+1,j)\Terrain\Extrusion>=LevelTiles(i,j)\Terrain\Extrusion ;Or HeightWasChanged
				UpdateLevelTile(i+1,j)
			EndIf
			If HeightWasChanged
				If HasNorth
					UpdateLevelTile(i+1,j-1)
				EndIf
				If HasSouth
					UpdateLevelTile(i+1,j+1)
				EndIf
			EndIf
		EndIf
		If HasNorth
			If LevelTiles(i,j-1)\Terrain\Extrusion>=LevelTiles(i,j)\Terrain\Extrusion ;Or HeightWasChanged
				UpdateLevelTile(i,j-1)
			EndIf
		EndIf
		If HasSouth
			If LevelTiles(i,j+1)\Terrain\Extrusion>=LevelTiles(i,j)\Terrain\Extrusion ;Or HeightWasChanged
				UpdateLevelTile(i,j+1)
			EndIf
		EndIf

		If SimulationLevel>=3
			;RecalculateNormals(j)
			DirtyNormals(j)=True
			If HasNorth
				;RecalculateNormals(j-1)
				DirtyNormals(j-1)=True
			EndIf
			If HasSouth
				;RecalculateNormals(j+1)
				DirtyNormals(j+1)=True
			EndIf
		EndIf
	EndIf

	; the water
	If CurrentWaterTileTextureUse=True
		LevelTiles(i,j)\Water\Texture=CurrentTile\Water\Texture
		LevelTiles(i,j)\Water\Rotation=CurrentTile\Water\Rotation
	EndIf
	If CurrentWaterTileHeightUse=True LevelTiles(i,j)\Water\Height=CurrentTile\Water\Height
	If CurrentWaterTileTurbulenceUse=True LevelTiles(i,j)\Water\Turbulence=CurrentTile\Water\Turbulence
	If update=True
		UpdateWaterTile(i,j)
		UpdateLogicTile(i,j)
	EndIf

End Function

Function GrabLevelTile(i,j)

	If i<0
		i=0
	ElseIf i>LevelWidth-1
		i=LevelWidth-1
	EndIf
	If j<0
		j=0
	ElseIf j>LevelHeight-1
		j=LevelHeight-1
	EndIf

	CopyTile(LevelTiles(i,j),CurrentTile)

	BuildCurrentTileModel()
	SetBrushToCurrentTile()

End Function

Function GrabLevelTileFromBrush(i,j)

	CopyTile(BrushTiles(i,j),CurrentTile)

	BuildCurrentTileModel()

End Function

Function CopyLevelTileToBrush(i,j,iDest,jDest)

	If i<0
		i=0
	ElseIf i>LevelWidth-1
		i=LevelWidth-1
	EndIf
	If j<0
		j=0
	ElseIf j>LevelHeight-1
		j=LevelHeight-1
	EndIf

	CopyTile(LevelTiles(i,j),BrushTiles(iDest,jDest))

End Function

Function LevelTileIsInBrush(tilex,tiley,cursorx,cursory)

	HalfBrushSize=BrushSize/2
	Return tilex>=cursorx-HalfBrushSize And tilex<=cursorx+HalfBrushSize And tiley>=cursory-HalfBrushSize And tiley<=cursory+HalfBrushSize

End Function

Function SetLevelTileAsTarget(i,j)

	CopyTile(LevelTiles(i,j),TargetTile)

End Function

Function LevelTileMatchesTarget(i,j)

	If i<0
		Return False
	ElseIf i>=LevelWidth
		Return False
	EndIf

	If j<0
		Return False
	ElseIf j>=LevelHeight
		Return False
	EndIf

	;If LevelTileIsInBrush(i,j)
	;	Return True
	;EndIf

	If TargetTileTextureUse And TargetTile\Terrain\Texture<>LevelTiles(i,j)\Terrain\Texture
		Return False
	EndIf
	;If TargetTile\Terrain\Rotation<>LevelTiles(i,j)\Terrain\Rotation
	;	Return False
	;EndIf
	If TargetTileSideTextureUse And TargetTile\Terrain\SideTexture<>LevelTiles(i,j)\Terrain\SideTexture
		Return False
	EndIf
	;If TargetTile\Terrain\SideRotation<>LevelTiles(i,j)\Terrain\SideRotation
	;	Return False
	;EndIf
	If TargetTileRandomUse And TargetTile\Terrain\Random<>LevelTiles(i,j)\Terrain\Random
		Return False
	EndIf
	If TargetTileHeightUse And TargetTile\Terrain\Height<>LevelTiles(i,j)\Terrain\Height
		Return False
	EndIf
	If TargetTileExtrusionUse And TargetTile\Terrain\Extrusion<>LevelTiles(i,j)\Terrain\Extrusion
		Return False
	EndIf
	If TargetTileRoundingUse And TargetTile\Terrain\Rounding<>LevelTiles(i,j)\Terrain\Rounding
		Return False
	EndIf
	If TargetTileEdgeRandomUse And TargetTile\Terrain\EdgeRandom<>LevelTiles(i,j)\Terrain\EdgeRandom
		Return False
	EndIf
	If TargetTileLogicUse And TargetTile\Terrain\Logic<>LevelTiles(i,j)\Terrain\Logic
		Return False
	EndIf

	If TargetWaterTileUse And TargetTile\Water\Texture<>LevelTiles(i,j)\Water\Texture
		Return False
	EndIf
	;If TargetTile\Water\Rotation<>LevelTiles(i,j)\Water\Rotation
	;	Return False
	;EndIf
	If TargetWaterTileHeightUse And TargetTile\Water\Height<>LevelTiles(i,j)\Water\Height
		Return False
	EndIf
	If TargetWaterTileTurbulenceUse And TargetTile\Water\Turbulence<>LevelTiles(i,j)\Water\Turbulence
		Return False
	EndIf

	Return True

End Function

Function SetXtrudeLogics(LessThanZero,EqualToZero,GreaterThanZero)

	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			If LevelTiles(i,j)\Terrain\Extrusion<0.0
				LevelTiles(i,j)\Terrain\Logic=LessThanZero
			ElseIf LevelTiles(i,j)\Terrain\Extrusion=0.0
				LevelTiles(i,j)\Terrain\Logic=EqualToZero
			ElseIf LevelTiles(i,j)\Terrain\Extrusion>0.0
				LevelTiles(i,j)\Terrain\Logic=GreaterThanZero
			EndIf
			UpdateLogicTile(i,j)
		Next
	Next

End Function

Function LoadTilePreset()

	Filename$="Data\Editor\TilePresets\"+TilePresetCategoryName$(CurrentTilePresetCategory)+"\"+TilePresetTileName$(CurrentTilePresetTile)
	file=ReadFile(filename$)
	CurrentTile\Terrain\Texture=ReadInt(file)
	CurrentTile\Terrain\Rotation=ReadInt(file)
	CurrentTile\Terrain\SideTexture=ReadInt(file)
	CurrentTile\Terrain\SideRotation=ReadInt(file)
	CurrentTile\Terrain\Random=ReadFloat(file)
	CurrentTile\Terrain\Height=ReadFloat(file)
	CurrentTile\Terrain\Extrusion=ReadFloat(file)
	CurrentTile\Terrain\Rounding=ReadInt(file)
	CurrentTile\Terrain\EdgeRandom=ReadInt(file)
	CurrentTile\Terrain\Logic=ReadInt(file)

	CurrentTile\Water\Texture=ReadInt(file)
	CurrentTile\Water\Rotation=ReadInt(file)
	CurrentTile\Water\Height=ReadFloat(file)
	CurrentTile\Water\Turbulence=ReadFloat(file)

	SetBrushToCurrentTile()
	BuildCurrentTileModel()

End Function

Function SaveTilePreset()
	FlushKeys
	Locate 0,0
	Color 0,0,0
	Rect 0,0,500,40,True
	Color 255,255,255
	Filename$=Input ("FileName: ")
	file=WriteFile ("data\editor\tilepresets\"+filename$+".tp1")

	WriteInt file,CurrentTile\Terrain\Texture
	WriteInt file,CurrentTile\Terrain\Rotation
	WriteInt file,CurrentTile\Terrain\SideTexture
	WriteInt file,CurrentTile\Terrain\SideRotation
	WriteFloat file,CurrentTile\Terrain\Random
	WriteFloat file,CurrentTile\Terrain\Height
	WriteFloat file,CurrentTile\Terrain\Extrusion
	WriteInt file,CurrentTile\Terrain\Rounding
	WriteInt file,CurrentTile\Terrain\EdgeRandom
	WriteInt file,CurrentTile\Terrain\Logic

	WriteInt file,CurrentTile\Water\Texture
	WriteInt file,CurrentTile\Water\Rotation
	WriteFloat file,CurrentTile\Water\Height
	WriteFloat file,CurrentTile\Water\Turbulence

	CloseFile file
End Function

Function BlankObjectPreset(ModelName$,ObjType,ObjSubType)

	CurrentObject\Attributes\ModelName$=ModelName$
	CurrentObject\Attributes\TexName$="!None"
	CurrentObject\Attributes\XScale#=1
	CurrentObject\Attributes\YScale#=1
	CurrentObject\Attributes\ZScale#=1
	CurrentObject\Attributes\XAdjust#=0.0
	CurrentObject\Attributes\YAdjust#=0.0
	CurrentObject\Attributes\ZAdjust#=0.0
	CurrentObject\Attributes\PitchAdjust#=0.0
	CurrentObject\Attributes\YawAdjust#=0.0
	CurrentObject\Attributes\RollAdjust#=0.0
	CurrentObject\Position\X#=0.0
	CurrentObject\Position\Y#=0.0
	CurrentObject\Position\Z#=0.0
	CurrentObject\Position\OldX#=-999
	CurrentObject\Position\OldY#=-999
	CurrentObject\Position\OldZ#=-999
	CurrentObject\Attributes\DX#=0
	CurrentObject\Attributes\DY#=0
	CurrentObject\Attributes\DZ#=0
	CurrentObject\Attributes\Pitch#=0
	CurrentObject\Attributes\Yaw#=0
	CurrentObject\Attributes\Roll#=0
	CurrentObject\Attributes\Pitch2#=0
	CurrentObject\Attributes\Yaw2#=0
	CurrentObject\Attributes\Roll2#=0
	CurrentObject\Attributes\XGoal#=0
	CurrentObject\Attributes\YGoal#=0
	CurrentObject\Attributes\ZGoal#=0
	CurrentObject\Attributes\MovementType=0
	CurrentObject\Attributes\MovementTypeData=0
	CurrentObject\Attributes\Speed#=0
	CurrentObject\Attributes\Radius#=0
	CurrentObject\Attributes\RadiusType=0
	CurrentObject\Attributes\Data10=-1
	CurrentObject\Attributes\PushDX#=0
	CurrentObject\Attributes\PushDY#=0
	CurrentObject\Attributes\AttackPower=0
	CurrentObject\Attributes\DefensePower=0
	CurrentObject\Attributes\DestructionType=0
	CurrentObject\Attributes\ID=-1
	CurrentObject\Attributes\LogicType=ObjType
	CurrentObject\Attributes\LogicSubType=ObjSubType
	CurrentObject\Attributes\Active=1001
	CurrentObject\Attributes\LastActive=1001
	CurrentObject\Attributes\ActivationType=0
	CurrentObject\Attributes\ActivationSpeed=0
	CurrentObject\Attributes\Status=0
	CurrentObject\Attributes\Timer=0
	CurrentObject\Attributes\TimerMax1=0
	CurrentObject\Attributes\TimerMax2=0
	CurrentObject\Attributes\Teleportable=False
	CurrentObject\Attributes\ButtonPush=False
	CurrentObject\Attributes\WaterReact=0
	CurrentObject\Attributes\Telekinesisable=0
	CurrentObject\Attributes\Freezable=0
	CurrentObject\Attributes\Reactive=True
	CurrentObject\Attributes\Child=-1
	CurrentObject\Attributes\Parent=-1
	CurrentObject\Attributes\Data0=0
	CurrentObject\Attributes\Data1=0
	CurrentObject\Attributes\Data2=0
	CurrentObject\Attributes\Data3=0
	CurrentObject\Attributes\Data4=0
	CurrentObject\Attributes\Data5=0
	CurrentObject\Attributes\Data6=0
	CurrentObject\Attributes\Data7=0
	CurrentObject\Attributes\Data8=0
	CurrentObject\Attributes\Data9=0
	CurrentObject\Attributes\TextData0=""
	CurrentObject\Attributes\TextData1=""
	CurrentObject\Attributes\TextData2=""
	CurrentObject\Attributes\TextData3=""
	CurrentObject\Attributes\Talkable=0
	CurrentObject\Attributes\CurrentAnim=0
	CurrentObject\Attributes\StandardAnim=0
	CurrentObject\Position\TileX=0
	CurrentObject\Position\TileY=0
	CurrentObject\Position\TileX2=0
	CurrentObject\Position\TileY2=0
	CurrentObject\Attributes\MovementTimer=0
	CurrentObject\Attributes\MovementSpeed=0
	CurrentObject\Attributes\MoveXGoal=0
	CurrentObject\Attributes\MoveYGoal=0
	CurrentObject\Attributes\TileTypeCollision=0
	CurrentObject\Attributes\ObjectTypeCollision=0
	CurrentObject\Attributes\Caged=0
	CurrentObject\Attributes\Dead=0
	CurrentObject\Attributes\DeadTimer=0
	CurrentObject\Attributes\Exclamation=0
	CurrentObject\Attributes\Shadow=-1
	CurrentObject\Attributes\Linked=-1
	CurrentObject\Attributes\LinkBack=-1
	CurrentObject\Attributes\Flying=0
	CurrentObject\Attributes\Frozen=0
	CurrentObject\Attributes\Indigo=0
	CurrentObject\Attributes\FutureInt24=0
	CurrentObject\Attributes\FutureInt25=0

	CurrentObject\Attributes\ScaleAdjust=1.0
	CurrentObject\Attributes\ScaleXAdjust=1.0
	CurrentObject\Attributes\ScaleYAdjust=1.0
	CurrentObject\Attributes\ScaleZAdjust=1.0
	CurrentObject\Attributes\FutureFloat5=0.0
	CurrentObject\Attributes\FutureFloat6=0.0
	CurrentObject\Attributes\FutureFloat7=0.0
	CurrentObject\Attributes\FutureFloat8=0.0
	CurrentObject\Attributes\FutureFloat9=0.0
	CurrentObject\Attributes\FutureFloat10=0.0
	CurrentObject\Attributes\FutureString1$=""
	CurrentObject\Attributes\FutureString2$=""

End Function

Function LoadObjectPreset()

	Filename$="Data\Editor\ObjectPresets\"+ObjectPresetCategoryName$(CurrentObjectPresetCategory)+"\"+ObjectPresetObjectName$(CurrentObjectPresetObject)

	file=ReadFile(filename$)

	CurrentObject\Attributes\ModelName$=ReadString$(file)
	CurrentObject\Attributes\TexName$=ReadString$(file)
	CurrentObject\Attributes\XScale#=ReadFloat(file)
	CurrentObject\Attributes\YScale#=ReadFloat(file)
	CurrentObject\Attributes\ZScale#=ReadFloat(file)
	CurrentObject\Attributes\XAdjust#=ReadFloat(file)
	CurrentObject\Attributes\YAdjust#=ReadFloat(file)
	CurrentObject\Attributes\ZAdjust#=ReadFloat(file)
	CurrentObject\Attributes\PitchAdjust#=ReadFloat(file)
	CurrentObject\Attributes\YawAdjust#=ReadFloat(file)
	CurrentObject\Attributes\RollAdjust#=ReadFloat(file)
	CurrentObject\Position\X#=ReadFloat(file)
	CurrentObject\Position\Y#=ReadFloat(file)
	CurrentObject\Position\Z#=ReadFloat(file)
	CurrentObject\Position\OldX#=ReadFloat(file)
	CurrentObject\Position\OldY#=ReadFloat(file)
	CurrentObject\Position\OldZ#=ReadFloat(file)
	CurrentObject\Attributes\DX#=ReadFloat(file)
	CurrentObject\Attributes\DY#=ReadFloat(file)
	CurrentObject\Attributes\DZ#=ReadFloat(file)
	CurrentObject\Attributes\Pitch#=ReadFloat(file)
	CurrentObject\Attributes\Yaw#=ReadFloat(file)
	CurrentObject\Attributes\Roll#=ReadFloat(file)
	CurrentObject\Attributes\Pitch2#=ReadFloat(file)
	CurrentObject\Attributes\Yaw2#=ReadFloat(file)
	CurrentObject\Attributes\Roll2#=ReadFloat(file)
	CurrentObject\Attributes\XGoal#=ReadFloat(file)
	CurrentObject\Attributes\YGoal#=ReadFloat(file)
	CurrentObject\Attributes\ZGoal#=ReadFloat(file)
	CurrentObject\Attributes\MovementType=ReadInt(file)
	CurrentObject\Attributes\MovementTypeData=ReadInt(file)
	CurrentObject\Attributes\Speed#=ReadFloat(file)
	CurrentObject\Attributes\Radius#=ReadFloat(file)
	CurrentObject\Attributes\RadiusType=ReadInt(file)
	;CurrentObject\Attributes\Data10=ReadInt(file)
	ReadInt(file)
	CurrentObject\Attributes\Data10=-1
	CurrentObject\Attributes\PushDX#=ReadFloat(file)
	CurrentObject\Attributes\PushDY#=ReadFloat(file)
	CurrentObject\Attributes\AttackPower=ReadInt(file)
	CurrentObject\Attributes\DefensePower=ReadInt(file)
	CurrentObject\Attributes\DestructionType=ReadInt(file)
	CurrentObject\Attributes\ID=ReadInt(file)
	CurrentObject\Attributes\LogicType=ReadInt(file)
	CurrentObject\Attributes\LogicSubType=ReadInt(file)
	CurrentObject\Attributes\Active=ReadInt(file)
	CurrentObject\Attributes\LastActive=ReadInt(file)
	CurrentObject\Attributes\ActivationType=ReadInt(file)
	CurrentObject\Attributes\ActivationSpeed=ReadInt(file)
	CurrentObject\Attributes\Status=ReadInt(file)
	CurrentObject\Attributes\Timer=ReadInt(file)
	CurrentObject\Attributes\TimerMax1=ReadInt(file)
	CurrentObject\Attributes\TimerMax2=ReadInt(file)
	CurrentObject\Attributes\Teleportable=ReadInt(file)
	CurrentObject\Attributes\ButtonPush=ReadInt(file)
	CurrentObject\Attributes\WaterReact=ReadInt(file)
	CurrentObject\Attributes\Telekinesisable=ReadInt(file)
	CurrentObject\Attributes\Freezable=ReadInt(file)
	CurrentObject\Attributes\Reactive=ReadInt(file)
	CurrentObject\Attributes\Child=ReadInt(file)
	CurrentObject\Attributes\Parent=ReadInt(file)
	CurrentObject\Attributes\Data0=ReadInt(file)
	CurrentObject\Attributes\Data1=ReadInt(file)
	CurrentObject\Attributes\Data2=ReadInt(file)
	CurrentObject\Attributes\Data3=ReadInt(file)
	CurrentObject\Attributes\Data4=ReadInt(file)
	CurrentObject\Attributes\Data5=ReadInt(file)
	CurrentObject\Attributes\Data6=ReadInt(file)
	CurrentObject\Attributes\Data7=ReadInt(file)
	CurrentObject\Attributes\Data8=ReadInt(file)
	CurrentObject\Attributes\Data9=ReadInt(file)
	CurrentObject\Attributes\TextData0$=ReadString$(file)
	CurrentObject\Attributes\TextData1$=ReadString$(file)
	CurrentObject\Attributes\TextData2$=ReadString$(file)
	CurrentObject\Attributes\TextData3$=ReadString$(file)
	CurrentObject\Attributes\Talkable=ReadInt(file)
	CurrentObject\Attributes\CurrentAnim=ReadInt(file)
	CurrentObject\Attributes\StandardAnim=ReadInt(file)
	CurrentObject\Position\TileX=ReadInt(file)
	CurrentObject\Position\TileY=ReadInt(file)
	CurrentObject\Position\TileX2=ReadInt(file)
	CurrentObject\Position\TileY2=ReadInt(file)
	CurrentObject\Attributes\MovementTimer=ReadInt(file)
	CurrentObject\Attributes\MovementSpeed=ReadInt(file)
	CurrentObject\Attributes\MoveXGoal=ReadInt(file)
	CurrentObject\Attributes\MoveYGoal=ReadInt(file)
	CurrentObject\Attributes\TileTypeCollision=ReadInt(file)
	CurrentObject\Attributes\ObjectTypeCollision=ReadInt(file)
	CurrentObject\Attributes\Caged=ReadInt(file)
	CurrentObject\Attributes\Dead=ReadInt(file)
	CurrentObject\Attributes\DeadTimer=ReadInt(file)
	CurrentObject\Attributes\Exclamation=ReadInt(file)
	CurrentObject\Attributes\Shadow=ReadInt(file)
	;CurrentObject\Attributes\Linked=ReadInt(file)
	ReadInt(file)
	CurrentObject\Attributes\Linked=-1
	;CurrentObject\Attributes\LinkBack=ReadInt(file)
	ReadInt(file)
	CurrentObject\Attributes\LinkBack=-1
	CurrentObject\Attributes\Flying=ReadInt(file)
	CurrentObject\Attributes\Frozen=ReadInt(file)
	CurrentObject\Attributes\Indigo=ReadInt(file)
	CurrentObject\Attributes\FutureInt24=ReadInt(file)
	CurrentObject\Attributes\FutureInt25=ReadInt(file)

	CurrentObject\Attributes\ScaleAdjust=ReadFloat(file)
	If CurrentObject\Attributes\ScaleAdjust=0.0
		CurrentObject\Attributes\ScaleAdjust=1.0
	EndIf

	CurrentObject\Attributes\ScaleXAdjust=ReadFloat(file)
	CurrentObject\Attributes\ScaleYAdjust=ReadFloat(file)
	CurrentObject\Attributes\ScaleZAdjust=ReadFloat(file)
	CurrentObject\Attributes\ScaleXAdjust=1.0
	CurrentObject\Attributes\ScaleYAdjust=1.0
	CurrentObject\Attributes\ScaleZAdjust=1.0
	CurrentObject\Attributes\FutureFloat5=ReadFloat(file)
	CurrentObject\Attributes\FutureFloat6=ReadFloat(file)
	CurrentObject\Attributes\FutureFloat7=ReadFloat(file)
	CurrentObject\Attributes\FutureFloat8=ReadFloat(file)
	CurrentObject\Attributes\FutureFloat9=ReadFloat(file)
	CurrentObject\Attributes\FutureFloat10=ReadFloat(file)
	CurrentObject\Attributes\FutureString1$=ReadString(file)
	CurrentObject\Attributes\FutureString2$=ReadString(file)

	NofObjectAdjusters=0

	; this line can be commented out now that all object adjusters are the same
	;ObjectAdjusterStart=0

	NofWopAdjusters=0

	For i=0 To 30
		If Eof(file)=False
		;	ObjectAdjuster$(i)=ReadString$(file)
		;	NofObjectAdjusters=NofObjectAdjusters+1
			ObjectAdjusterWop$(i)=ReadString$(file)
			NofWopAdjusters=NofWopAdjusters+1
		;Else
		;	ObjectAdjuster$(i)=""
		EndIf
	Next

	; Add these adjusters to every object.

	AddAdjuster("ID")
	AddAdjuster("Active")
	AddAdjuster("ActivationType")
	AddAdjuster("ActivationSpeed")
	AddAdjuster("Type")
	AddAdjuster("SubType")
	AddAdjuster("ModelName")
	AddAdjuster("TextureName")
	AddAdjuster("ScaleAdjust")

	;AddAdjuster("X")
	;AddAdjuster("Y")
	;AddAdjuster("Z")

	AddAdjuster("XAdjust")
	AddAdjuster("YAdjust")
	AddAdjuster("ZAdjust")
	AddAdjuster("YawAdjust")
	AddAdjuster("RollAdjust")
	AddAdjuster("PitchAdjust")
	AddAdjuster("XScale")
	AddAdjuster("YScale")
	AddAdjuster("ZScale")

	AddAdjuster("Data0")
	AddAdjuster("Data1")
	AddAdjuster("Data2")
	AddAdjuster("Data3")
	AddAdjuster("Data4")
	AddAdjuster("Data5")
	AddAdjuster("Data6")
	AddAdjuster("Data7")
	AddAdjuster("Data8")

	AddAdjuster("Data9")
	AddAdjuster("TextData0")
	AddAdjuster("TextData1")
	AddAdjuster("Talkable")
	AddAdjuster("Exclamation")
	AddAdjuster("Timer")
	AddAdjuster("TimerMax1")
	AddAdjuster("TimerMax2")
	AddAdjuster("DefensePower")

	AddAdjuster("Z")
	AddAdjuster("Flying")
	;AddAdjuster("DX")
	;AddAdjuster("DY")
	AddAdjuster("Frozen")
	AddAdjuster("Teleportable")
	AddAdjuster("ButtonPush")
	AddAdjuster("MovementSpeed")
	AddAdjuster("MovementType")
	;AddAdjuster("MovementTypeData")
	;AddAdjuster("MovementTimer")
	AddAdjuster("TileTypeCollision")
	AddAdjuster("ObjectTypeCollision")

	AddAdjuster("Linked")
	AddAdjuster("LinkBack")
	;AddAdjuster("Parent")
	;AddAdjuster("Child")
	AddAdjuster("MoveXGoal")
	AddAdjuster("MoveYGoal")
	AddAdjuster("Dead")
	AddAdjuster("Caged")
	AddAdjuster("Indigo")
	AddAdjuster("DestructionType")
	;AddAdjuster("Flying")
	;AddAdjuster("MovementTimer")
	AddAdjuster("Status")

	CloseFile file

	MakeAllObjectAdjustersAbsolute()

	BuildCurrentObjectModel()
	;SetBrushToCurrentObject()
	CurrentObjectWasChanged()

End Function

Function NextObjectAdjusterPage()

	If ObjectAdjusterStart+9<NofObjectAdjusters
	ObjectAdjusterStart=ObjectAdjusterStart+9
	Else
		ObjectAdjusterStart=0
	EndIf

End Function

Function PreviousObjectAdjusterPage()

	If ObjectAdjusterStart=0
		ObjectAdjusterStart=((NofObjectAdjusters-1)/9)*9
	Else
		ObjectAdjusterStart=ObjectAdjusterStart-9
	EndIf

End Function

Function AddAdjuster(Name$)

	ObjectAdjuster$(NofObjectAdjusters)=Name$
	NofObjectAdjusters=NofObjectAdjusters+1

End Function

Function PrintMessageForInstant(Message$)

	Locate 0,0
	Color 0,0,0
	Rect 0,0,500,40,True
	Color 255,255,255
	Print message$

End Function

Function ShowMessage(message$, milliseconds)

	PrintMessageForInstant(message$)
	Delay milliseconds

End Function

; for preventing several of the same message from pausing the same frame for a long time
Function ShowMessageOnce(message$, milliseconds)

	If ShowingError=False
		ShowingError=True ; will reset at the start of every frame
		ShowMessage(message$, milliseconds)
	EndIf

End Function

Function ShowLevelEditorWarning(Message$)

	StartX=LevelViewportWidth/2
	StartY=LevelViewportHeight/2

	TextOffset=GetCenteredTextOffset(Message$)
	Color RectToolbarR,RectToolbarG,RectToolbarB
	Rect StartX-TextOffset-2,StartY-10,TextOffset*2+4,40,True
	Color TextWarningR,TextWarningG,TextWarningB
	CenteredText(StartX,StartY-6,"!!! WARNING !!!")
	Text StartX-TextOffset,StartY+10,Message$

End Function

Function GetObjectOffset#(Attributes.GameObjectAttributes,index)

	; Type-specific placements
	If Attributes\LogicType=10 And Attributes\LogicSubType=1 ; house-door
		If Attributes\YawAdjust=90
			xoffset#=0.5
			yoffset#=1.0
		Else If Attributes\YawAdjust=270
			xoffset#=0.5
			yoffset#=0.0
		Else If Attributes\YawAdjust=45
			xoffset#=-0.1
			yoffset#=0.6
		Else If Attributes\YawAdjust=315
			xoffset#=0.40
			yoffset#=-0.1

		Else
			xoffset#=-0.00
			yoffset#=0.5

		EndIf
	Else If Attributes\LogicType=10 And Attributes\LogicSubType=2 ; dungeon-door
		If Attributes\YawAdjust=0
			xoffset#=0.0
			yoffset#=1.0
		Else If Attributes\YawAdjust=90
			xoffset#=1.0
			yoffset#=1.0
		Else If Attributes\YawAdjust=180
			xoffset#=1.0
			yoffset#=0.0
		Else
			xoffset#=0.0
			yoffset#=0.0

		EndIf
	Else If Attributes\LogicType=10 And Attributes\LogicSubType=3 ; townhouse1-door
		If Attributes\YawAdjust=90
			xoffset#=0.6
			yoffset#=1.0
		Else If Attributes\YawAdjust=270
			xoffset#=+0.40
			yoffset#=0.0
		Else If Attributes\YawAdjust=45
			xoffset#=-0.338
			yoffset#=0.342
		Else If Attributes\YawAdjust=315
			xoffset#=0.637
			yoffset#=-0.361

		Else
			xoffset#=-0.00
			yoffset#=0.6

		EndIf

	Else If Attributes\LogicType=10 And Attributes\LogicSubType=4 ; townhouse2-door
		If Attributes\YawAdjust=90
			xoffset#=0.1
			yoffset#=1.0
		Else If Attributes\YawAdjust=270
			xoffset#=0.90
			yoffset#=0.0
		Else If Attributes\YawAdjust=45
			xoffset#=-0.338-.35
			yoffset#=0.342-.35
		Else If Attributes\YawAdjust=315
			xoffset#=0.637+.35
			yoffset#=-0.361-.35

		Else
			xoffset#=0.00
			yoffset#=0.1

		EndIf

	Else
		xoffset#=0.5
		yoffset#=0.5
	EndIf

	If index=0
		Return xoffset#
	Else
		Return yoffset#
	EndIf

End Function

Function IsPositionInLevel(x,y)

	Return x>=0 And y>=0 And x<LevelWidth And y<LevelHeight

End Function

Function IsPositionInLevelArrayBounds(x,y)

	Return x>=0 And y>=0 And x<=100 And y<=100

End Function

Function SetObjectPosition(i,x#,y#)

	floorx=Floor(x)
	floory=Floor(y)

	SetObjectTileXY(i,floorx,floory)

	xoffset#=GetObjectOffset#(LevelObjects(i)\Attributes,0)
	yoffset#=GetObjectOffset#(LevelObjects(i)\Attributes,1)

	LevelObjects(i)\Position\X#=x#+xoffset#
	LevelObjects(i)\Position\Y#=y#+yoffset#

	Return True

End Function

Function PlaceObjectOrChangeLevelTile(x,y)

	If EditorMode=0
		ChangeLevelTile(x,y,True)
	ElseIf EditorMode=3
		PlaceObject(x,y)
	EndIf

End Function

Function PlaceObject(x#,y#)

	If Not PassesPlacementDensityTest()
		Return
	EndIf

	BrushSpaceX=LevelSpaceToBrushSpaceX(x,BrushWrap)
	BrushSpaceY=LevelSpaceToBrushSpaceY(y,BrushWrap)

	PlaceObjectActual(x#,y#,BrushSpaceX,BrushSpaceY)

	If DupeMode=DupeModeX
		TargetX#=MirrorAcrossFloat#(x#,MirrorPositionX)
		If TargetX#<>x#
			PlaceObjectActual(TargetX#,y#,BrushSpaceX,BrushSpaceY)
		EndIf
	ElseIf DupeMode=DupeModeY
		TargetY#=MirrorAcrossFloat#(y#,MirrorPositionY)
		If TargetY#<>y#
			PlaceObjectActual(x#,LevelHeight-1-y#,BrushSpaceX,BrushSpaceY)
		EndIf
	ElseIf DupeMode=DupeModeXPlusY
		TargetX#=MirrorAcrossFloat#(x#,MirrorPositionX)
		TargetY#=MirrorAcrossFloat#(y#,MirrorPositionY)
		If TargetX#<>x#
			PlaceObjectActual(TargetX#,y#,BrushSpaceX,BrushSpaceY)
		EndIf
		If TargetY#<>y#
			PlaceObjectActual(x#,TargetY#,BrushSpaceX,BrushSpaceY)
		EndIf
		If TargetX#<>x# And TargetY#<>y#
			PlaceObjectActual(TargetX#,TargetY#,BrushSpaceX,BrushSpaceY)
		EndIf
	EndIf

End Function

Function PlaceObjectActual(x#,y#,BrushSpaceX,BrushSpaceY)

	For k=0 To NofBrushObjects-1
		If BrushObjectTileXOffset(k)=BrushSpaceX And BrushObjectTileYOffset(k)=BrushSpaceY
			CopyObjectFromBrush(k,TempObject)
			PlaceThisObject(x,y,TempObject)
		EndIf
	Next

End Function

Function PlaceThisObject(x#,y#,SourceObject.GameObject)

	If NofObjects>=MaxNofObjects
		ShowMessageOnce(MaxNofObjects+" object limit reached; refusing to place any more", 1000)
		Return
	EndIf

	If PreventPlacingObjectsOutsideLevel And (Not IsPositionInLevelArrayBounds(Floor(x#),Floor(y#)))
		Return
	EndIf

	SourceAttributes.GameObjectAttributes=SourceObject\Attributes
	SourcePosition.GameObjectPosition=SourceObject\Position

	RandomizeObjectData(SourceObject)

	NewObject.GameObject=LevelObjects(NofObjects)

	CopyObjectAttributes(SourceAttributes,NewObject\Attributes)
	CopyObjectPosition(SourcePosition,NewObject\Position)

	SetObjectPosition(NofObjects,x#,y#)

	NewObject\Model\HatEntity=0
	NewObject\Model\HatTexture=0
	NewObject\Model\AccEntity=0
	NewObject\Model\AccTexture=0

	NewObject\Position\OldX#=-999
	NewObject\Position\OldY#=-999
	NewObject\Position\OldZ#=-999

	;For i=0 To 30
	;	ObjectAdjusterString$(NofObjects,i)=ObjectAdjuster$(i)
	;Next

	ThisObject=NofObjects
	SetNofObjects(NofObjects+1)

	BuildLevelObjectModel(ThisObject)

	CreateObjectPositionMarker(ThisObject)

	AddOrToggleSelectObject(ThisObject)

End Function

Function SetNofObjects(NewValue)

	NofObjects=NewValue

End Function

Function RandomizeObjectData(SourceObj.GameObject)

	SourceAttributes.GameObjectAttributes=SourceObj\Attributes
	SourcePosition.GameObjectPosition=SourceObj\Position

	If ObjectAdjusterLogicType\RandomEnabled
		SourceAttributes\LogicType=RandomObjectAdjusterInt(ObjectAdjusterLogicType)
	EndIf
	If ObjectAdjusterLogicSubType\RandomEnabled
		SourceAttributes\LogicSubType=RandomObjectAdjusterInt(ObjectAdjusterLogicSubType)
	EndIf

	If ObjectAdjusterPitchAdjust\RandomEnabled
		SourceAttributes\PitchAdjust#=RandomObjectAdjusterFloat#(ObjectAdjusterPitchAdjust)
	EndIf
	If ObjectAdjusterYawAdjust\RandomEnabled
		SourceAttributes\YawAdjust#=RandomObjectAdjusterFloat#(ObjectAdjusterYawAdjust)
	EndIf
	If ObjectAdjusterRollAdjust\RandomEnabled
		SourceAttributes\RollAdjust#=RandomObjectAdjusterFloat#(ObjectAdjusterRollAdjust)
	EndIf

	If ObjectAdjusterData0\RandomEnabled
		SourceAttributes\Data0=RandomObjectAdjusterInt(ObjectAdjusterData0)
	EndIf
	If ObjectAdjusterData1\RandomEnabled
		SourceAttributes\Data1=RandomObjectAdjusterInt(ObjectAdjusterData1)
	EndIf
	If ObjectAdjusterData2\RandomEnabled
		SourceAttributes\Data2=RandomObjectAdjusterInt(ObjectAdjusterData2)
	EndIf
	If ObjectAdjusterData3\RandomEnabled
		SourceAttributes\Data3=RandomObjectAdjusterInt(ObjectAdjusterData3)
	EndIf
	If ObjectAdjusterData4\RandomEnabled
		SourceAttributes\Data4=RandomObjectAdjusterInt(ObjectAdjusterData4)
	EndIf
	If ObjectAdjusterData5\RandomEnabled
		SourceAttributes\Data5=RandomObjectAdjusterInt(ObjectAdjusterData5)
	EndIf
	If ObjectAdjusterData6\RandomEnabled
		SourceAttributes\Data6=RandomObjectAdjusterInt(ObjectAdjusterData6)
	EndIf
	If ObjectAdjusterData7\RandomEnabled
		SourceAttributes\Data7=RandomObjectAdjusterInt(ObjectAdjusterData7)
	EndIf
	If ObjectAdjusterData8\RandomEnabled
		SourceAttributes\Data8=RandomObjectAdjusterInt(ObjectAdjusterData8)
	EndIf
	If ObjectAdjusterData9\RandomEnabled
		SourceAttributes\Data9=RandomObjectAdjusterInt(ObjectAdjusterData9)
	EndIf

	If ObjectAdjusterXScale\RandomEnabled
		SourceAttributes\XScale#=RandomObjectAdjusterFloat#(ObjectAdjusterXScale)
	EndIf
	If ObjectAdjusterYScale\RandomEnabled
		SourceAttributes\YScale#=RandomObjectAdjusterFloat#(ObjectAdjusterYScale)
	EndIf
	If ObjectAdjusterZScale\RandomEnabled
		SourceAttributes\ZScale#=RandomObjectAdjusterFloat#(ObjectAdjusterZScale)
	EndIf

	If ObjectAdjusterXAdjust\RandomEnabled
		SourceAttributes\XAdjust#=RandomObjectAdjusterFloat#(ObjectAdjusterXAdjust)
	EndIf
	If ObjectAdjusterYAdjust\RandomEnabled
		SourceAttributes\YAdjust#=RandomObjectAdjusterFloat#(ObjectAdjusterYAdjust)
	EndIf
	If ObjectAdjusterZAdjust\RandomEnabled
		SourceAttributes\ZAdjust#=RandomObjectAdjusterFloat#(ObjectAdjusterZAdjust)
	EndIf

	If ObjectAdjusterX\RandomEnabled
		SourcePosition\X#=RandomObjectAdjusterFloat#(ObjectAdjusterX)
	EndIf
	If ObjectAdjusterY\RandomEnabled
		SourcePosition\Y#=RandomObjectAdjusterFloat#(ObjectAdjusterY)
	EndIf
	If ObjectAdjusterZ\RandomEnabled
		SourcePosition\Z#=RandomObjectAdjusterFloat#(ObjectAdjusterZ)
	EndIf

	If ObjectAdjusterDX\RandomEnabled
		SourceAttributes\DX#=RandomObjectAdjusterFloat#(ObjectAdjusterDX)
	EndIf
	If ObjectAdjusterDY\RandomEnabled
		SourceAttributes\DY#=RandomObjectAdjusterFloat#(ObjectAdjusterDY)
	EndIf
	If ObjectAdjusterDZ\RandomEnabled
		SourceAttributes\DZ#=RandomObjectAdjusterFloat#(ObjectAdjusterDZ)
	EndIf

	If ObjectAdjusterMovementType\RandomEnabled
		SourceAttributes\MovementType=RandomObjectAdjusterInt(ObjectAdjusterMovementType)
	EndIf

	If ObjectAdjusterDefensePower\RandomEnabled
		SourceAttributes\DefensePower=RandomObjectAdjusterInt(ObjectAdjusterDefensePower)
	EndIf

	If ObjectAdjusterID\RandomEnabled
		SourceAttributes\ID=RandomObjectAdjusterInt(ObjectAdjusterID)
	EndIf

	If ObjectAdjusterActive\RandomEnabled
		SourceAttributes\Active=RandomObjectAdjusterInt(ObjectAdjusterActive)
	EndIf
	If ObjectAdjusterActivationType\RandomEnabled
		SourceAttributes\ActivationType=RandomObjectAdjusterInt(ObjectAdjusterActivationType)
	EndIf
	If ObjectAdjusterActivationSpeed\RandomEnabled
		SourceAttributes\ActivationSpeed=RandomObjectAdjusterInt(ObjectAdjusterActivationSpeed)
		; enforce even numbers
		If SourceAttributes\ActivationSpeed Mod 2=1
			SourceAttributes\ActivationSpeed=SourceAttributes\ActivationSpeed+1
		EndIf
	EndIf

	If ObjectAdjusterStatus\RandomEnabled
		SourceAttributes\Status=RandomObjectAdjusterInt(ObjectAdjusterStatus)
	EndIf

	If ObjectAdjusterTimer\RandomEnabled
		SourceAttributes\Timer=RandomObjectAdjusterInt(ObjectAdjusterTimer)
	EndIf
	If ObjectAdjusterTimerMax1\RandomEnabled
		SourceAttributes\TimerMax1=RandomObjectAdjusterInt(ObjectAdjusterTimerMax1)
	EndIf
	If ObjectAdjusterTimerMax2\RandomEnabled
		SourceAttributes\TimerMax2=RandomObjectAdjusterInt(ObjectAdjusterTimerMax2)
	EndIf

	If ObjectAdjusterTeleportable\RandomEnabled
		SourceAttributes\Teleportable=RandomObjectAdjusterInt(ObjectAdjusterTeleportable)
	EndIf
	If ObjectAdjusterButtonPush\RandomEnabled
		SourceAttributes\ButtonPush=RandomObjectAdjusterInt(ObjectAdjusterButtonPush)
	EndIf

	If ObjectAdjusterFlying\RandomEnabled
		SourceAttributes\Flying=RandomObjectAdjusterInt(ObjectAdjusterFlying)
	EndIf

	If ObjectAdjusterTalkable\RandomEnabled
		SourceAttributes\Talkable=RandomObjectAdjusterInt(ObjectAdjusterTalkable)
	EndIf

	If ObjectAdjusterMovementSpeed\RandomEnabled
		SourceAttributes\MovementSpeed=RandomObjectAdjusterInt(ObjectAdjusterMovementSpeed)
	EndIf

	If ObjectAdjusterMoveXGoal\RandomEnabled
		SourceAttributes\MoveXGoal=RandomObjectAdjusterInt(ObjectAdjusterMoveXGoal)
	EndIf
	If ObjectAdjusterMoveYGoal\RandomEnabled
		SourceAttributes\MoveYGoal=RandomObjectAdjusterInt(ObjectAdjusterMoveYGoal)
	EndIf

	If ObjectAdjusterTileTypeCollision\RandomEnabled
		SourceAttributes\TileTypeCollision=0
		For i=0 To 14
			If Rand(0,1)=0
				SourceAttributes\TileTypeCollision=SourceAttributes\TileTypeCollision+2^i
			EndIf
		Next
	EndIf
	If ObjectAdjusterObjectTypeCollision\RandomEnabled
		SourceAttributes\ObjectTypeCollision=0
		For i=1 To 10
			If Rand(0,1)=0
				SourceAttributes\ObjectTypeCollision=SourceAttributes\ObjectTypeCollision+2^i
			EndIf
		Next
	EndIf

	If ObjectAdjusterDead\RandomEnabled
		SourceAttributes\Dead=RandomObjectAdjusterInt(ObjectAdjusterDead)
	EndIf

	If ObjectAdjusterCaged\RandomEnabled
		SourceAttributes\Caged=RandomObjectAdjusterInt(ObjectAdjusterCaged)
	EndIf

	If ObjectAdjusterIndigo\RandomEnabled
		SourceAttributes\Indigo=RandomObjectAdjusterInt(ObjectAdjusterIndigo)
	EndIf

	If ObjectAdjusterFrozen\RandomEnabled
		SourceAttributes\Frozen=RandomObjectAdjusterInt(ObjectAdjusterFrozen)
	EndIf

	If ObjectAdjusterExclamation\RandomEnabled
		SourceAttributes\Exclamation=RandomObjectAdjusterInt(ObjectAdjusterExclamation)
	EndIf

	If ObjectAdjusterDestructionType\RandomEnabled
		SourceAttributes\DestructionType=RandomObjectAdjusterInt(ObjectAdjusterDestructionType)
	EndIf

	If ObjectAdjusterLinked\RandomEnabled
		SourceAttributes\Linked=RandomObjectAdjusterInt(ObjectAdjusterLinked)
	EndIf

	If ObjectAdjusterLinkBack\RandomEnabled
		SourceAttributes\LinkBack=RandomObjectAdjusterInt(ObjectAdjusterLinkBack)
	EndIf

	If ObjectAdjusterScaleAdjust\RandomEnabled
		SourceAttributes\ScaleAdjust#=RandomObjectAdjusterFloat#(ObjectAdjusterScaleAdjust)
	EndIf

End Function

Function CalculateEffectiveID(Attributes.GameObjectAttributes)

	Return CalculateEffectiveIDWith(Attributes\LogicType,Attributes\ID,Attributes\Data0,Attributes\Data1,Attributes\TileTypeCollision,Attributes\ModelName$)

End Function

Function CalculateEffectiveIDWith(TargetType,TargetID,Data0,Data1,TargetTileTypeCollision,ModelName$)

	Select TargetType
	Case 10,20,45,210,410,424 ; gate, fire trap, conveyor lead, transporter, flip bridge, laser gate
		If TargetID=-1
			Return 500+5*Data0+Data1
		EndIf
	Case 40 ; stepping stone
		If TargetID=-1
			Return 500+(8+Data0)*5+Data1
		EndIf
	Case 280,281 ; spring, suctube straight
		Return 500+5*Data0+Data1
	Case 301 ; rainbow float
		If TargetID=-1
			Return 500+(8+Data0)*5+Data1
		EndIf
	Case 432 ; moobot
		If TargetTileTypeCollision=0
			Return 500+Data0*5+Data1
		EndIf
	End Select

	If ModelName$="!Cage" Or ModelName$="!FlipBridge" Or ModelName$="!Spring" Or ModelName$="!ColourGate" Or ModelName$="!Transporter" Or ModelName$="!Teleport" Or ModelName$="!Suctube"
		If TargetID=-1
			Return 500+Data0*5+Data1
		EndIf
	EndIf
	If ModelName$="!SteppingStone"
		If TargetID=-1
			Return 500+(8+Data0)*5+Data1
		EndIf
	EndIf

	Return TargetID

End Function

Function ShouldBeInvisibleInGame(Attributes.GameObjectAttributes)

	If Attributes\ModelName$="!None" Or Attributes\ModelName$="!FloingOrb"
		Return True
	ElseIf Attributes\ModelName$="!Button" And (Attributes\LogicSubType=11 Or Attributes\LogicSubType>=32 Or Attributes\LogicSubType=15) ; NPC move, invisible buttons, general command
		Return True
	ElseIf Attributes\ModelName$="!IceBlock" And Attributes\Data3<>0 And Attributes\Data3<>1
		Return True
	; Q - player NPC functionality
	ElseIf Attributes\Active=0 And (IsModelNPC(Attributes\ModelName$) Or Attributes\LogicType=424) ; NPCs, OpenWA retro laser gates, player NPC
		Return True
	Else
		Return False
	EndIf

End Function

Function HideObjectModel(Model.GameObjectModel)

	If Model\Entity>0
		HideEntity Model\Entity
	EndIf
	If Model\HatEntity>0
		HideEntity Model\HatEntity
	EndIf
	If Model\AccEntity>0
		HideEntity Model\AccEntity
	EndIf

End Function

Function ShowObjectModel(Obj.GameObject)

	Model.GameObjectModel=Obj\Model

	If Model\Entity>0
		ShowEntity Model\Entity
	EndIf
	If Model\HatEntity>0
		ShowEntity Model\HatEntity
	EndIf
	If Model\AccEntity>0
		ShowEntity Model\AccEntity
	EndIf

	UpdateObjectAlpha(Obj)

End Function

Function IDFilterShouldHide(Attributes.GameObjectAttributes)

If IDFilterEnabled
	If IDFilterInverted=True
		Return IDFilterAllow=CalculateEffectiveID(Attributes)
	Else
		Return IDFilterAllow<>CalculateEffectiveID(Attributes)
	EndIf
Else
	Return False
EndIf

End Function

Function UpdateObjectVisibility(Obj.GameObject)

	If ShowObjectMesh=0 Or IDFilterShouldHide(Obj\Attributes)
		HideObjectModel(Obj\Model)
	Else
		If SimulationLevel>=2 And ShouldBeInvisibleInGame(Obj\Attributes)
			HideObjectModel(Obj\Model)
		Else
			ShowObjectModel(Obj)
		EndIf
	EndIf

End Function

Function SetEntityAlphaWithModelName(Entity,Alpha#,ModelName$)

	; Q - player NPC functionality
	If IsModelNPC(ModelName$) Or ModelName$="!Tentacle"
		Entity=GetChild(Entity,3)
	EndIf

	EntityAlpha Entity,Alpha#

End Function

Function UpdateObjectAlpha(Obj.GameObject)

	SetEntityAlphaWithModelName(Obj\Model\Entity,BaseObjectAlpha#(Obj\Attributes),Obj\Attributes\ModelName$)

End Function

Function BaseObjectAlpha#(Attributes.GameObjectAttributes)

	If Attributes\ModelName$="!FloingBubble"
		Return 0.5
	;ElseIf Attributes\ModelName$="!MagicMirror"
	;	Return 0.5
	ElseIf Attributes\ModelName$="!IceFloat"
		Return 0.8
	ElseIf Attributes\ModelName$="!PlantFloat"
		Return 0.7
	ElseIf Attributes\ModelName$="!Retrolasergate"
		Return 0.5
	ElseIf Attributes\ModelName$="!Teleport"
		Return 0.6
	ElseIf Attributes\ModelName$="!WaterFall"
		Return 0.7
	ElseIf Attributes\ModelName$="!IceBlock" And (Attributes\Data3=0 Or Attributes\Data3=1)
		Return 0.5
	ElseIf Attributes\ModelName$="!Conveyor" And Attributes\Data4=4
		Return 0.8
	Else
		Return 1.0
	EndIf

	; rainbow bubble alpha is set to 0.8 during gameplay/simulation

End Function

Function ObjectSumX#(Obj.GameObject)
	Return Obj\Position\X#+Obj\Attributes\XAdjust#
End Function

Function ObjectSumY#(Obj.GameObject)
	Return Obj\Position\Y#+Obj\Attributes\YAdjust#
End Function

Function ObjectSumZ#(Obj.GameObject)
	Return Obj\Position\Z#+Obj\Attributes\ZAdjust#
End Function

Function ObjectSumYaw#(Obj.GameObject)
	Return Obj\Attributes\Yaw#+Obj\Attributes\YawAdjust#
End Function

Function SetEntityPositionToWorldPosition(entity,XP#,YP#,ZP#,TargetType,Yaw#,XScale#,YScale#)

	If TargetType=230
		; adjustment for fireflower position (MS why did you put yourself through this??)
		xadjust#=-26.0
		yadjust#=0.0

		ScaleThingXCos#=XScale*xadjust*Cos(Yaw#)
		ScaleThingXSin#=XScale*xadjust*Sin(Yaw#)
		ScaleThingYCos#=YScale*yadjust*Cos(Yaw#)
		ScaleThingYSin#=YScale*yadjust*Sin(Yaw#)
		XP#=XP#+ScaleThingXCos#+ScaleThingYSin#
		YP#=YP#-ScaleThingXSin#+ScaleThingYCos#
	EndIf

	SetEntityPositionInWorld(entity,XP#,YP#,ZP#)

End Function

Function SetEntityPositionToObjectPosition(entity, Obj.GameObject)

	TheX#=ObjectSumX#(Obj)
	TheY#=ObjectSumY#(Obj)
	TheZ#=ObjectSumZ#(Obj)
	TheYaw#=ObjectSumYaw#(Obj)
	SetEntityPositionToWorldPosition(entity,TheX,TheY,TheZ,Obj\Attributes\LogicType,TheYaw#,Obj\Attributes\XScale,Obj\Attributes\YScale)

End Function

Function SetEntityPositionToObjectPositionWithoutZ(entity, Obj.GameObject, z#)

	SetEntityPositionInWorld(entity,ObjectSumX#(Obj),ObjectSumY#(Obj),z#)

End Function

Function SetEntityPositionInWorld(entity,x#,y#,z#)

	PositionEntity entity,x#,z#,-y#

End Function

Function UpdateObjectPosition(Dest)

	Obj.GameObject=LevelObjects(Dest)

	SetEntityPositionToObjectPosition(Obj\Model\Entity, Obj)

	;PositionEntity ObjectEntity(Dest),ObjectX(Dest)+ObjectXAdjust(Dest),ObjectZ(Dest)+ObjectZAdjust(Dest),-ObjectY(Dest)-ObjectYAdjust(Dest)

;	If ObjectHatEntity(Dest)>0
;		PositionEntity ObjectHatEntity(Dest),ObjectX(Dest)+ObjectXAdjust(Dest),ObjectZ(Dest)+ObjectZAdjust(Dest)+.1+.84*ObjectZScale(Dest)/.035,-ObjectY(Dest)-ObjectYAdjust(Dest)
;	EndIf

;	If ObjectAccEntity(Dest)>0
;		PositionEntity ObjectAccEntity(Dest),ObjectX(Dest)+ObjectXAdjust(Dest),ObjectZ(Dest)+ObjectZAdjust(Dest)+.1+.84*ObjectZScale(Dest)/.035,-ObjectY(Dest)-ObjectYAdjust(Dest)
;	EndIf

	If Obj\Model\HatEntity>0
		TransformAccessoryEntityOntoBone(Obj\Model\HatEntity,Obj\Model\Entity)
	EndIf
	If Obj\Model\AccEntity>0
		TransformAccessoryEntityOntoBone(Obj\Model\AccEntity,Obj\Model\Entity)
	EndIf

	PositionObjectPositionMarker(Dest)

	If IsObjectSelected(Dest)
		UpdateCurrentGrabbedObjectMarkerPosition(Dest)
	EndIf

End Function

Function UpdateObjectEntityToCurrent(Dest)

	Obj.GameObject=LevelObjects(Dest)
	Obj\Model\Entity=CopyEntity(CurrentObject\Model\Entity)

	UpdateObjectVisibility(Obj)

	If CurrentObject\Model\HatEntity>0

		Obj\Model\HatEntity=CreateAccEntity(CurrentObject\Attributes\Data2)
		Obj\Model\HatTexture=CreateHatTexture(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)

		ScaleEntity Obj\Model\HatEntity,CurrentObject\Attributes\YScale*CurrentObject\Attributes\ScaleAdjust,CurrentObject\Attributes\ZScale*CurrentObject\Attributes\ScaleAdjust,CurrentObject\Attributes\XScale*CurrentObject\Attributes\ScaleAdjust

		;RotateEntity ObjectHatEntity(Dest),0,0,0
		;TurnEntity ObjectHatEntity(Dest),CurrentObject\Attributes\PitchAdjust,0,CurrentObject\Attributes\RollAdjust
		;TurnEntity ObjectHatEntity(Dest),0,CurrentObject\Attributes\YawAdjust-90,0

		If Obj\Model\HatTexture=0
			EntityColor Obj\Model\HatEntity,ModelErrorR,ModelErrorG,ModelErrorB
		Else
			EntityTexture Obj\Model\HatEntity,Obj\Model\HatTexture
		EndIf
	EndIf

	If CurrentObject\Model\AccEntity>0
		Obj\Model\AccEntity=CreateAccEntity(CurrentObject\Attributes\Data4)
		Obj\Model\AccTexture=CreateGlassesTexture(CurrentObject\Attributes\Data4,CurrentObject\Attributes\Data5)

		ScaleEntity Obj\Model\AccEntity,CurrentObject\Attributes\YScale*CurrentObject\Attributes\ScaleAdjust,CurrentObject\Attributes\ZScale*CurrentObject\Attributes\ScaleAdjust,CurrentObject\Attributes\XScale*CurrentObject\Attributes\ScaleAdjust

		;RotateEntity ObjectAccEntity(Dest),0,0,0
		;TurnEntity ObjectAccEntity(Dest),CurrentObject\Attributes\PitchAdjust,0,CurrentObject\Attributes\RollAdjust
		;TurnEntity ObjectAccEntity(Dest),0,CurrentObject\Attributes\YawAdjust-90,0

		If Obj\Model\AccTexture=0
			EntityColor Obj\Model\AccEntity,ModelErrorR,ModelErrorG,ModelErrorB
		Else
			EntityTexture Obj\Model\AccEntity,Obj\Model\AccTexture
		EndIf
	EndIf

	UpdateObjectAnimation(Obj)

	UpdateObjectPosition(Dest)

End Function

Function UpdateObjectAnimation(Obj.GameObject)

	ModelName$=Obj\Attributes\ModelName$
	Entity=Obj\Model\Entity

	If ModelName$="!BabyBoomer"
		AnimateMD2 Entity,0,.2,1,2
	ElseIf ModelName$="!Busterfly"
		If SimulationLevel<SimulationLevelAnimation
			AnimateMD2 Entity,0
		Else
			AnimateMD2 Entity,2,.4,2,9
		EndIf
	ElseIf ModelName$="!Chomper"
		If SimulationLevel<SimulationLevelAnimation
			AnimateMD2 Entity,0
		Else
			AnimateMD2 Entity,1,.6,1,29
		EndIf
	ElseIf ModelName$="!Crab"
		If SimulationLevel<SimulationLevelAnimation
			AnimateMD2 Entity,0
		Else
			Select Obj\Attributes\Data1
			Case 2,3
				; asleep
				AnimateMD2 Entity,3,1,48,49
			End Select
		EndIf
	ElseIf ModelName$="!FireFlower"
		If SimulationLevel<SimulationLevelAnimation
			AnimateMD2 Entity,0
		EndIf
	ElseIf ModelName$="!Kaboom"
		If SimulationLevel<SimulationLevelAnimation
			AnimateMD2 Entity,0
		EndIf
	; Q - player NPC functionality
	ElseIf IsModelNPC(ModelName$)
		If SimulationLevel<SimulationLevelAnimation
			Animate GetChild(Entity,3),0
		Else
			Animate GetChild(Entity,3),1,.05,10
		EndIf
	ElseIf ModelName$="!StinkerWee"
		If SimulationLevel<SimulationLevelAnimation
			AnimateMD2 Entity,0
		EndIf
	ElseIf ModelName$="!Tentacle"
		If SimulationLevel<SimulationLevelAnimation
			Animate GetChild(Entity,3),0
		Else
			Animate GetChild(Entity,3),1,.1,1,0
		EndIf
	ElseIf Obj\Attributes\LogicType=290 And ModelName$="!Thwart"
		If SimulationLevel<SimulationLevelAnimation
			AnimateMD2 Entity,0
		Else
			AnimateMD2 Entity,2,0.005,81,82
		EndIf
	ElseIf ModelName$="!Troll"
		If SimulationLevel<SimulationLevelAnimation
			AnimateMD2 Entity,0
		Else
			AnimateMD2 Entity,2,0.005,81,82
		EndIf
	EndIf

End Function

; Returns True if the entity gets animated.
Function MaybeAnimate(Entity,mode=1,speed#=1,sequence=0,transition#=0)

	If SimulationLevel<SimulationLevelAnimation
		Return False
	Else
		Animate Entity,mode,speed#,sequence,transition#
		Return True
	EndIf

End Function

Function MaybeAnimateMD2(Entity,mode=1,speed#=1,FirstFrame=1,LastFrame=1,transition#=0)

	If SimulationLevel<SimulationLevelAnimation
		Return False
	Else
		AnimateMD2 Entity,mode,speed#,FirstFrame,LastFrame,transition#
		Return True
	EndIf

End Function

Function ObjectsWereChanged()

	ResetSimulatedQuantities()
	FinalizeCurrentObject()

	;AddUnsavedChange()

	NofObjectsInstantiated=NofObjects
	For i=0 To NofObjects-1
		NofObjectsInstantiated=NofObjectsInstantiated+ObjectCountExtraInstantiations(LevelObjects(i)\Attributes)
	Next

End Function

Function SomeTileWasChanged()

	;AddUnsavedChange()

End Function

Function TilesWereChanged()

	;AddUnsavedChange()

End Function

Function LightingWasChanged()

	SetLightNow(LightRed,LightGreen,LightBlue,AmbientRed,AmbientGreen,AmbientBlue)

End Function

Function ResetSimulatedQuantities()

	For i=0 To NofObjects-1
		Obj.GameObject=LevelObjects(i)
		SimulatedObjectXAdjust(i)=Obj\Attributes\XAdjust
		SimulatedObjectYAdjust(i)=Obj\Attributes\YAdjust
		SimulatedObjectZAdjust(i)=Obj\Attributes\ZAdjust
		SimulatedObjectX(i)=Obj\Position\X
		SimulatedObjectY(i)=Obj\Position\Y
		SimulatedObjectZ(i)=Obj\Position\Z
		SimulatedObjectYaw(i)=Obj\Attributes\Yaw
		SimulatedObjectPitch(i)=Obj\Attributes\Pitch
		SimulatedObjectRoll(i)=Obj\Attributes\Roll
		SimulatedObjectYawAdjust(i)=Obj\Attributes\YawAdjust
		SimulatedObjectPitchAdjust(i)=Obj\Attributes\PitchAdjust
		SimulatedObjectRollAdjust(i)=Obj\Attributes\RollAdjust
		SimulatedObjectYaw2(i)=Obj\Attributes\Yaw2
		SimulatedObjectPitch2(i)=Obj\Attributes\Pitch2
		SimulatedObjectRoll2(i)=Obj\Attributes\Roll2
		SimulatedObjectActive(i)=Obj\Attributes\Active
		SimulatedObjectLastActive(i)=Obj\Attributes\LastActive
		SimulatedObjectXScale(i)=Obj\Attributes\XScale
		SimulatedObjectYScale(i)=Obj\Attributes\YScale
		SimulatedObjectZScale(i)=Obj\Attributes\ZScale
		SimulatedObjectStatus(i)=Obj\Attributes\Status
		SimulatedObjectTimer(i)=Obj\Attributes\Timer

		SimulatedObjectData(i,0)=Obj\Attributes\Data0
		SimulatedObjectData(i,1)=Obj\Attributes\Data1
		SimulatedObjectData(i,2)=Obj\Attributes\Data2
		SimulatedObjectData(i,3)=Obj\Attributes\Data3
		SimulatedObjectData(i,4)=Obj\Attributes\Data4
		SimulatedObjectData(i,5)=Obj\Attributes\Data5
		SimulatedObjectData(i,6)=Obj\Attributes\Data6
		SimulatedObjectData(i,7)=Obj\Attributes\Data7
		SimulatedObjectData(i,8)=Obj\Attributes\Data8
		SimulatedObjectData(i,9)=Obj\Attributes\Data9

		SimulatedObjectCurrentAnim(i)=Obj\Attributes\CurrentAnim
		SimulatedObjectMovementSpeed(i)=Obj\Attributes\MovementSpeed
		SimulatedObjectMoveXGoal(i)=Obj\Attributes\MoveXGoal
		SimulatedObjectMoveYGoal(i)=Obj\Attributes\MoveYGoal
		SimulatedObjectData10(i)=Obj\Attributes\Data10
		SimulatedObjectSubType(i)=Obj\Attributes\LogicSubType
		SimulatedObjectTileTypeCollision(i)=Obj\Attributes\TileTypeCollision
		SimulatedObjectExclamation(i)=Obj\Attributes\Exclamation
		SimulatedObjectFrozen(i)=Obj\Attributes\Frozen
		If Obj\Attributes\ScaleAdjust<>0.0
			SimulatedObjectXScale(i)=SimulatedObjectXScale(i)*Obj\Attributes\ScaleAdjust
			SimulatedObjectYScale(i)=SimulatedObjectYScale(i)*Obj\Attributes\ScaleAdjust
			SimulatedObjectZScale(i)=SimulatedObjectZScale(i)*Obj\Attributes\ScaleAdjust
		EndIf
		SimulatedObjectScaleXAdjust(i)=Obj\Attributes\ScaleXAdjust
		SimulatedObjectScaleYAdjust(i)=Obj\Attributes\ScaleYAdjust
		SimulatedObjectScaleZAdjust(i)=Obj\Attributes\ScaleZAdjust

		; make sure flipbridges are scaled properly
		If Obj\Attributes\LogicType=410 Or Obj\Attributes\ModelName$="!FlipBridge"
			ControlFlipbridge(i)
		EndIf
	Next

	; This solves the flickering issue with objects that change scale in their control code.
	If SimulationLevel>=1
		ControlObjects()
	EndIf

End Function

Function SimulateObjectPosition(Dest)

	XP#=SimulatedObjectX(Dest)+SimulatedObjectXAdjust(Dest)
	YP#=SimulatedObjectY(Dest)+SimulatedObjectYAdjust(Dest)
	ZP#=SimulatedObjectZ(Dest)+SimulatedObjectZAdjust(Dest)

	Entity=LevelObjects(Dest)\Model\Entity
	TheType=LevelObjects(Dest)\Attributes\LogicType

	SetEntityPositionToWorldPosition(Entity,XP#,YP#,ZP#,TheType,SimulatedObjectYaw(Dest)+SimulatedObjectYawAdjust(Dest),SimulatedObjectXScale(Dest),SimulatedObjectYScale(Dest))

End Function

Function SimulateObjectRotation(Dest)

	Pitch#=SimulatedObjectPitch(Dest)+SimulatedObjectPitchAdjust(Dest)
	Roll#=SimulatedObjectRoll(Dest)+SimulatedObjectRollAdjust(Dest)
	Yaw#=SimulatedObjectYaw(Dest)+SimulatedObjectYawAdjust(Dest)

	Entity=LevelObjects(Dest)\Model\Entity
	GameLikeRotation(Entity,Yaw#,Pitch#,Roll#)
	TurnEntity Entity,SimulatedObjectPitch2(Dest),SimulatedObjectYaw2(Dest),SimulatedObjectRoll2(Dest)

	ModelName$=LevelObjects(Dest)\Attributes\ModelName$
	If ModelName$="!Troll" Or ModelName$="!Crab" Then TurnEntity Entity,0,-90,0
	If ModelName$="!Kaboom" Or ModelName$="!BabyBoomer" Then TurnEntity Entity,0,90,0

End Function

Function SimulateObjectScale(Dest)

	XS#=SimulatedObjectXScale(Dest)
	YS#=SimulatedObjectYScale(Dest)
	ZS#=SimulatedObjectZScale(Dest)

	Entity=LevelObjects(Dest)\Model\Entity
	ScaleEntity Entity,XS#,ZS#,YS#

End Function

Function GameLikeRotation(Entity,Yaw#,Pitch#,Roll#)

	RotateEntity Entity,0,0,0
	TurnEntity Entity,Pitch#,0,Roll#
	TurnEntity Entity,0,Yaw#,0

End Function

Function CopyObjectPosition(SourceAttributes.GameObjectPosition,DestAttributes.GameObjectPosition)

	; oldxyz is not grabbed
	DestAttributes\X#=SourceAttributes\X#
	DestAttributes\Y#=SourceAttributes\Y#
	DestAttributes\Z#=SourceAttributes\Z#
	DestAttributes\TileX=SourceAttributes\TileX
	DestAttributes\TileY=SourceAttributes\TileY
	DestAttributes\TileX2=SourceAttributes\TileX2
	DestAttributes\TileY2=SourceAttributes\TileY2

End Function

Function CopyObjectAttributes(SourceAttributes.GameObjectAttributes,DestAttributes.GameObjectAttributes)

	DestAttributes\ModelName$=SourceAttributes\ModelName$
	DestAttributes\TexName$=SourceAttributes\TexName$
	DestAttributes\XScale#=SourceAttributes\XScale#
	DestAttributes\ZScale#=SourceAttributes\ZScale#
	DestAttributes\YScale#=SourceAttributes\YScale#
	DestAttributes\XAdjust#=SourceAttributes\XAdjust#
	DestAttributes\ZAdjust#=SourceAttributes\ZAdjust#
	DestAttributes\YAdjust#=SourceAttributes\YAdjust#
	DestAttributes\PitchAdjust#=SourceAttributes\PitchAdjust#
	DestAttributes\YawAdjust#=SourceAttributes\YawAdjust#
	DestAttributes\RollAdjust#=SourceAttributes\RollAdjust#
	DestAttributes\DX#=SourceAttributes\DX#
	DestAttributes\DY#=SourceAttributes\DY#
	DestAttributes\DZ#=SourceAttributes\DZ#
	DestAttributes\Pitch#=SourceAttributes\Pitch#
	DestAttributes\Yaw#=SourceAttributes\Yaw#
	DestAttributes\Roll#=SourceAttributes\Roll#
	DestAttributes\Pitch2#=SourceAttributes\Pitch2#
	DestAttributes\Yaw2#=SourceAttributes\Yaw2#
	DestAttributes\Roll2#=SourceAttributes\Roll2#
	DestAttributes\XGoal#=SourceAttributes\XGoal#
	DestAttributes\YGoal#=SourceAttributes\YGoal#
	DestAttributes\ZGoal#=SourceAttributes\ZGoal#
	DestAttributes\MovementType=SourceAttributes\MovementType
	DestAttributes\MovementTypeData=SourceAttributes\MovementTypeData
	DestAttributes\Speed#=SourceAttributes\Speed#
	DestAttributes\Radius#=SourceAttributes\Radius#
	DestAttributes\RadiusType=SourceAttributes\RadiusType
	DestAttributes\Data10=SourceAttributes\Data10
	DestAttributes\PushDX#=SourceAttributes\PushDX#
	DestAttributes\PushDY#=SourceAttributes\PushDY#
	DestAttributes\AttackPower=SourceAttributes\AttackPower
	DestAttributes\DefensePower=SourceAttributes\DefensePower
	DestAttributes\DestructionType=SourceAttributes\DestructionType
	DestAttributes\ID=SourceAttributes\ID
	DestAttributes\LogicType=SourceAttributes\LogicType
	DestAttributes\LogicSubType=SourceAttributes\LogicSubType
	DestAttributes\Active=SourceAttributes\Active
	DestAttributes\LastActive=SourceAttributes\LastActive
	DestAttributes\ActivationType=SourceAttributes\ActivationType
	DestAttributes\ActivationSpeed=SourceAttributes\ActivationSpeed
	DestAttributes\Status=SourceAttributes\Status
	DestAttributes\Timer=SourceAttributes\Timer
	DestAttributes\TimerMax1=SourceAttributes\TimerMax1
	DestAttributes\TimerMax2=SourceAttributes\TimerMax2
	DestAttributes\Teleportable=SourceAttributes\Teleportable
	DestAttributes\ButtonPush=SourceAttributes\ButtonPush
	DestAttributes\WaterReact=SourceAttributes\WaterReact
	DestAttributes\Telekinesisable=SourceAttributes\Telekinesisable
	DestAttributes\Freezable=SourceAttributes\Freezable
	DestAttributes\Reactive=SourceAttributes\Reactive
	DestAttributes\Child=SourceAttributes\Child
	DestAttributes\Parent=SourceAttributes\Parent

	DestAttributes\Data0=SourceAttributes\Data0
	DestAttributes\Data1=SourceAttributes\Data1
	DestAttributes\Data2=SourceAttributes\Data2
	DestAttributes\Data3=SourceAttributes\Data3
	DestAttributes\Data4=SourceAttributes\Data4
	DestAttributes\Data5=SourceAttributes\Data5
	DestAttributes\Data6=SourceAttributes\Data6
	DestAttributes\Data7=SourceAttributes\Data7
	DestAttributes\Data8=SourceAttributes\Data8
	DestAttributes\Data9=SourceAttributes\Data9

	DestAttributes\TextData0$=SourceAttributes\TextData0$
	DestAttributes\TextData1$=SourceAttributes\TextData1$
	DestAttributes\TextData2$=SourceAttributes\TextData2$
	DestAttributes\TextData3$=SourceAttributes\TextData3$

	DestAttributes\Talkable=SourceAttributes\Talkable
	DestAttributes\CurrentAnim=SourceAttributes\CurrentAnim
	DestAttributes\StandardAnim=SourceAttributes\StandardAnim
	DestAttributes\MovementTimer=SourceAttributes\MovementTimer
	DestAttributes\MovementSpeed=SourceAttributes\MovementSpeed
	DestAttributes\MoveXGoal=SourceAttributes\MoveXGoal
	DestAttributes\MoveYGoal=SourceAttributes\MoveYGoal
	DestAttributes\TileTypeCollision=SourceAttributes\TileTypeCollision
	DestAttributes\ObjectTypeCollision=SourceAttributes\ObjectTypeCollision
	DestAttributes\Caged=SourceAttributes\Caged
	DestAttributes\Dead=SourceAttributes\Dead
	DestAttributes\DeadTimer=SourceAttributes\DeadTimer
	DestAttributes\Exclamation=SourceAttributes\Exclamation
	DestAttributes\Shadow=SourceAttributes\Shadow
	DestAttributes\Linked=SourceAttributes\Linked
	DestAttributes\LinkBack=SourceAttributes\LinkBack
	DestAttributes\Flying=SourceAttributes\Flying
	DestAttributes\Frozen=SourceAttributes\Frozen
	DestAttributes\Indigo=SourceAttributes\Indigo
	DestAttributes\FutureInt24=SourceAttributes\FutureInt24
	DestAttributes\FutureInt25=SourceAttributes\FutureInt25

	DestAttributes\ScaleAdjust=SourceAttributes\ScaleAdjust
	DestAttributes\ScaleXAdjust=SourceAttributes\ScaleXAdjust
	DestAttributes\ScaleYAdjust=SourceAttributes\ScaleYAdjust
	DestAttributes\ScaleZAdjust=SourceAttributes\ScaleZAdjust
	DestAttributes\FutureFloat5=SourceAttributes\FutureFloat5
	DestAttributes\FutureFloat6=SourceAttributes\FutureFloat6
	DestAttributes\FutureFloat7=SourceAttributes\FutureFloat7
	DestAttributes\FutureFloat8=SourceAttributes\FutureFloat8
	DestAttributes\FutureFloat9=SourceAttributes\FutureFloat9
	DestAttributes\FutureFloat10=SourceAttributes\FutureFloat10
	DestAttributes\FutureString1$=SourceAttributes\FutureString1$
	DestAttributes\FutureString2$=SourceAttributes\FutureString2$

End Function

Function ObjectIsAtInt(Obj.GameObject,x,y)

	MyX#=Obj\Position\X-GetObjectOffset#(Obj\Attributes,0)
	MyY#=Obj\Position\Y-GetObjectOffset#(Obj\Attributes,1)
	Return Floor(MyX#)=x And Floor(MyY#)=y

End Function

Function ObjectIsAtFloat(Obj.GameObject,x#,y#)

	Return ObjectIsAtInt(Obj,Floor(x),Floor(y))

End Function

Function GetFirstObjectAtFloat(x#,y#)

	For i=0 To NofObjects-1
		If ObjectIsAtFloat(LevelObjects(i),x#,y#)
			Return i
		EndIf
	Next
	Return -1

End Function

Function TryGrabObjectLoop(x#,y#,Target)
	For i=0 To NofObjects-1
		If ObjectIsAtFloat(LevelObjects(i),x#,y#) And i>Target
			AddOrToggleSelectObject(i)
			Return True
		EndIf
	Next
	Return False
End Function

Function GrabObject(x#,y#,SelectAllOnTile)

	If LevelTileObjectCount(Floor(x#),Floor(y#))=0
		Return
	EndIf

	If SelectAllOnTile
		For i=0 To NofObjects-1
			If ObjectIsAtFloat(LevelObjects(i),x#,y#)
				AddOrToggleSelectObject(i)
			EndIf
		Next
	Else
		Flag=TryGrabObjectLoop(x#,y#,PreviousSelectedObject)
		If Flag=False
			; restart the cycle
			Flag=TryGrabObjectLoop(x#,y#,-1)
;			If Flag=False
;				; no object found
;				Return
;			EndIf
		EndIf
	EndIf

End Function

Function ReadObjectIntoCurrentObject(Obj.GameObject)

	If ReadyToCopyFirstSelected=True
		ReadyToCopyFirstSelected=False

		NofWopAdjusters=0

		CopyObjectAttributes(Obj\Attributes,CurrentObject\Attributes)
		CopyObjectPosition(Obj\Position,CurrentObject\Position)

		CurrentObject\Position\X#=CurrentObject\Position\X#-x-0.5
		CurrentObject\Position\Y#=CurrentObject\Position\Y#-y-0.5

		;NofObjectAdjusters=0
		;ObjectAdjusterStart=0
		;For i=0 To 30
		;	ObjectAdjuster$(i)=ObjectAdjusterString$(Dest,i)
		;	If ObjectAdjuster$(i) <>""
		;		NofObjectAdjusters=NofObjectAdjusters+1
		;	EndIf
		;Next

		MakeAllObjectAdjustersAbsolute()

		;BuildCurrentObjectModel()
	Else
		CompareObjectToCurrent(Obj)
	EndIf

End Function

Function CopyObjectFromBrush(i,DestObject.GameObject)

	CopyObjectAttributes(BrushObjects(i)\Attributes,DestObject\Attributes)
	CopyObjectPosition(BrushObjects(i)\Position,DestObject\Position)

End Function

Function CreateObjectPositionMarker(i)

	ObjectPositionMarker(i)=CopyEntity(ObjectPositionMarkerMesh)
	EntityAlpha ObjectPositionMarker(i),.8
	PositionObjectPositionMarker(i)

	UpdateObjectPositionMarkersAtTile(LevelObjects(i)\Position\TileX,LevelObjects(i)\Position\TileY)

	If ShowObjectPositions=False
		HideEntity ObjectPositionMarker(i)
	EndIf

End Function

Function IncrementLevelTileObjectCount(x,y)

	LevelTileObjectCount(x,y)=LevelTileObjectCount(x,y)+1
	UpdateObjectPositionMarkersAtTile(x,y)

End Function

Function DecrementLevelTileObjectCount(x,y)

	LevelTileObjectCount(x,y)=LevelTileObjectCount(x,y)-1
	UpdateObjectPositionMarkersAtTile(x,y)

End Function

Function IncrementLevelTileObjectCountFor(Attributes.GameObjectPosition)

	IncrementLevelTileObjectCount(Attributes\TileX,Attributes\TileY)

End Function

Function DecrementLevelTileObjectCountFor(Attributes.GameObjectPosition)

	DecrementLevelTileObjectCount(Attributes\TileX,Attributes\TileY)

End Function

Function PositionObjectPositionMarker(i)

	Obj.GameObject=LevelObjects(i)
	PositionEntityInLevel(ObjectPositionMarker(i),Obj\Position\X,Obj\Position\Y)

End Function

Function PositionEntityInLevel(Entity,x#,y#)

	PositionEntity Entity,x#,0,-y#

End Function

Function UpdateObjectPositionMarkersAtTile(tilex,tiley)

	;ShowMessage("Updating object position markers...", 100)

	;LevelTileObjectCount(tilex,tiley)=0

	For i=0 To NofObjects-1
		If LevelObjects(i)\Position\TileX=tilex And LevelObjects(i)\Position\TileY=tiley
		;If ObjectIsAtInt(i,tilex,tiley)
			If LevelTileObjectCount(tilex,tiley)=1
				EntityColor ObjectPositionMarker(i),255,100,100
			ElseIf LevelTileObjectCount(tilex,tiley)>1
				EntityColor ObjectPositionMarker(i),255,255,100
			Else ; ERROR: LevelTileObjectCount is zero or less!
				EntityColor ObjectPositionMarker(i),255,100,255
			EndIf
		EndIf
	Next

	;ShowMessage("Update successful.", 100)

End Function

Function FreeObjectModel(Model.GameObjectModel)

	If Model\Entity>0
		FreeEntity Model\Entity
		Model\Entity=0
	EndIf
	If Model\Texture>0
		FreeTexture Model\Texture
		Model\Texture=0
	EndIf

	If Model\HatEntity>0
		FreeEntity Model\HatEntity
		Model\HatEntity=0
	EndIf
	If Model\AccEntity>0
		FreeEntity Model\AccEntity
		Model\AccEntity=0
	EndIf
	If Model\HatTexture>0
		FreeTexture Model\HatTexture
		Model\HatTexture=0
	EndIf
	If Model\AccTexture>0
		FreeTexture Model\AccTexture
		Model\AccTexture=0
	EndIf

End Function

Function FreeObject(i)

	Obj.GameObject=LevelObjects(i)

	FreeObjectModel(Obj\Model)

	tilex=Obj\Position\TileX
	tiley=Obj\Position\TileY
	LevelTileObjectCount(tilex,tiley)=LevelTileObjectCount(tilex,tiley)-1

	If ObjectPositionMarker(i)>0
		FreeEntity ObjectPositionMarker(i)
		ObjectPositionMarker(i)=0
	EndIf

End Function

; Move object indices around without deleting any of them.
Function SetObjectIndex(SourceIndex,TargetIndex)

	If TargetIndex<SourceIndex
		MoveObjectData(SourceIndex,1000) ; Move to temp.
		i=SourceIndex-1
		While i>=TargetIndex
			MoveObjectData(i,i+1)
			i=i-1
		Wend
		MoveObjectData(1000,TargetIndex)

		For j=0 To NofObjects-1
			Obj.GameObject=LevelObjects(j)

			If Obj\Attributes\Linked=SourceIndex
				Obj\Attributes\Linked=TargetIndex
			Else If Obj\Attributes\Linked>=TargetIndex And Obj\Attributes\Linked<SourceIndex
				Obj\Attributes\Linked=Obj\Attributes\Linked+1
			EndIf

			If Obj\Attributes\LinkBack=SourceIndex
				Obj\Attributes\LinkBack=TargetIndex
			Else If Obj\Attributes\LinkBack>=TargetIndex And Obj\Attributes\LinkBack<SourceIndex
				Obj\Attributes\LinkBack=Obj\Attributes\LinkBack+1
			EndIf

			If Obj\Attributes\Parent=SourceIndex
				Obj\Attributes\Parent=TargetIndex
			Else If Obj\Attributes\Parent>=TargetIndex And Obj\Attributes\Parent<SourceIndex
				Obj\Attributes\Parent=Obj\Attributes\Parent+1
			EndIf

			If Obj\Attributes\Child=SourceIndex
				Obj\Attributes\Child=TargetIndex
			Else If Obj\Attributes\Child>=TargetIndex And Obj\Attributes\Child<SourceIndex
				Obj\Attributes\Child=Obj\Attributes\Child+1
			EndIf
		Next

		ObjectsWereChanged()
		AddUnsavedChange()
	ElseIf TargetIndex>SourceIndex
		MoveObjectData(SourceIndex,1000) ; Move to temp.
		i=SourceIndex+1
		While i<=TargetIndex
			MoveObjectData(i,i-1)
			i=i+1
		Wend
		MoveObjectData(1000,TargetIndex)

		For j=0 To NofObjects-1
			Obj.GameObject=LevelObjects(j)

			If Obj\Attributes\Linked=SourceIndex
				Obj\Attributes\Linked=TargetIndex
			Else If Obj\Attributes\Linked>SourceIndex And Obj\Attributes\Linked<=TargetIndex
				Obj\Attributes\Linked=Obj\Attributes\Linked-1
			EndIf

			If Obj\Attributes\LinkBack=SourceIndex
				Obj\Attributes\LinkBack=TargetIndex
			Else If Obj\Attributes\LinkBack>SourceIndex And Obj\Attributes\LinkBack<=TargetIndex
				Obj\Attributes\LinkBack=Obj\Attributes\LinkBack-1
			EndIf

			If Obj\Attributes\Parent=SourceIndex
				Obj\Attributes\Parent=TargetIndex
			Else If Obj\Attributes\Parent>SourceIndex And Obj\Attributes\Parent<=TargetIndex
				Obj\Attributes\Parent=Obj\Attributes\Parent-1
			EndIf

			If Obj\Attributes\Child=SourceIndex
				Obj\Attributes\Child=TargetIndex
			Else If Obj\Attributes\Child>SourceIndex And Obj\Attributes\Child<=TargetIndex
				Obj\Attributes\Child=Obj\Attributes\Child-1
			EndIf
		Next

		ObjectsWereChanged()
		AddUnsavedChange()
	EndIf

End Function

Function DeleteObject(i)

	;ShowMessage("Deleting object "+i, 100)

	;DecrementLevelTileObjectCountFor(LevelObjects(i)\Position)

	tilex=LevelObjects(i)\Position\TileX
	tiley=LevelObjects(i)\Position\TileY

	FreeObject(i)

	If IsObjectSelected(i)
		RemoveSelectObject(i)
	EndIf
	For j=0 To NofSelectedObjects-1
		If i<SelectedObjects(j)
			SelectedObjects(j)=SelectedObjects(j)-1
		EndIf
	Next

	If IsObjectDragged(i)
		RemoveDraggedObject(i)
	EndIf
	For j=0 To NofDraggedObjects-1
		If i<DraggedObjects(j)
			DraggedObjects(j)=DraggedObjects(j)-1
		EndIf
	Next

	;ShowMessage("Moving object data...", 100)

	For j=i+1 To NofObjects-1
		MoveObjectData(j,j-1)
	Next

	;ShowMessage("Setting current grabbed object...", 100)

	SetNofObjects(NofObjects-1)

	For j=0 To NofObjects-1
		Obj.GameObject=LevelObjects(j)

		If Obj\Attributes\Linked=i
			Obj\Attributes\Linked=-1
		Else If Obj\Attributes\Linked>i
			Obj\Attributes\Linked=Obj\Attributes\Linked-1
		EndIf

		If Obj\Attributes\LinkBack=i
			Obj\Attributes\LinkBack=-1
		Else If Obj\Attributes\LinkBack>i
			Obj\Attributes\LinkBack=Obj\Attributes\LinkBack-1
		EndIf

		If Obj\Attributes\Parent=i
			Obj\Attributes\Parent=-1
		Else If Obj\Attributes\Parent>i
			Obj\Attributes\Parent=Obj\Attributes\Parent-1
		EndIf

		If Obj\Attributes\Child=i
			Obj\Attributes\Child=-1
		Else If Obj\Attributes\Child>i
			Obj\Attributes\Child=Obj\Attributes\Child-1
		EndIf
	Next

	If CurrentObject\Attributes\Linked=i
		CurrentObject\Attributes\Linked=-1
	Else If CurrentObject\Attributes\Linked>i
		CurrentObject\Attributes\Linked=CurrentObject\Attributes\Linked-1
	EndIf

	If CurrentObject\Attributes\LinkBack=i
		CurrentObject\Attributes\LinkBack=-1
	Else If CurrentObject\Attributes\LinkBack>i
		CurrentObject\Attributes\LinkBack=CurrentObject\Attributes\LinkBack-1
	EndIf

	If CurrentObject\Attributes\Parent=i
		CurrentObject\Attributes\Parent=-1
	Else If CurrentObject\Attributes\Parent>i
		CurrentObject\Attributes\Parent=CurrentObject\Attributes\Parent-1
	EndIf

	If CurrentObject\Attributes\Child=i
		CurrentObject\Attributes\Child=-1
	Else If CurrentObject\Attributes\Child>i
		CurrentObject\Attributes\Child=CurrentObject\Attributes\Child-1
	EndIf

	UpdateObjectPositionMarkersAtTile(tilex,tiley)

End Function

Function DeleteObjectAt(x,y)

	If Not PassesPlacementDensityTest()
		Return
	EndIf

	DeleteObjectAtActual(x,y)

	If DupeMode=DupeModeX
		TargetX=MirrorAcrossInt(x,MirrorPositionX)
		DeleteObjectAtActual(TargetX,y)
	ElseIf DupeMode=DupeModeY
		TargetY=MirrorAcrossInt(y,MirrorPositionY)
		DeleteObjectAtActual(x,TargetY)
	ElseIf DupeMode=DupeModeXPlusY
		TargetX=MirrorAcrossInt(x,MirrorPositionX)
		TargetY=MirrorAcrossInt(y,MirrorPositionY)
		DeleteObjectAtActual(TargetX,y)
		DeleteObjectAtActual(x,TargetY)
		DeleteObjectAtActual(TargetX,TargetY)
	EndIf

End Function

Function DeleteObjectAtActual(x,y)

	DeleteCount=0
	For i=0 To NofObjects-1
		Pos.GameObjectPosition=LevelObjects(i)\Position
		If Floor(Pos\X)=x And Floor(Pos\Y)=y
			DeleteObject(i)
			SetEditorMode(3)
			i=i-1
			DeleteCount=DeleteCount+1
		EndIf
	Next
	;ShowMessage(DeleteCount, 1000)
	Return DeleteCount

End Function

Function MoveObjectData(Source,Dest)

	ObjSource.GameObject=LevelObjects(Source)
	ObjDest.GameObject=LevelObjects(Dest)

	MoveObjectModel(ObjSource\Model,ObjDest\Model)

	CopyObjectAttributes(ObjSource\Attributes,ObjDest\Attributes)
	CopyObjectPosition(ObjSource\Position,ObjDest\Position)

	;For i=0 To 30
	;	ObjectAdjusterString$(Dest,i)=ObjectAdjusterString$(Source,i)
	;Next

	ObjectPositionMarker(Dest)=ObjectPositionMarker(Source)

End Function

Function MoveObjectModel(Source.GameObjectModel,Dest.GameObjectModel)

	Dest\Entity=Source\Entity
	Dest\Texture=Source\Texture
	Dest\HatEntity=Source\HatEntity
	Dest\AccEntity=Source\AccEntity
	Dest\HatTexture=Source\HatTexture
	Dest\AccTexture=Source\AccTexture
	; making sure there is no aliasing since that previously caused occasional MAVs
	Source\Entity=0
	Source\Texture=0
	Source\HatEntity=0
	Source\AccEntity=0
	Source\HatTexture=0
	Source\AccTexture=0

End Function

Function CopyObjectToBrush(Source.GameObject,Dest,XOffset,YOffset)

	;BrushObjectXOffset#(Dest)=XOffset#-0.5
	;BrushObjectYOffset#(Dest)=YOffset#-0.5
	BrushObjectTileXOffset(Dest)=XOffset
	BrushObjectTileYOffset(Dest)=YOffset

	;BrushObjects(Dest)\X=0.5 ;ObjectX(Source)
	;BrushObjects(Dest)\Y=0.5 ;ObjectY(Source)
	;BrushObjects(Dest)\Z=LevelObjects(Source)\Attributes\Z
	;BrushObjectTileX(Dest)=ObjectTileX(Source)
	;BrushObjectTileY(Dest)=ObjectTileY(Source)
	;BrushObjectTileX2(Dest)=ObjectTileX2(Source)
	;BrushObjectTileY2(Dest)=ObjectTileY2(Source)

	CopyObjectAttributes(Source\Attributes,BrushObjects(Dest)\Attributes)
	CopyObjectPosition(Source\Position,BrushObjects(Dest)\Position)
	CopyObjectModel(Source\Model,BrushObjects(Dest)\Model)
	HideObjectModel(BrushObjects(Dest)\Model)

End Function

Function CopySelectedObjectsToBrush()

	; set custom brush
	If EditorMode=3 And NofSelectedObjects<>0
		RecalculateSelectionSize()

		NofBrushObjects=NofSelectedObjects
		BrushSpaceWidth=SelectionMaxTileX-SelectionMinTileX+1
		BrushSpaceHeight=SelectionMaxTileY-SelectionMinTileY+1
		BrushSpaceOriginX=SelectionMinTileX+BrushSpaceWidth/2
		BrushSpaceOriginY=SelectionMinTileY+BrushSpaceHeight/2
		BrushWidth=BrushSpaceWidth
		BrushHeight=BrushSpaceHeight
		For i=0 To NofSelectedObjects-1
			LevelObject.GameObject=LevelObjects(SelectedObjects(i))
			BrushSpaceX=LevelSpaceToBrushSpaceX(LevelObject\Position\TileX,BrushWrapModulus)
			BrushSpaceY=LevelSpaceToBrushSpaceY(LevelObject\Position\TileY,BrushWrapModulus)
			CopyObjectToBrush(LevelObject,i,BrushSpaceX,BrushSpaceY)
		Next
		BrushCursorStateWasChanged()
	EndIf

End Function

Function CopyObjectModel(Source.GameObjectModel,Dest.GameObjectModel)

	FreeObjectModel(Dest)

	If Source\Entity>0
		Dest\Entity=CopyEntity(Source\Entity)
	EndIf
	If Source\Texture>0
		Dest\Texture=0 ;Source\Texture
	EndIf
	If Source\HatEntity>0
		Dest\HatEntity=CopyEntity(Source\HatEntity)
	EndIf
	If Source\AccEntity>0
		Dest\AccEntity=CopyEntity(Source\AccEntity)
	EndIf
	If Source\HatTexture>0
		Dest\HatTexture=0 ;Source\HatTexture
	EndIf
	If Source\AccTexture>0
		Dest\AccTexture=0 ;Source\AccTexture
	EndIf

End Function

Function UpdateSelectedObjects()

	SetEditorMode(3)
	For i=0 To NofSelectedObjects-1
		CurrentGrabbedObject=SelectedObjects(i)
		PasteObjectData(CurrentGrabbedObject)
	Next
	;If AreAllObjectAdjustersAbsolute() ; Allows reapplication of the same relative change.
	;	CurrentGrabbedObjectModified=False
	;EndIf

	; Zero all relative object adjusters.
	RecalculateObjectAdjusterModes()

	ObjectsWereChanged()
	AddUnsavedChange()

End Function

Function UpdateSelectedObjectsIfExists()

	If NofSelectedObjects<>0 And CurrentGrabbedObjectModified
		UpdateSelectedObjects()
	EndIf

End Function

Function PasteObjectData(Dest)

	;xy position is not changed

	;CopyObjectAttributes(CurrentObject\Attributes,LevelObjects(Dest)\Attributes)

	SourceObject.GameObject=CurrentObject
	DestObject.GameObject=LevelObjects(Dest)

	SourceAttributes.GameObjectAttributes=SourceObject\Attributes
	DestAttributes.GameObjectAttributes=DestObject\Attributes

	If ObjectAdjusterZ\Absolute
		DestObject\Position\Z=SourceObject\Position\Z
	EndIf

	If ObjectAdjusterModelName\Absolute
		DestAttributes\ModelName$=SourceAttributes\ModelName$
	EndIf
	If ObjectAdjusterTextureName\Absolute
		DestAttributes\TexName$=SourceAttributes\TexName$
	EndIf

	If ObjectAdjusterXScale\Absolute
		DestAttributes\XScale#=SourceAttributes\XScale#
	Else
		DestAttributes\XScale#=DestAttributes\XScale#+SourceAttributes\XScale#
	EndIf
	If ObjectAdjusterYScale\Absolute
		DestAttributes\YScale#=SourceAttributes\YScale#
	Else
		DestAttributes\YScale#=DestAttributes\YScale#+SourceAttributes\YScale#
	EndIf
	If ObjectAdjusterZScale\Absolute
		DestAttributes\ZScale#=SourceAttributes\ZScale#
	Else
		DestAttributes\ZScale#=DestAttributes\ZScale#+SourceAttributes\ZScale#
	EndIf

	If ObjectAdjusterXAdjust\Absolute
		DestAttributes\XAdjust#=SourceAttributes\XAdjust#
	Else
		DestAttributes\XAdjust#=DestAttributes\XAdjust#+SourceAttributes\XAdjust#
	EndIf
	If ObjectAdjusterYAdjust\Absolute
		DestAttributes\YAdjust#=SourceAttributes\YAdjust#
	Else
		DestAttributes\YAdjust#=DestAttributes\YAdjust#+SourceAttributes\YAdjust#
	EndIf
	If ObjectAdjusterZAdjust\Absolute
		DestAttributes\ZAdjust#=SourceAttributes\ZAdjust#
	Else
		DestAttributes\ZAdjust#=DestAttributes\ZAdjust#+SourceAttributes\ZAdjust#
	EndIf

	If ObjectAdjusterPitchAdjust\Absolute
		DestAttributes\PitchAdjust#=SourceAttributes\PitchAdjust#
	Else
		DestAttributes\PitchAdjust#=DestAttributes\PitchAdjust#+SourceAttributes\PitchAdjust#
	EndIf
	If ObjectAdjusterYawAdjust\Absolute
		DestAttributes\YawAdjust#=SourceAttributes\YawAdjust#
	Else
		DestAttributes\YawAdjust#=DestAttributes\YawAdjust#+SourceAttributes\YawAdjust#
	EndIf
	If ObjectAdjusterRollAdjust\Absolute
		DestAttributes\RollAdjust#=SourceAttributes\RollAdjust#
	Else
		DestAttributes\RollAdjust#=DestAttributes\RollAdjust#+SourceAttributes\RollAdjust#
	EndIf

	If ObjectAdjusterDX\Absolute
		DestAttributes\DX#=SourceAttributes\DX#
	Else
		DestAttributes\DX#=DestAttributes\DX#+SourceAttributes\DX#
	EndIf
	If ObjectAdjusterDY\Absolute
		DestAttributes\DY#=SourceAttributes\DY#
	Else
		DestAttributes\DY#=DestAttributes\DY#+SourceAttributes\DY#
	EndIf
	If ObjectAdjusterDZ\Absolute
		DestAttributes\DZ#=SourceAttributes\DZ#
	Else
		DestAttributes\DZ#=DestAttributes\DZ#+SourceAttributes\DZ#
	EndIf

	DestAttributes\Pitch#=SourceAttributes\Pitch#
	DestAttributes\Yaw#=SourceAttributes\Yaw#
	DestAttributes\Roll#=SourceAttributes\Roll#
	DestAttributes\Pitch2#=SourceAttributes\Pitch2#
	DestAttributes\Yaw2#=SourceAttributes\Yaw2#
	DestAttributes\Roll2#=SourceAttributes\Roll2#
	DestAttributes\XGoal#=SourceAttributes\XGoal#
	DestAttributes\YGoal#=SourceAttributes\YGoal#
	DestAttributes\ZGoal#=SourceAttributes\ZGoal#

	If ObjectAdjusterMovementType\Absolute
		DestAttributes\MovementType=SourceAttributes\MovementType
	Else
		DestAttributes\MovementType=DestAttributes\MovementType+SourceAttributes\MovementType
	EndIf
	If ObjectAdjusterMovementTypeData\Absolute
		DestAttributes\MovementTypeData=SourceAttributes\MovementTypeData
	Else
		DestAttributes\MovementTypeData=DestAttributes\MovementTypeData+SourceAttributes\MovementTypeData
	EndIf

	DestAttributes\Speed#=SourceAttributes\Speed#
	DestAttributes\Radius#=SourceAttributes\Radius#
	DestAttributes\RadiusType=SourceAttributes\RadiusType

	If ObjectAdjusterData10\Absolute
		DestAttributes\Data10=SourceAttributes\Data10
	Else
		DestAttributes\Data10=DestAttributes\Data10+SourceAttributes\Data10
	EndIf

	DestAttributes\PushDX#=SourceAttributes\PushDX#
	DestAttributes\PushDY#=SourceAttributes\PushDY#
	DestAttributes\AttackPower=SourceAttributes\AttackPower

	If ObjectAdjusterDefensePower\Absolute
		DestAttributes\DefensePower=SourceAttributes\DefensePower
	Else
		DestAttributes\DefensePower=DestAttributes\DefensePower+SourceAttributes\DefensePower
	EndIf
	If ObjectAdjusterDestructionType\Absolute
		DestAttributes\DestructionType=SourceAttributes\DestructionType
	Else
		DestAttributes\DestructionType=DestAttributes\DestructionType+SourceAttributes\DestructionType
	EndIf
	If ObjectAdjusterID\Absolute
		DestAttributes\ID=SourceAttributes\ID
	Else
		DestAttributes\ID=DestAttributes\ID+SourceAttributes\ID
	EndIf
	If ObjectAdjusterLogicType\Absolute
		DestAttributes\LogicType=SourceAttributes\LogicType
	Else
		DestAttributes\LogicType=DestAttributes\LogicType+SourceAttributes\LogicType
	EndIf
	If ObjectAdjusterLogicSubType\Absolute
		DestAttributes\LogicSubType=SourceAttributes\LogicSubType
	Else
		DestAttributes\LogicSubType=DestAttributes\LogicSubType+SourceAttributes\LogicSubType
	EndIf
	If ObjectAdjusterActive\Absolute
		DestAttributes\Active=SourceAttributes\Active
	Else
		DestAttributes\Active=DestAttributes\Active+SourceAttributes\Active
	EndIf

	DestAttributes\LastActive=SourceAttributes\LastActive

	If ObjectAdjusterActivationType\Absolute
		DestAttributes\ActivationType=SourceAttributes\ActivationType
	Else
		DestAttributes\ActivationType=DestAttributes\ActivationType+SourceAttributes\ActivationType
	EndIf
	If ObjectAdjusterActivationSpeed\Absolute
		DestAttributes\ActivationSpeed=SourceAttributes\ActivationSpeed
	Else
		DestAttributes\ActivationSpeed=DestAttributes\ActivationSpeed+SourceAttributes\ActivationSpeed
	EndIf
	If ObjectAdjusterStatus\Absolute
		DestAttributes\Status=SourceAttributes\Status
	Else
		DestAttributes\Status=DestAttributes\Status+SourceAttributes\Status
	EndIf
	If ObjectAdjusterTimer\Absolute
		DestAttributes\Timer=SourceAttributes\Timer
	Else
		DestAttributes\Timer=DestAttributes\Timer+SourceAttributes\Timer
	EndIf
	If ObjectAdjusterTimerMax1\Absolute
		DestAttributes\TimerMax1=SourceAttributes\TimerMax1
	Else
		DestAttributes\TimerMax1=DestAttributes\TimerMax1+SourceAttributes\TimerMax1
	EndIf
	If ObjectAdjusterTimerMax2\Absolute
		DestAttributes\TimerMax2=SourceAttributes\TimerMax2
	Else
		DestAttributes\TimerMax2=DestAttributes\TimerMax2+SourceAttributes\TimerMax2
	EndIf
	If ObjectAdjusterTeleportable\Absolute
		DestAttributes\Teleportable=SourceAttributes\Teleportable
	Else
		DestAttributes\Teleportable=DestAttributes\Teleportable+SourceAttributes\Teleportable
	EndIf
	If ObjectAdjusterButtonPush\Absolute
		DestAttributes\ButtonPush=SourceAttributes\ButtonPush
	Else
		DestAttributes\ButtonPush=DestAttributes\ButtonPush+SourceAttributes\ButtonPush
	EndIf
	If ObjectAdjusterWaterReact\Absolute
		DestAttributes\WaterReact=SourceAttributes\WaterReact
	Else
		DestAttributes\WaterReact=DestAttributes\WaterReact+SourceAttributes\WaterReact
	EndIf

	DestAttributes\Telekinesisable=SourceAttributes\Telekinesisable
	DestAttributes\Freezable=SourceAttributes\Freezable
	DestAttributes\Reactive=SourceAttributes\Reactive

	If ObjectAdjusterChild\Absolute
		DestAttributes\Child=SourceAttributes\Child
	Else
		DestAttributes\Child=DestAttributes\Child+SourceAttributes\Child
	EndIf
	If ObjectAdjusterParent\Absolute
		DestAttributes\Parent=SourceAttributes\Parent
	Else
		DestAttributes\Parent=DestAttributes\Parent+SourceAttributes\Parent
	EndIf

	If ObjectAdjusterData0\Absolute
		DestAttributes\Data0=SourceAttributes\Data0
	Else
		DestAttributes\Data0=DestAttributes\Data0+SourceAttributes\Data0
	EndIf
	If ObjectAdjusterData1\Absolute
		DestAttributes\Data1=SourceAttributes\Data1
	Else
		DestAttributes\Data1=DestAttributes\Data1+SourceAttributes\Data1
	EndIf
	If ObjectAdjusterData2\Absolute
		DestAttributes\Data2=SourceAttributes\Data2
	Else
		DestAttributes\Data2=DestAttributes\Data2+SourceAttributes\Data2
	EndIf
	If ObjectAdjusterData3\Absolute
		DestAttributes\Data3=SourceAttributes\Data3
	Else
		DestAttributes\Data3=DestAttributes\Data3+SourceAttributes\Data3
	EndIf
	If ObjectAdjusterData4\Absolute
		DestAttributes\Data4=SourceAttributes\Data4
	Else
		DestAttributes\Data4=DestAttributes\Data4+SourceAttributes\Data4
	EndIf
	If ObjectAdjusterData5\Absolute
		DestAttributes\Data5=SourceAttributes\Data5
	Else
		DestAttributes\Data5=DestAttributes\Data5+SourceAttributes\Data5
	EndIf
	If ObjectAdjusterData6\Absolute
		DestAttributes\Data6=SourceAttributes\Data6
	Else
		DestAttributes\Data6=DestAttributes\Data6+SourceAttributes\Data6
	EndIf
	If ObjectAdjusterData7\Absolute
		DestAttributes\Data7=SourceAttributes\Data7
	Else
		DestAttributes\Data7=DestAttributes\Data7+SourceAttributes\Data7
	EndIf
	If ObjectAdjusterData8\Absolute
		DestAttributes\Data8=SourceAttributes\Data8
	Else
		DestAttributes\Data8=DestAttributes\Data8+SourceAttributes\Data8
	EndIf
	If ObjectAdjusterData9\Absolute
		DestAttributes\Data9=SourceAttributes\Data9
	Else
		DestAttributes\Data9=DestAttributes\Data9+SourceAttributes\Data9
	EndIf

	If ObjectAdjusterTextData0\Absolute
		DestAttributes\TextData0$=SourceAttributes\TextData0$
	EndIf
	If ObjectAdjusterTextData1\Absolute
		DestAttributes\TextData1$=SourceAttributes\TextData1$
	EndIf

	DestAttributes\TextData2$=SourceAttributes\TextData2$
	DestAttributes\TextData3$=SourceAttributes\TextData3$

	If ObjectAdjusterTalkable\Absolute
		DestAttributes\Talkable=SourceAttributes\Talkable
	Else
		DestAttributes\Talkable=DestAttributes\Talkable+SourceAttributes\Talkable
	EndIf
	If ObjectAdjusterCurrentAnim\Absolute
		DestAttributes\CurrentAnim=SourceAttributes\CurrentAnim
	Else
		DestAttributes\CurrentAnim=DestAttributes\CurrentAnim+SourceAttributes\CurrentAnim
	EndIf
	If ObjectAdjusterStandardAnim\Absolute
		DestAttributes\StandardAnim=SourceAttributes\StandardAnim
	Else
		DestAttributes\StandardAnim=DestAttributes\StandardAnim+SourceAttributes\StandardAnim
	EndIf
	If ObjectAdjusterMovementTimer\Absolute
		DestAttributes\MovementTimer=SourceAttributes\MovementTimer
	Else
		DestAttributes\MovementTimer=DestAttributes\MovementTimer+SourceAttributes\MovementTimer
	EndIf
	If ObjectAdjusterMovementSpeed\Absolute
		DestAttributes\MovementSpeed=SourceAttributes\MovementSpeed
	Else
		DestAttributes\MovementSpeed=DestAttributes\MovementSpeed+SourceAttributes\MovementSpeed
	EndIf

	If ObjectAdjusterMoveXGoal\Absolute
		DestAttributes\MoveXGoal=SourceAttributes\MoveXGoal
	Else
		DestAttributes\MoveXGoal=DestAttributes\MoveXGoal+SourceAttributes\MoveXGoal
	EndIf
	If ObjectAdjusterMoveYGoal\Absolute
		DestAttributes\MoveYGoal=SourceAttributes\MoveYGoal
	Else
		DestAttributes\MoveYGoal=DestAttributes\MoveYGoal+SourceAttributes\MoveYGoal
	EndIf

	If ObjectAdjusterTileTypeCollision\Absolute
		DestAttributes\TileTypeCollision=SourceAttributes\TileTypeCollision
	Else
		DestAttributes\TileTypeCollision=DestAttributes\TileTypeCollision+SourceAttributes\TileTypeCollision
	EndIf
	If ObjectAdjusterObjectTypeCollision\Absolute
		DestAttributes\ObjectTypeCollision=SourceAttributes\ObjectTypeCollision
	Else
		DestAttributes\ObjectTypeCollision=DestAttributes\ObjectTypeCollision+SourceAttributes\ObjectTypeCollision
	EndIf

	If ObjectAdjusterCaged\Absolute
		DestAttributes\Caged=SourceAttributes\Caged
	Else
		DestAttributes\Caged=DestAttributes\Caged+SourceAttributes\Caged
	EndIf
	If ObjectAdjusterDead\Absolute
		DestAttributes\Dead=SourceAttributes\Dead
	Else
		DestAttributes\Dead=DestAttributes\Dead+SourceAttributes\Dead
	EndIf
	If ObjectAdjusterDeadTimer\Absolute
		DestAttributes\DeadTimer=SourceAttributes\DeadTimer
	Else
		DestAttributes\DeadTimer=DestAttributes\DeadTimer+SourceAttributes\DeadTimer
	EndIf
	If ObjectAdjusterExclamation\Absolute
		DestAttributes\Exclamation=SourceAttributes\Exclamation
	Else
		DestAttributes\Exclamation=DestAttributes\Exclamation+SourceAttributes\Exclamation
	EndIf
	If ObjectAdjusterShadow\Absolute
		DestAttributes\Shadow=SourceAttributes\Shadow
	Else
		DestAttributes\Shadow=DestAttributes\Shadow+SourceAttributes\Shadow
	EndIf
	If ObjectAdjusterLinked\Absolute
		DestAttributes\Linked=SourceAttributes\Linked
	Else
		DestAttributes\Linked=DestAttributes\Linked+SourceAttributes\Linked
	EndIf
	If ObjectAdjusterLinkBack\Absolute
		DestAttributes\LinkBack=SourceAttributes\LinkBack
	Else
		DestAttributes\LinkBack=DestAttributes\LinkBack+SourceAttributes\LinkBack
	EndIf
	If ObjectAdjusterFlying\Absolute
		DestAttributes\Flying=SourceAttributes\Flying
	Else
		DestAttributes\Flying=DestAttributes\Flying+SourceAttributes\Flying
	EndIf
	If ObjectAdjusterFrozen\Absolute
		DestAttributes\Frozen=SourceAttributes\Frozen
	Else
		DestAttributes\Frozen=DestAttributes\Frozen+SourceAttributes\Frozen
	EndIf
	If ObjectAdjusterIndigo\Absolute
		DestAttributes\Indigo=SourceAttributes\Indigo
	Else
		DestAttributes\Indigo=DestAttributes\Indigo+SourceAttributes\Indigo
	EndIf

	DestAttributes\FutureInt24=SourceAttributes\FutureInt24
	DestAttributes\FutureInt25=SourceAttributes\FutureInt25

	If ObjectAdjusterScaleAdjust\Absolute
		DestAttributes\ScaleAdjust=SourceAttributes\ScaleAdjust
	Else
		DestAttributes\ScaleAdjust=DestAttributes\ScaleAdjust+SourceAttributes\ScaleAdjust
	EndIf
	If ObjectAdjusterScaleXAdjust\Absolute
		DestAttributes\ScaleXAdjust=SourceAttributes\ScaleXAdjust
	Else
		DestAttributes\ScaleXAdjust=DestAttributes\ScaleXAdjust+SourceAttributes\ScaleXAdjust
	EndIf
	If ObjectAdjusterScaleYAdjust\Absolute
		DestAttributes\ScaleYAdjust=SourceAttributes\ScaleYAdjust
	Else
		DestAttributes\ScaleYAdjust=DestAttributes\ScaleYAdjust+SourceAttributes\ScaleYAdjust
	EndIf
	If ObjectAdjusterScaleZAdjust\Absolute
		DestAttributes\ScaleZAdjust=SourceAttributes\ScaleZAdjust
	Else
		DestAttributes\ScaleZAdjust=DestAttributes\ScaleZAdjust+SourceAttributes\ScaleZAdjust
	EndIf
	DestAttributes\FutureFloat5=SourceAttributes\FutureFloat5
	DestAttributes\FutureFloat6=SourceAttributes\FutureFloat6
	DestAttributes\FutureFloat7=SourceAttributes\FutureFloat7
	DestAttributes\FutureFloat8=SourceAttributes\FutureFloat8
	DestAttributes\FutureFloat9=SourceAttributes\FutureFloat9
	DestAttributes\FutureFloat10=SourceAttributes\FutureFloat10
	DestAttributes\FutureString1$=SourceAttributes\FutureString1$
	DestAttributes\FutureString2$=SourceAttributes\FutureString2$

	;For i=0 To 30
	;	ObjectAdjusterString$(Dest,i)="" ;ObjectAdjuster$(i)
	;Next

	RandomizeObjectData(DestObject)

	FreeObjectModel(LevelObjects(Dest)\Model)

	BuildLevelObjectModel(Dest)

	UpdateCurrentGrabbedObjectMarkerPosition(Dest)

End Function

Function CompareObjectToCurrent(Obj.GameObject)

	SourceObject.GameObject=CurrentObject
	DestObject.GameObject=Obj

	SourceAttributes.GameObjectAttributes=SourceObject\Attributes
	DestAttributes.GameObjectAttributes=DestObject\Attributes

	If DestObject\Position\Z<>SourceObject\Position\Z
		ObjectAdjusterZ\Absolute=False
	EndIf

	If DestAttributes\ModelName$<>SourceAttributes\ModelName$
		ObjectAdjusterModelName\Absolute=False
	EndIf
	If DestAttributes\TexName$<>SourceAttributes\TexName$
		ObjectAdjusterTextureName\Absolute=False
	EndIf

	If DestAttributes\XScale#<>SourceAttributes\XScale#
		ObjectAdjusterXScale\Absolute=False
		SourceAttributes\XScale=0
	EndIf
	If DestAttributes\YScale#<>SourceAttributes\YScale#
		ObjectAdjusterYScale\Absolute=False
		SourceAttributes\YScale=0
	EndIf
	If DestAttributes\ZScale#<>SourceAttributes\ZScale#
		ObjectAdjusterZScale\Absolute=False
		SourceAttributes\ZScale=0
	EndIf

	If DestAttributes\XAdjust#<>SourceAttributes\XAdjust#
		ObjectAdjusterXAdjust\Absolute=False
		SourceAttributes\XAdjust=0
	EndIf
	If DestAttributes\YAdjust#<>SourceAttributes\YAdjust#
		ObjectAdjusterYAdjust\Absolute=False
		SourceAttributes\YAdjust=0
	EndIf
	If DestAttributes\ZAdjust#<>SourceAttributes\ZAdjust#
		ObjectAdjusterZAdjust\Absolute=False
		SourceAttributes\ZAdjust=0
	EndIf

	If DestAttributes\PitchAdjust#<>SourceAttributes\PitchAdjust#
		ObjectAdjusterPitchAdjust\Absolute=False
		SourceAttributes\PitchAdjust=0
	EndIf
	If DestAttributes\YawAdjust#<>SourceAttributes\YawAdjust#
		ObjectAdjusterYawAdjust\Absolute=False
		SourceAttributes\YawAdjust=0
	EndIf
	If DestAttributes\RollAdjust#<>SourceAttributes\RollAdjust#
		ObjectAdjusterRollAdjust\Absolute=False
		SourceAttributes\RollAdjust=0
	EndIf

	If DestAttributes\DX#<>SourceAttributes\DX#
		ObjectAdjusterDX\Absolute=False
		SourceAttributes\DX=0
	EndIf
	If DestAttributes\DY#<>SourceAttributes\DY#
		ObjectAdjusterDY\Absolute=False
		SourceAttributes\DY=0
	EndIf
	If DestAttributes\DZ#<>SourceAttributes\DZ#
		ObjectAdjusterDZ\Absolute=False
		SourceAttributes\DZ=0
	EndIf

	If DestAttributes\MovementType<>SourceAttributes\MovementType
		ObjectAdjusterMovementType\Absolute=False
		SourceAttributes\MovementType=0
	EndIf
	If DestAttributes\MovementTypeData<>SourceAttributes\MovementTypeData
		ObjectAdjusterMovementTypeData\Absolute=False
		SourceAttributes\MovementTypeData=0
	EndIf

	If DestAttributes\Data10<>SourceAttributes\Data10
		ObjectAdjusterData10\Absolute=False
		SourceAttributes\Data10=0
	EndIf

	If DestAttributes\DefensePower<>SourceAttributes\DefensePower
		ObjectAdjusterDefensePower\Absolute=False
		SourceAttributes\DefensePower=0
	EndIf
	If DestAttributes\DestructionType<>SourceAttributes\DestructionType
		ObjectAdjusterDestructionType\Absolute=False
		SourceAttributes\DestructionType=0
	EndIf
	If DestAttributes\ID<>SourceAttributes\ID
		ObjectAdjusterID\Absolute=False
		SourceAttributes\ID=0
	EndIf
	If DestAttributes\LogicType<>SourceAttributes\LogicType
		ObjectAdjusterLogicType\Absolute=False
		SourceAttributes\LogicType=0
	EndIf
	If DestAttributes\LogicSubType<>SourceAttributes\LogicSubType
		ObjectAdjusterLogicSubType\Absolute=False
		SourceAttributes\LogicSubType=0
	EndIf
	If DestAttributes\Active<>SourceAttributes\Active
		ObjectAdjusterActive\Absolute=False
		SourceAttributes\Active=0
	EndIf

	If DestAttributes\ActivationType<>SourceAttributes\ActivationType
		ObjectAdjusterActivationType\Absolute=False
		SourceAttributes\ActivationType=0
	EndIf
	If DestAttributes\ActivationSpeed<>SourceAttributes\ActivationSpeed
		ObjectAdjusterActivationSpeed\Absolute=False
		SourceAttributes\ActivationSpeed=0
	EndIf
	If DestAttributes\Status<>SourceAttributes\Status
		ObjectAdjusterStatus\Absolute=False
		SourceAttributes\Status=0
	EndIf
	If DestAttributes\Timer<>SourceAttributes\Timer
		ObjectAdjusterTimer\Absolute=False
		SourceAttributes\Timer=0
	EndIf
	If DestAttributes\TimerMax1<>SourceAttributes\TimerMax1
		ObjectAdjusterTimerMax1\Absolute=False
		SourceAttributes\TimerMax1=0
	EndIf
	If DestAttributes\TimerMax2<>SourceAttributes\TimerMax2
		ObjectAdjusterTimerMax2\Absolute=False
		SourceAttributes\TimerMax2=0
	EndIf
	If DestAttributes\Teleportable<>SourceAttributes\Teleportable
		ObjectAdjusterTeleportable\Absolute=False
		SourceAttributes\Teleportable=0
	EndIf
	If DestAttributes\ButtonPush<>SourceAttributes\ButtonPush
		ObjectAdjusterButtonPush\Absolute=False
		SourceAttributes\ButtonPush=0
	EndIf
	If DestAttributes\WaterReact<>SourceAttributes\WaterReact
		ObjectAdjusterWaterReact\Absolute=False
		SourceAttributes\WaterReact=0
	EndIf

	If DestAttributes\Child<>SourceAttributes\Child
		ObjectAdjusterChild\Absolute=False
		SourceAttributes\Child=0
	EndIf
	If DestAttributes\Parent<>SourceAttributes\Parent
		ObjectAdjusterParent\Absolute=False
		SourceAttributes\Parent=0
	EndIf

	If DestAttributes\Data0<>SourceAttributes\Data0
		ObjectAdjusterData0\Absolute=False
		SourceAttributes\Data0=0
	EndIf
	If DestAttributes\Data1<>SourceAttributes\Data1
		ObjectAdjusterData1\Absolute=False
		SourceAttributes\Data1=0
	EndIf
	If DestAttributes\Data2<>SourceAttributes\Data2
		ObjectAdjusterData2\Absolute=False
		SourceAttributes\Data2=0
	EndIf
	If DestAttributes\Data3<>SourceAttributes\Data3
		ObjectAdjusterData3\Absolute=False
		SourceAttributes\Data3=0
	EndIf
	If DestAttributes\Data4<>SourceAttributes\Data4
		ObjectAdjusterData4\Absolute=False
		SourceAttributes\Data4=0
	EndIf
	If DestAttributes\Data5<>SourceAttributes\Data5
		ObjectAdjusterData5\Absolute=False
		SourceAttributes\Data5=0
	EndIf
	If DestAttributes\Data6<>SourceAttributes\Data6
		ObjectAdjusterData6\Absolute=False
		SourceAttributes\Data6=0
	EndIf
	If DestAttributes\Data7<>SourceAttributes\Data7
		ObjectAdjusterData7\Absolute=False
		SourceAttributes\Data7=0
	EndIf
	If DestAttributes\Data8<>SourceAttributes\Data8
		ObjectAdjusterData8\Absolute=False
		SourceAttributes\Data8=0
	EndIf
	If DestAttributes\Data9<>SourceAttributes\Data9
		ObjectAdjusterData9\Absolute=False
		SourceAttributes\Data9=0
	EndIf

	If DestAttributes\TextData0$<>SourceAttributes\TextData0$
		ObjectAdjusterTextData0\Absolute=False
	EndIf
	If DestAttributes\TextData1$<>SourceAttributes\TextData1$
		ObjectAdjusterTextData1\Absolute=False
	EndIf

	If DestAttributes\Talkable<>SourceAttributes\Talkable
		ObjectAdjusterTalkable\Absolute=False
		SourceAttributes\Talkable=0
	EndIf
	If DestAttributes\CurrentAnim<>SourceAttributes\CurrentAnim
		ObjectAdjusterCurrentAnim\Absolute=False
		SourceAttributes\CurrentAnim=0
	EndIf
	If DestAttributes\StandardAnim<>SourceAttributes\StandardAnim
		ObjectAdjusterStandardAnim\Absolute=False
		SourceAttributes\StandardAnim=0
	EndIf
	If DestAttributes\MovementTimer<>SourceAttributes\MovementTimer
		ObjectAdjusterMovementTimer\Absolute=False
		SourceAttributes\MovementTimer=0
	EndIf
	If DestAttributes\MovementSpeed<>SourceAttributes\MovementSpeed
		ObjectAdjusterMovementSpeed\Absolute=False
		SourceAttributes\MovementSpeed=0
	EndIf

	If DestAttributes\MoveXGoal<>SourceAttributes\MoveXGoal
		ObjectAdjusterMoveXGoal\Absolute=False
		SourceAttributes\MoveXGoal=0
	EndIf
	If DestAttributes\MoveYGoal<>SourceAttributes\MoveYGoal
		ObjectAdjusterMoveYGoal\Absolute=False
		SourceAttributes\MoveYGoal=0
	EndIf

	If DestAttributes\TileTypeCollision<>SourceAttributes\TileTypeCollision
		ObjectAdjusterTileTypeCollision\Absolute=False
		SourceAttributes\TileTypeCollision=0
	EndIf
	If DestAttributes\ObjectTypeCollision<>SourceAttributes\ObjectTypeCollision
		ObjectAdjusterObjectTypeCollision\Absolute=False
		SourceAttributes\ObjectTypeCollision=0
	EndIf

	If DestAttributes\Caged<>SourceAttributes\Caged
		ObjectAdjusterCaged\Absolute=False
		SourceAttributes\Caged=0
	EndIf
	If DestAttributes\Dead<>SourceAttributes\Dead
		ObjectAdjusterDead\Absolute=False
		SourceAttributes\Dead=0
	EndIf
	If DestAttributes\DeadTimer<>SourceAttributes\DeadTimer
		ObjectAdjusterDeadTimer\Absolute=False
		SourceAttributes\DeadTimer=0
	EndIf
	If DestAttributes\Exclamation<>SourceAttributes\Exclamation
		ObjectAdjusterExclamation\Absolute=False
		SourceAttributes\Exclamation=0
	EndIf
	If DestAttributes\Shadow<>SourceAttributes\Shadow
		ObjectAdjusterShadow\Absolute=False
		SourceAttributes\Shadow=0
	EndIf
	If DestAttributes\Linked<>SourceAttributes\Linked
		ObjectAdjusterLinked\Absolute=False
		SourceAttributes\Linked=0
	EndIf
	If DestAttributes\LinkBack<>SourceAttributes\LinkBack
		ObjectAdjusterLinkBack\Absolute=False
		SourceAttributes\LinkBack=0
	EndIf
	If DestAttributes\Flying<>SourceAttributes\Flying
		ObjectAdjusterFlying\Absolute=False
		SourceAttributes\Flying=0
	EndIf
	If DestAttributes\Frozen<>SourceAttributes\Frozen
		ObjectAdjusterFrozen\Absolute=False
		SourceAttributes\Frozen=0
	EndIf
	If DestAttributes\Indigo<>SourceAttributes\Indigo
		ObjectAdjusterIndigo\Absolute=False
		SourceAttributes\Indigo=0
	EndIf

	If DestAttributes\ScaleAdjust<>SourceAttributes\ScaleAdjust
		ObjectAdjusterScaleAdjust\Absolute=False
		SourceAttributes\ScaleAdjust=0
	EndIf
	If DestAttributes\ScaleXAdjust<>SourceAttributes\ScaleXAdjust
		ObjectAdjusterScaleXAdjust\Absolute=False
		SourceAttributes\ScaleXAdjust=0
	EndIf
	If DestAttributes\ScaleYAdjust<>SourceAttributes\ScaleYAdjust
		ObjectAdjusterScaleYAdjust\Absolute=False
		SourceAttributes\ScaleYAdjust=0
	EndIf
	If DestAttributes\ScaleZAdjust<>SourceAttributes\ScaleZAdjust
		ObjectAdjusterScaleZAdjust\Absolute=False
		SourceAttributes\ScaleZAdjust=0
	EndIf

End Function

Function AreAllObjectAdjustersAbsolute()

	For ObjAdjusterInt.ObjectAdjusterInt=Each ObjectAdjusterInt
		If Not ObjAdjusterInt\Absolute
			Return False
		EndIf
	Next
	For ObjAdjusterFloat.ObjectAdjusterFloat=Each ObjectAdjusterFloat
		If Not ObjAdjusterFloat\Absolute
			Return False
		EndIf
	Next
	For ObjAdjusterString.ObjectAdjusterString=Each ObjectAdjusterString
		If Not ObjAdjusterString\Absolute
			Return False
		EndIf
	Next

	Return True

End Function

Function MakeAllObjectAdjustersAbsolute()

	For ObjAdjusterInt.ObjectAdjusterInt=Each ObjectAdjusterInt
		ObjAdjusterInt\Absolute=True
	Next
	For ObjAdjusterFloat.ObjectAdjusterFloat=Each ObjectAdjusterFloat
		ObjAdjusterFloat\Absolute=True
	Next
	For ObjAdjusterString.ObjectAdjusterString=Each ObjectAdjusterString
		ObjAdjusterString\Absolute=True
	Next

End Function

Function MakeAllObjectAdjustersNotRandomized()

	For ObjAdjusterInt.ObjectAdjusterInt=Each ObjectAdjusterInt
		ObjAdjusterInt\Absolute=True
		ObjAdjusterInt\RandomEnabled=False
	Next
	For ObjAdjusterFloat.ObjectAdjusterFloat=Each ObjectAdjusterFloat
		ObjAdjusterFloat\Absolute=True
		ObjAdjusterFloat\RandomEnabled=False
	Next
	For ObjAdjusterString.ObjectAdjusterString=Each ObjectAdjusterString
		ObjAdjusterString\Absolute=True
		ObjAdjusterString\RandomEnabled=False
	Next

End Function

Function UpdateCurrentGrabbedObjectMarkerPosition(i)

	SetEntityPositionToObjectPositionWithoutZ(CurrentGrabbedObjectMarkers(i),LevelObjects(i),0.0)

End Function

Function DisplayAsBinaryString$(value)

Result$=""

For i=0 To 14
	If i Mod 5 = 0 And i>0
		Result$=Result$+" "
	EndIf
	If (value And 2^i)<>0
		Result$=Result$+"1"
	Else
		Result$=Result$+"0"
	EndIf
Next

Return Result$

End Function

Function AdjusterAppearsInWop(adjuster$)

	For i=0 To NofWopAdjusters-1
		If ObjectAdjusterWop$(i)=adjuster$
			Return True
		EndIf
	Next

	Return False

End Function

Function TooltipTargetsEffectiveID(StartX,StartY,Index)

	If CurrentObjectTargetIDEnabled(Index)
		TooltipTargetsEffectiveIDInner(StartX,StartY,CurrentObjectTargetID(Index))
	EndIf

End Function

Function TooltipTargetsEffectiveIDInner(StartX,StartY,EffectiveID)

	Count=CountObjectEffectiveIDs(EffectiveID)
	ShowTooltipRightAligned(StartX,StartY,"Targets effective ID "+EffectiveID+", which matches "+Count+" "+MaybePluralize$("object",Count)+" in this level.")

End Function

Function TooltipHasActivateID(StartX,StartY,Index)

	If CurrentObjectActivateIDEnabled(Index)
		TooltipHasActivateIDInner(StartX,StartY,CurrentObjectActivateID(Index))
	EndIf

End Function

Function TooltipHasActivateIDInner(StartX,StartY,ActivateID)

	If ActivateID>0
		Count=CountObjectEffectiveIDs(ActivateID)
		ShowTooltipRightAligned(StartX,StartY,"Effective ID "+ActivateID+" matches "+Count+" "+MaybePluralize$("object",Count)+" in this level.")
	EndIf

End Function

Function HoverOverObjectAdjuster(i)

	StartX=SidebarX+10
	StartY=SidebarY+305
	StartY=StartY+15+(i-ObjectAdjusterStart)*15

	CenterX=StartX+92
	TooltipLeftY=StartY+30
	TooltipAboveY=StartY+8

	Select ObjectAdjuster$(i)

	Case "Data0"
		If CurrentObject\Attributes\LogicType=90
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				TooltipTargetsEffectiveID(StartX,TooltipLeftY,0)
			ElseIf (CurrentObject\Attributes\LogicSubType Mod 32)<10 Or (CurrentObject\Attributes\LogicSubType Mod 32)=16 Or (CurrentObject\Attributes\LogicSubType Mod 32)=17 ; ColorX2Y or Rotator or ???
				TooltipTargetsEffectiveID(StartX,TooltipLeftY,0)
			ElseIf CurrentObject\Attributes\LogicSubType=13 And ObjectAdjusterData0\Absolute ; Adventure star
				ShowTooltipRightAligned(StartX,TooltipLeftY,GetAdventureName$(CurrentObject\Attributes\Data0))
			EndIf
		ElseIf CurrentObject\Attributes\LogicType=165 ; Arcade Cabinet
			tex$=""
			If CurrentObjectTargetIDEnabled(0)
				TargetID=CurrentObjectTargetID(0)
				Count=CountObjectEffectiveIDs(TargetID)
				tex$=tex$+"ID "+TargetID+" matches "+Count+"."
			EndIf
			For i=1 To CurrentObjectTargetIDCount-1
				If CurrentObjectTargetIDEnabled(i)
					TargetID=CurrentObjectTargetID(i)
					Count=CountObjectEffectiveIDs(TargetID)
					tex$=tex$+" ID "+TargetID+" matches "+Count+"."
				EndIf
			Next
			If CurrentObjectTargetIDEnabled(0)
				ShowTooltipRightAligned(StartX,TooltipLeftY,tex$)
			EndIf
		ElseIf CurrentObject\Attributes\LogicType=190 Or CurrentObject\Attributes\LogicType=164 ; Particle Spawner or Fountain
			ShowParticlePreviewRightAligned(StartX,TooltipLeftY,CurrentObject\Attributes\Data0)
		EndIf

	Case "Data1"
		If CurrentObject\Attributes\LogicType=90
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				TooltipTargetsEffectiveID(StartX,TooltipLeftY,1)
			ElseIf (CurrentObject\Attributes\LogicSubType Mod 32)=15 ; General Command
				TooltipTargetsEffectiveID(StartX,TooltipLeftY,0)
				If ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute
					ExtraInfo$=GetCmdData1ExtraInfo$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1)
					If ExtraInfo$<>""
						ShowTooltipRightAligned(StartX,TooltipLeftY,ExtraInfo$)
					EndIf
				EndIf
			ElseIf (CurrentObject\Attributes\LogicSubType Mod 32)=11 Or (CurrentObject\Attributes\LogicSubType Mod 32)=16 Or (CurrentObject\Attributes\LogicSubType Mod 32)=17 ; NPC Modifier or Rotator
				TooltipTargetsEffectiveID(StartX,TooltipLeftY,0)
			EndIf
		ElseIf IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			TooltipTargetsEffectiveID(StartX,TooltipLeftY,0)
			If ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute
				If CurrentObject\Attributes\Data0>=21 And CurrentObject\Attributes\Data0<=27
					ShowTooltipRightAligned(StartX,TooltipLeftY,PreviewDialog$(CurrentObject\Attributes\Data1,0))
				EndIf
			EndIf
		EndIf

	Case "Data2"
		If CurrentObject\Attributes\LogicType=90
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				TooltipTargetsEffectiveID(StartX,TooltipLeftY,2)
			ElseIf (CurrentObject\Attributes\LogicSubType Mod 32)=15 ; General Command
				If ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute And ObjectAdjusterData2\Absolute
					ShowTooltipRightAligned(StartX,TooltipLeftY,GetCmdData2ExtraInfo$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1,CurrentObject\Attributes\Data2))
				EndIf
			ElseIf (CurrentObject\Attributes\LogicSubType Mod 32)=11 And CurrentObject\Attributes\Data0=2 ; NPC exclamation
				If ObjectAdjusterData0\Absolute And ObjectAdjusterData2\Absolute
					ShowParticlePreviewRightAligned(StartX,TooltipLeftY,CurrentObject\Attributes\Data2)
				EndIf
			ElseIf (CurrentObject\Attributes\LogicSubType Mod 32)<10 ; ColorX2Y
				TooltipTargetsEffectiveID(StartX,TooltipLeftY,0)
			EndIf
		ElseIf IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			If ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute And ObjectAdjusterData2\Absolute
				ShowTooltipRightAligned(StartX,TooltipLeftY,GetCmdData2ExtraInfo$(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1,CurrentObject\Attributes\Data2))
			EndIf
		EndIf

		; Q - player NPC functionality
		If IsModelNPC(CurrentObject\Attributes\ModelName$) And ObjectAdjusterModelName\Absolute And ObjectAdjusterData2\Absolute
			If CurrentObject\Attributes\Data2>0
				ShowTooltipRightAligned(StartX,TooltipLeftY,MyProcessFileNameModel$(GetAccFilenameModel$(CurrentObject\Attributes\Data2)))
			EndIf
		EndIf

	Case "Data3"
		If CurrentObject\Attributes\LogicType=90
			If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				TooltipTargetsEffectiveID(StartX,TooltipLeftY,3)
			ElseIf (CurrentObject\Attributes\LogicSubType Mod 32)=15 ; General Command
				If ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute And ObjectAdjusterData3\Absolute And CurrentObject\Attributes\Data0=27
					ShowTooltipRightAligned(StartX,TooltipLeftY,PreviewDialog$(CurrentObject\Attributes\Data1,CurrentObject\Attributes\Data3))
				EndIf
			EndIf
		ElseIf IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			If ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute And ObjectAdjusterData3\Absolute And CurrentObject\Attributes\Data0=27
				ShowTooltipRightAligned(StartX,TooltipLeftY,PreviewDialog$(CurrentObject\Attributes\Data1,CurrentObject\Attributes\Data3))
			EndIf
		ElseIf CurrentObject\Attributes\LogicType=242 ; Cuboid
			If CurrentObjectTargetIDCount<>0
				TooltipTargetsEffectiveID(StartX,TooltipLeftY,0)
			EndIf
		EndIf

		; Q - player NPC functionality
		If IsModelNPC(CurrentObject\Attributes\ModelName$) And ObjectAdjusterModelName\Absolute And ObjectAdjusterData2\Absolute And ObjectAdjusterData3\Absolute
			If CurrentObject\Attributes\Data2>0
				ShowTooltipRightAligned(StartX,TooltipLeftY,MyProcessFileNameTexture$(GetAccFilenameTexture$(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)))
			EndIf
		EndIf

	Case "Data4"
		If IsObjectLogicFourColorButton(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
			TooltipTargetsEffectiveID(StartX,TooltipLeftY,0)
		ElseIf IsObjectLogicAutodoor(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
			TooltipHasActivateID(StartX,TooltipLeftY,0)
		EndIf

		; Q - player NPC functionality
		If IsModelNPC(CurrentObject\Attributes\ModelName$) And ObjectAdjusterModelName\Absolute And ObjectAdjusterData4\Absolute
			If CurrentObject\Attributes\Data4>0
				ShowTooltipRightAligned(StartX,TooltipLeftY,MyProcessFileNameModel$(GetAccFilenameModel$(CurrentObject\Attributes\Data4)))
			EndIf
		EndIf

	Case "Data5"
		If IsObjectLogicFourColorButton(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
			TooltipTargetsEffectiveID(StartX,TooltipLeftY,1)
		ElseIf IsObjectLogicAutodoor(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
			TooltipHasActivateID(StartX,TooltipLeftY,1)
		EndIf

		; Q - player NPC functionality
		If IsModelNPC(CurrentObject\Attributes\ModelName$) And ObjectAdjusterModelName\Absolute And ObjectAdjusterData4\Absolute And ObjectAdjusterData5\Absolute
			If CurrentObject\Attributes\Data4>0
				ShowTooltipRightAligned(StartX,TooltipLeftY,MyProcessFileNameTexture$(GetAccFilenameTexture$(CurrentObject\Attributes\Data4,CurrentObject\Attributes\Data5+1)))
			EndIf
		EndIf

	Case "Data6"
		If IsObjectLogicFourColorButton(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
			TooltipTargetsEffectiveID(StartX,TooltipLeftY,2)
		ElseIf IsObjectLogicAutodoor(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
			TooltipHasActivateID(StartX,TooltipLeftY,2)
		EndIf

	Case "Data7"
		If IsObjectLogicFourColorButton(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
			TooltipTargetsEffectiveID(StartX,TooltipLeftY,3)
		EndIf

	Case "Data8"
		If CurrentObject\Attributes\LogicType=90 And ObjectAdjusterLogicType\Absolute
			TooltipHasActivateID(StartX,TooltipLeftY,0)
		EndIf

	Case "TileTypeCollision"
		If ObjectAdjusterTileTypeCollision\Absolute
			tex2$="TTC"
			tex$="00000 00000 00000"

			HalfNameWidth=4*Len(tex2$+": "+tex$)
			BitStartX=CenterX-HalfNameWidth+8*Len(tex2$+": ")

			BitPositionIndex=GetBitPositionIndex(BitStartX)
			BitIndex=BitPositionIndexToBitIndex(BitPositionIndex)
			If BitIndexIsValid(BitIndex) And BitPositionIndexIsValid(BitPositionIndex)
				ShowTooltipCenterAligned(BitStartX+BitPositionIndex*8+12,TooltipAboveY,LogicIdToLogicName$(BitIndex))
			EndIf
		EndIf

	Case "ObjectTypeCollision"
		If ObjectAdjusterObjectTypeCollision\Absolute
			tex2$="OTC"
			tex$="00000 00000 00000"

			HalfNameWidth=4*Len(tex2$+": "+tex$)
			BitStartX=CenterX-HalfNameWidth+8*Len(tex2$+": ")

			BitPositionIndex=GetBitPositionIndex(BitStartX)
			BitIndex=BitPositionIndexToBitIndex(BitPositionIndex)
			If BitIndexIsValid(BitIndex) And BitPositionIndexIsValid(BitPositionIndex)
				ShowTooltipCenterAligned(BitStartX+BitPositionIndex*8+12,TooltipAboveY,ObjectTypeCollisionBitToName$(BitIndex))
			EndIf
		EndIf

	Case "Type"
		If ObjectAdjusterLogicType\Absolute
			Count=CountObjectTypes(CurrentObject\Attributes\LogicType)
			ShowTooltipRightAligned(StartX,TooltipLeftY,Count+" "+MaybePluralize$("object",Count)+" in this level "+MaybePluralize$("has",Count)+" this Type.")
		EndIf

	Case "SubType"
		If ObjectAdjusterLogicType\Absolute And ObjectAdjusterLogicSubType\Absolute
			Count=CountObjectLogics(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
			ShowTooltipRightAligned(StartX,TooltipLeftY,Count+" "+MaybePluralize$("object",Count)+" in this level "+MaybePluralize$("has",Count)+" this object logic.")
		EndIf

	Case "ModelName"
		If ObjectAdjusterModelName\Absolute
			Count=CountObjectModelNames(CurrentObject\Attributes\ModelName$)
			ShowTooltipRightAligned(StartX,TooltipLeftY,Count+" "+MaybePluralize$("object",Count)+" in this level "+MaybePluralize$("has",Count)+" this ModelName.")
		EndIf

	Case "TextureName"
		If ObjectAdjusterTextureName\Absolute
			Count=CountObjectTextureNames(CurrentObject\Attributes\TexName$)
			ShowTooltipRightAligned(StartX,TooltipLeftY,Count+" "+MaybePluralize$("object",Count)+" in this level "+MaybePluralize$("has",Count)+" this TextureName.")
		EndIf

	Case "ID"
		If ObjectAdjusterID\Absolute
			EffectiveID=CalculateEffectiveID(CurrentObject\Attributes)
			Count=CountObjectEffectiveIDs(EffectiveID)
			ShowTooltipRightAligned(StartX,TooltipLeftY,Count+" "+MaybePluralize$("object",Count)+" in this level "+MaybePluralize$("has",Count)+" this effective ID, which is "+EffectiveID+".")
		EndIf

	Case "Talkable"
		If ObjectAdjusterTalkable\Absolute
			TheDialog=CurrentObject\Attributes\Talkable
			If TheDialog<>0
				If TheDialog>=10001
					TheDialog=TheDialog-10000
				EndIf
				ShowTooltipRightAligned(StartX,TooltipLeftY,PreviewDialog$(TheDialog,0))
			EndIf
		EndIf

	Case "Exclamation"
		If CurrentObject\Attributes\Exclamation<>-1
			ShowParticlePreviewRightAligned(StartX,TooltipLeftY,CurrentObject\Attributes\Exclamation)
		EndIf

	End Select

End Function

Function GetMagicColor(id,index)

	Select id
	Case -1 ; no charge (white in OpenWA, black in vanilla; same as not having any magic charged to your gloves)
		red=255
		green=255
		blue=255
	Case 0 ; floing
		red=255
	Case 1 ; pow
		red=255
		green=100
	Case 2 ; pop
		red=255
		green=255
	Case 3 ; grow
		green=255
	Case 4 ; brr
		green=255
		blue=255
	Case 5 ; flash
		blue=255
	Case 6 ; blink
		red=255
		blue=255
	Case 7 ; null
		red=255
		blue=255
		green=255
	Case 8 ; rainbow
		;red=ii*64
		;green=255-ii*32
		;blue=255-ii*64
		red=255
		green=255
		blue=255
	Case 9 ; barrel
		red=67
		blue=67
		green=67
	Case 10 ; turret
		red=107
		green=0
		blue=153
	End Select

	If index=0 Return red
	If index=1 Return green
	If index=2 Return blue

	Return 255

End Function

Function GapSubType(SmallerExclusive,LargerExclusive)

	If CurrentObject\Attributes\LogicSubType>SmallerExclusive And CurrentObject\Attributes\LogicSubType<LargerExclusive-20
		CurrentObject\Attributes\LogicSubType=LargerExclusive
	Else If CurrentObject\Attributes\LogicSubType>LargerExclusive-19 And CurrentObject\Attributes\LogicSubType<LargerExclusive
		CurrentObject\Attributes\LogicSubType=SmallerExclusive
	EndIf

End Function

Function OnLeftHalfAdjuster()

	Return MouseX()<SidebarX+102 ;602

End Function

Function SetupPrompt()
	FlushKeys
	Locate 0,0
	Color 0,0,0
	Rect 0,0,500,40,True
	Color 255,255,255
End Function

Function InputString$(title$)
	SetupPrompt()
	Result$=Input$(title$)
	ReturnKeyReleased=False
	Return Result$
End Function

Function InputInt(title$)
	SetupPrompt()
	Result=Input(title$)
	ReturnKeyReleased=False
	Return Result
End Function

Function InputFloat#(title$)
	SetupPrompt()
	Result#=Input$(title$)
	ReturnKeyReleased=False
	Return Result#
End Function

Global SomethingWasAdjusted=False
Global UsedRawInput=False

Function WasAdjusted()

	Result=SomethingWasAdjusted
	SomethingWasAdjusted=False
	Return Result

End Function

Function AdjustInt(ValueName$, CurrentValue, NormalSpeed, FastSpeed, DelayTime)

	SomethingWasAdjusted=False
	UsedRawInput=False

	Fast=False
	If ShiftDown() Then Fast=True
	RawInput=False
	If CtrlDown() Then RawInput=True

	Adj=NormalSpeed
	If Fast Adj=FastSpeed

	If LeftMouse=True
		If RawInput=True
			SomethingWasAdjusted=True
			UsedRawInput=True
			Return InputInt(ValueName$)
		ElseIf MouseDebounceFinished()
			SomethingWasAdjusted=True
			MouseDebounceSet(DelayTime)
			Return CurrentValue+Adj
		EndIf
	EndIf
	If RightMouse=True And MouseDebounceFinished()
		SomethingWasAdjusted=True
		MouseDebounceSet(DelayTime)
		Return CurrentValue-Adj
	EndIf
	If MouseScroll<>0
		SomethingWasAdjusted=True
	EndIf
	Return CurrentValue+MouseScroll*Adj

End Function

Function ZeroRoundFloat#(Value#)

	If Value#>-0.00001 And Value#<0.00001
		Return 0.0
	Else
		Return Value#
	EndIf

End Function

Function AdjustFloat#(ValueName$, CurrentValue#, NormalSpeed#, FastSpeed#, DelayTime)

	Result#=AdjustFloatWithoutZeroRounding#(ValueName$, CurrentValue#, NormalSpeed#, FastSpeed#, DelayTime)
	Result#=ZeroRoundFloat#(Result#)
	Return Result#

End Function

Function AdjustFloatWithoutZeroRounding#(ValueName$, CurrentValue#, NormalSpeed#, FastSpeed#, DelayTime)

	SomethingWasAdjusted=False
	UsedRawInput=False

	Fast=False
	If ShiftDown() Then Fast=True
	RawInput=False
	If CtrlDown() Then RawInput=True

	Adj#=NormalSpeed
	If Fast Adj#=FastSpeed

	If LeftMouse=True
		If RawInput=True
			SomethingWasAdjusted=True
			UsedRawInput=True
			Return InputFloat#(ValueName$)
		ElseIf MouseDebounceFinished()
			SomethingWasAdjusted=True
			MouseDebounceSet(DelayTime)
			Return CurrentValue+Adj
		EndIf
	EndIf
	If RightMouse=True And MouseDebounceFinished()
		SomethingWasAdjusted=True
		MouseDebounceSet(DelayTime)
		Return CurrentValue-Adj
	EndIf
	If MouseScroll<>0
		SomethingWasAdjusted=True
	EndIf
	Return CurrentValue+MouseScroll*Adj

End Function

Function GetActiveTexturePrefix$()

	If CurrentTexturePrefix=-1
		Return ""
	Else
		Return TexturePrefix$(CurrentTexturePrefix)
	EndIf

End Function

Function InputTextureName(Prompt$)

	CurrentObject\Attributes\TexName$=InputString$(Prompt$)
	ObjectAdjusterTextureName\Absolute=True
	If Left$(CurrentObject\Attributes\TexName$,1)="/"
		CurrentObject\Attributes\TexName$="userdata/custom/models/"+Right$(CurrentObject\Attributes\TexName$,Len(CurrentObject\Attributes\TexName$)-1)
	Else
		CurrentObject\Attributes\TexName$=GetActiveTexturePrefix$()+CurrentObject\Attributes\TexName$
	EndIf

End Function

Function InputModelName(Prompt$)

	CurrentObject\Attributes\ModelName$=InputString$(Prompt$)
	ObjectAdjusterModelName\Absolute=True
	If CurrentObject\Attributes\ModelName$="!CustomModel"
		CurrentObject\Attributes\TextData0$=InputString$("Enter custom model name (e.g. Default): ")
	ElseIf Left$(CurrentObject\Attributes\ModelName$,1)="/" Or Left$(CurrentObject\Attributes\ModelName$,1)="?"
		CurrentObject\Attributes\TextData0$=Right$(CurrentObject\Attributes\ModelName$,Len(CurrentObject\Attributes\ModelName$)-1)
		CurrentObject\Attributes\ModelName$="!CustomModel"
	EndIf

End Function

Function ConfirmFindAndReplace()

	Return GetConfirmation("Find and replace on matching values for ALL objects?")

End Function

Function CurrentObjectWasChanged()

	CurrentGrabbedObjectModified=True

	If AreAllObjectAdjustersAbsolute()
		SetBrushToCurrentObject()
	EndIf

End Function

Function SetBrushToCurrentObject()

	NofBrushObjects=1
	BrushSpaceWidth=1
	BrushSpaceHeight=1
	CopyObjectToBrush(CurrentObject,0,0,0)
	GenerateBrushPreview()

End Function

Function CurrentTileWasChanged()

	SetBrushToCurrentTile()

End Function

Function SetBrushToCurrentTile()

	BrushSpaceWidth=1
	BrushSpaceHeight=1
	CopyTile(CurrentTile,BrushTiles(0,0))
	GenerateBrushPreview()

End Function

Function UpdateTile(i,j)

	UpdateLevelTile(i,j)
	UpdateWaterTile(i,j)
	UpdateLogicTile(i,j)

End Function

Function UpdateLevelTile(i,j)

	If i<0 Or j<0 Or i>=levelwidth Or j>=levelheight Then Return

	ResetLevelTile(i,j)
	UpdateLevelTileTexture(i,j)
	ShiftLevelTileByExtrude(i,j)
	ShiftLevelTileByRandom(i,j)
	ShiftLevelTileByHeight(i,j)
	ShiftLevelTileEdges(i,j)
	UpdateLevelTileSides(i,j)

End Function

Function UpdateWater()

	If WaterFlow>=0
		PositionTexture WaterTexture,0,-((4*LevelTimer*WaterFlow) Mod 10000)/10000.0
	EndIf
	If waterflow<0
		; rock
		PositionTexture WaterTexture,0,0.5+0.125*WaterFlow/4*Sin(-(4*LevelTimer*WaterFlow)/10.0)
	EndIf

	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			UpdateWaterVertices(i,j)
		Next
	Next

End Function

Function UpdateWaterVertices(i,j)
	mySurface=WaterSurface(j)
	Turbulence#=LevelTiles(i,j)\Water\Turbulence
	If Turbulence#<0.0
		Turbulence=0.0
	EndIf

	VertexCoords mySurface,i*4+0,VertexX(mySurface,i*4+0),LevelTiles(i,j)\Water\Height+Turbulence*Cos(LevelTimer+(i Mod 4)*90+(j Mod 2)*180),VertexZ(mySurface,i*4+0)
	VertexCoords mySurface,i*4+1,VertexX(mySurface,i*4+1),LevelTiles(i,j)\Water\Height+Turbulence*Cos(LevelTimer+(i Mod 4)*90+(j Mod 2)*180+90),VertexZ(mySurface,i*4+1)

	VertexCoords mySurface,i*4+2,VertexX(mySurface,i*4+2),LevelTiles(i,j)\Water\Height+Turbulence*Cos(LevelTimer+(i Mod 4)*90+(j Mod 2)*180-180),VertexZ(mySurface,i*4+2)
	VertexCoords mySurface,i*4+3,VertexX(mySurface,i*4+3),LevelTiles(i,j)\Water\Height+Turbulence*Cos(LevelTimer+(i Mod 4)*90+(j Mod 2)*180+90-180),VertexZ(mySurface,i*4+3)
End Function

Function UpdateWaterTile(i,j)

	If i<0 Or j<0 Or i>=levelwidth Or j>=levelheight Then Return

	; top face
	CalculateUV(LevelTiles(i,j)\Water\Texture,0,0,LevelTiles(i,j)\Water\Rotation,4,1)
	VertexCoords WaterSurface(j),i*4+0,i,LevelTiles(i,j)\Water\Height,-j
	VertexTexCoords WaterSurface(j),i*4+0,ChunkTileU,ChunkTileV
	If LevelTiles(i,j)\Water\Texture>=0
		VertexColor WaterSurface(j),i*4+0,255,255,255
	Else
		VertexColor WaterSurface(j),i*4+0,0,0,0
	EndIf

	CalculateUV(LevelTiles(i,j)\Water\Texture,1,0,LevelTiles(i,j)\Water\Rotation,4,1)
	VertexCoords WaterSurface(j),i*4+1,i+1,LevelTiles(i,j)\Water\Height,-j
	VertexTexCoords WaterSurface(j),i*4+1,ChunkTileU,ChunkTileV
	If LevelTiles(i,j)\Water\Texture>=0
		VertexColor WaterSurface(j),i*4+1,255,255,255
	Else
		VertexColor WaterSurface(j),i*4+1,0,0,0
	EndIf

	CalculateUV(LevelTiles(i,j)\Water\Texture,0,1,LevelTiles(i,j)\Water\Rotation,4,1)
	VertexCoords WaterSurface(j),i*4+2,i,LevelTiles(i,j)\Water\Height,-j-1
	VertexTexCoords WaterSurface(j),i*4+2,ChunkTileU,ChunkTileV
	If LevelTiles(i,j)\Water\Texture>=0
		VertexColor WaterSurface(j),i*4+2,255,255,255
	Else
		VertexColor WaterSurface(j),i*4+2,0,0,0
	EndIf

	CalculateUV(LevelTiles(i,j)\Water\Texture,1,1,LevelTiles(i,j)\Water\Rotation,4,1)
	VertexCoords WaterSurface(j),i*4+3,i+1,LevelTiles(i,j)\Water\Height,-j-1
	VertexTexCoords WaterSurface(j),i*4+3,ChunkTileU,ChunkTileV
	If LevelTiles(i,j)\Water\Texture>=0
		VertexColor WaterSurface(j),i*4+3,255,255,255
	Else
		VertexColor WaterSurface(j),i*4+3,0,0,0
	EndIf

End Function

Function UpdateLogicTile(i,j)

	; top face

	If LevelTileLogicHasVisuals(i,j)
		nologicshow=0
	Else
		nologicshow=-300
	EndIf

	If LevelTiles(i,j)\Water\Height>LevelTiles(i,j)\Terrain\Extrusion
		height#=LevelTiles(i,j)\Water\Height+0.05
	Else
		height#=LevelTiles(i,j)\Terrain\Extrusion+0.05
	EndIf
	VertexCoords LogicSurface(j),i*LogicVerticesPerTile,i+nologicshow,height,-j
	VertexCoords LogicSurface(j),i*LogicVerticesPerTile+1,i+1+nologicshow,height,-j
	VertexCoords LogicSurface(j),i*LogicVerticesPerTile+2,i+nologicshow,height,-j-1
	VertexCoords LogicSurface(j),i*LogicVerticesPerTile+3,i+1+nologicshow,height,-j-1

	height#=0.05

	VertexCoords LogicSurface(j),i*LogicVerticesPerTile+4,i+nologicshow,height,-j
	VertexCoords LogicSurface(j),i*LogicVerticesPerTile+5,i+1+nologicshow,height,-j
	VertexCoords LogicSurface(j),i*LogicVerticesPerTile+6,i+nologicshow,height,-j-1
	VertexCoords LogicSurface(j),i*LogicVerticesPerTile+7,i+1+nologicshow,height,-j-1

	ColorLevelTileLogic(i,j)

End Function

; Used for models that do not exist.
Function CreateErrorMesh()

	Entity=CreateSphere()
	ScaleMesh Entity,.3,.3,.3
	UseErrorColor(Entity)
	Return Entity

End Function

; Used for textures that do not exist.
Function UseErrorColor(Entity)

	EntityColor Entity,ModelErrorR,ModelErrorG,ModelErrorB

End Function

Function UseMagicColor(Entity,Col)

	EntityColor Entity,GetMagicColor(Col,0),GetMagicColor(Col,1),GetMagicColor(Col,2)

End Function

Function CreateSignMesh(Data0,Data1)

	If Data0>-1 And Data0<6
		Entity=CopyEntity(SignMesh(Data0))
		If Data1>-1 And Data1<6
			EntityTexture Entity,SignTexture(Data1)
		Else
			UseErrorColor(Entity)
		EndIf
	Else
		Entity=CreateErrorMesh()
	EndIf

	Return Entity

End Function

Function CreateNoneMesh()

	Entity=CreateSphere()
	ScaleMesh Entity,.2,.2,.2
	Return Entity

End Function

Function CreateFloingBubbleMesh() ; editor specific

	Entity=CreateSphere()
	s=CreateCylinder()
	ScaleMesh s,0.5,0.01,0.5
	PositionMesh s,0,-0.58,0
	AddMesh s,Entity
	FreeEntity s
	EntityTexture Entity,FloingTexture
	EntityAlpha Entity,0.5
	EntityBlend Entity,3
	Return Entity

End Function

Function CreateGrowFlowerMesh(tilelogic) ; editor specific

	If tilelogic=2
		Return CopyEntity(ObstacleMesh(7))
	Else If tilelogic=11 Or tilelogic=12
		Return CopyEntity(ObstacleMesh(16))
		;EntityTexture ObjectEntity(Dest),waterfalltexture
	Else
		Return CopyEntity(ObstacleMesh(10))
	EndIf

End Function

Function CreateMagicMirrorMesh() ; editor specific

	Entity=CreateCube()
	ScaleMesh Entity,3.5,2.59,.52
	;EntityColor Entity,255,0,0
	;EntityAlpha Entity,.5
	Return Entity

End Function

Function BuildCurrentTileModel()

	j=0
	i=0
	ClearSurface CurrentSurface

	; add block

	; top face
	CalculateUV(CurrentTile\Terrain\Texture,0,0,CurrentTile\Terrain\Rotation,8,1)
	AddVertex (CurrentSurface,-1,101,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\Texture,1,0,CurrentTile\Terrain\Rotation,8,1)
	AddVertex (CurrentSurface,1,101,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\Texture,0,1,CurrentTile\Terrain\Rotation,8,1)
	AddVertex (CurrentSurface,-1,101,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\Texture,1,1,CurrentTile\Terrain\Rotation,8,1)
	AddVertex (CurrentSurface,1,101,-1,ChunkTileU,ChunkTileV)

	AddTriangle (CurrentSurface,i*20+0,i*20+1,i*20+2)
	AddTriangle (CurrentSurface,i*20+1,i*20+3,i*20+2)

	; north face
	CalculateUV(CurrentTile\Terrain\SideTexture,0,0,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,1,101,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,1,0,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,-1,101,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,0,1,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,1,99,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,1,1,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,-1,99,1,ChunkTileU,ChunkTileV)

	AddTriangle (CurrentSurface,i*20+4,i*20+5,i*20+6)
	AddTriangle (CurrentSurface,i*20+5,i*20+7,i*20+6)

	; east face
	CalculateUV(CurrentTile\Terrain\SideTexture,0,0,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,1,101,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,1,0,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,1,101,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,0,1,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,1,99,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,1,1,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,1,99,1,ChunkTileU,ChunkTileV)

	AddTriangle (CurrentSurface,i*20+8,i*20+9,i*20+10)
	AddTriangle (CurrentSurface,i*20+9,i*20+11,i*20+10)

	; south face
	CalculateUV(CurrentTile\Terrain\SideTexture,0,0,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,-1,101,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,1,0,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,1,101,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,0,1,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,-1,99,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,1,1,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,1,99,-1,ChunkTileU,ChunkTileV)

	AddTriangle (CurrentSurface,i*20+12,i*20+13,i*20+14)
	AddTriangle (CurrentSurface,i*20+13,i*20+15,i*20+14)

	; west face
	CalculateUV(CurrentTile\Terrain\SideTexture,0,0,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,-1,101,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,1,0,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,-1,101,-1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,0,1,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,-1,99,1,ChunkTileU,ChunkTileV)
	CalculateUV(CurrentTile\Terrain\SideTexture,1,1,CurrentTile\Terrain\SideRotation,8,1)
	AddVertex (CurrentSurface,-1,99,-1,ChunkTileU,ChunkTileV)

	AddTriangle (CurrentSurface,i*20+16,i*20+17,i*20+18)
	AddTriangle (CurrentSurface,i*20+17,i*20+19,i*20+18)

	UpdateNormals CurrentMesh
	EntityTexture CurrentMesh,LevelTexture

	; and the water tile
	; top face

	mySurface=CurrentWaterTileSurface

	CalculateUV(CurrentTile\Water\Texture,0,0,CurrentTile\Water\Rotation,4,LevelDetail)
	VertexTexCoords mySurface,0,ChunkTileU,ChunkTileV
	CalculateUV(CurrentTile\Water\Texture,LevelDetail,0,CurrentTile\Water\Rotation,4,LevelDetail)
	VertexTexCoords mySurface,1,ChunkTileU,ChunkTileV
	CalculateUV(CurrentTile\Water\Texture,0,LevelDetail,CurrentTile\Water\Rotation,4,LevelDetail)
	VertexTexCoords mySurface,2,ChunkTileU,ChunkTileV
	CalculateUV(CurrentTile\Water\Texture,LevelDetail,LevelDetail,CurrentTile\Water\Rotation,4,LevelDetail)
	VertexTexCoords mySurface,3,ChunkTileU,ChunkTileV

	EntityTexture CurrentWaterTile,WaterTexture

End Function

Function BuildCurrentObjectModel()

	BuildObjectModel(CurrentObject,0,0,300+CurrentObject\Position\Z)

	FinalizeCurrentObject()

End Function

Function BuildObjectAccessories(Obj.GameObject)

	If Obj\Model\HatEntity>0

		If Obj\Model\HatTexture=0
			UseErrorColor(Obj\Model\HatEntity)
		Else
			EntityTexture Obj\Model\HatEntity,Obj\Model\HatTexture
		EndIf
		ScaleEntity Obj\Model\HatEntity,Obj\Attributes\YScale*Obj\Attributes\ScaleAdjust,Obj\Attributes\ZScale*Obj\Attributes\ScaleAdjust,Obj\Attributes\XScale*Obj\Attributes\ScaleAdjust

		;RotateEntity Obj\Model\Entity,Obj\Attributes\PitchAdjust,Obj\Attributes\YawAdjust,Obj\Attributes\RollAdjust
;		RotateEntity Obj\Model\HatEntity,0,0,0
;		TurnEntity Obj\Model\HatEntity,Obj\Attributes\PitchAdjust,0,Obj\Attributes\RollAdjust
;		TuObj\Attributes\YawAdjustel,0,Obj\Attributes\YawAdjust-90,0

		;bone=FindChild(Obj\Model\Entity,"hat_bone")

;		PositionEntity Obj\Model\HatEntity,0+Obj\Attributes\XAdjust,300+Obj\AttribuObj\Attributes\YawAdjustectZ+.1+.84*Obj\Attributes\ZScale/.035,0-Obj\Attributes\YAdjust

		TransformAccessoryEntityOntoBone(Obj\Model\HatEntity,Obj\Model\Entity)

	EndIf

	If Obj\Model\AccEntity>0

		If Obj\Model\AccTexture=0
			UseErrorColor(Obj\Model\AccEntity)
		Else
			EntityTexture Obj\Model\AccEntity,Obj\Model\AccTexture
		EndIf
		ScaleEntity Obj\Model\AccEntity,Obj\Attributes\YScale*Obj\Attributes\ScaleAdjust,Obj\Attributes\ZScale*Obj\Attributes\ScaleAdjust,Obj\Attributes\XScale*Obj\Attributes\ScaleAdjust

		;RotateEntity Obj\Model\Entity,Obj\Attributes\PitchAdjust,Obj\Attributes\YawAdjust,Obj\Attributes\RollAdjust
;		RotateEntity Obj\Model\AccEntity,0,0,0
;		TurnEntity Obj\Model\AccEntity,Obj\Attributes\PitchAdjust,0,Obj\Attributes\RollAdjust
;		TurnEntity Obj\Model\AccEntity,0,Obj\Attributes\YawAdjust-90,0

		;bone=FindChild(Obj\Model\Entity,"hat_bone")

		;PositionEntity Obj\Attributes\YawAdjustentAccModel,0+Obj\Attributes\XAdjust,300+Obj\Attributes\ZAdjust+Obj\Position\Z+.1+.84*Obj\Attributes\ZScale/.035,0-Obj\Attributes\YAdjust

		TransformAccessoryEntityOntoBone(Obj\Model\AccEntity,Obj\Model\Entity)

	EndIf

End Function

Function PositionEntityWithXYZAdjust(Entity,x#,y#,z#,Attributes.GameObjectAttributes)

	PositionEntity Entity,x#+Attributes\XAdjust,z#+Attributes\ZAdjust,-y#-Attributes\YAdjust

End Function

Function FinalizeCurrentObject()

	ShowCurrentObjectMoveXYGoal()
	ShowWorldAdjusterPositions()
	CalculateCurrentObjectTargetIDs()
	CalculateCurrentObjectActivateIDs()

End Function

Function ColorToID(Col,SubCol)

	Return 500+5*Col+SubCol

End Function

Function CommandAdjusterStartDataIndex()

	If ObjectAdjusterLogicType\Absolute
		If CurrentObject\Attributes\LogicType=90 And CurrentObject\Attributes\LogicSubType=15 And ObjectAdjusterLogicSubType\Absolute ; General CMD
			Return 0
		ElseIf IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			Return 0
		ElseIf CurrentObject\Attributes\LogicType=242 ; Cuboid
			Return 2
		EndIf
	EndIf

	Return -1

End Function

Function CalculateCurrentObjectTargetIDs()

	;CurrentObjectTargetIDCount=0
	For i=0 To CurrentObjectTargetIDCount-1
		CurrentObjectTargetIDEnabled(i)=False
	Next

	If ObjectAdjusterLogicType\Absolute
		If CurrentObject\Attributes\LogicType=90 ; Buttons
			If ObjectAdjusterLogicSubType\Absolute
				If IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
					;CurrentObjectTargetIDCount=4
					CurrentObjectTargetID(0)=ColorToID(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data4)
					CurrentObjectTargetIDEnabled(0)=ObjectAdjusterData0\Absolute And ObjectAdjusterData4\Absolute
					CurrentObjectTargetID(1)=ColorToID(CurrentObject\Attributes\Data1,CurrentObject\Attributes\Data5)
					CurrentObjectTargetIDEnabled(1)=ObjectAdjusterData1\Absolute And ObjectAdjusterData5\Absolute
					CurrentObjectTargetID(2)=ColorToID(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data6)
					CurrentObjectTargetIDEnabled(2)=ObjectAdjusterData2\Absolute And ObjectAdjusterData6\Absolute
					CurrentObjectTargetID(3)=ColorToID(CurrentObject\Attributes\Data3,CurrentObject\Attributes\Data7)
					CurrentObjectTargetIDEnabled(3)=ObjectAdjusterData3\Absolute And ObjectAdjusterData7\Absolute
				Else If (CurrentObject\Attributes\LogicSubType Mod 32)<10 ; ColorX2Y
					;CurrentObjectTargetIDCount=1
					CurrentObjectTargetID(0)=ColorToID(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data2)
					CurrentObjectTargetIDEnabled(0)=ObjectAdjusterData0\Absolute And ObjectAdjusterData2\Absolute
				Else If (CurrentObject\Attributes\LogicSubType Mod 32)=16 Or (CurrentObject\Attributes\LogicSubType Mod 32)=17 ; Rotator or ???
					;CurrentObjectTargetIDCount=1
					CurrentObjectTargetID(0)=ColorToID(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1)
					CurrentObjectTargetIDEnabled(0)=ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute
				Else If (CurrentObject\Attributes\LogicSubType Mod 32)=15 ; General Command
					If ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute
						CalculateCommandTargetIDs(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1)
					EndIf
				Else If (CurrentObject\Attributes\LogicSubType Mod 32)=11 ; NPC Modifier
					If ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute
						If CurrentObject\Attributes\Data0=2 ; NPC Exclamation
							If CurrentObject\Attributes\Data1<>-1 ; Ignore if targeting the player
								;CurrentObjectTargetIDCount=1
								CurrentObjectTargetID(0)=CurrentObject\Attributes\Data1
								CurrentObjectTargetIDEnabled(0)=True
							EndIf
						Else
							;CurrentObjectTargetIDCount=1
							CurrentObjectTargetID(0)=CurrentObject\Attributes\Data1
							CurrentObjectTargetIDEnabled(0)=True
						EndIf
					EndIf
				EndIf
			EndIf
		ElseIf IsObjectTypeKeyblock(CurrentObject\Attributes\LogicType)
			If ObjectAdjusterData0\Absolute And ObjectAdjusterData1\Absolute
				CalculateCommandTargetIDs(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1)
			EndIf
		ElseIf CurrentObject\Attributes\LogicType=165 ; Arcade Cabinet
			If ObjectAdjusterData0\Absolute
				Data0=CurrentObject\Attributes\Data0
				CurrentObjectTargetID(0)=Data0
				CurrentObjectTargetIDEnabled(0)=True
				TargetCount=1
				While True
					Data0=Data0+1
					If ((Data0-200) Mod 3)=0
						Exit
					Else
						CurrentObjectTargetID(TargetCount)=Data0
						CurrentObjectTargetIDEnabled(TargetCount)=True
						TargetCount=TargetCount+1
					EndIf
				Wend
			EndIf
		ElseIf CurrentObject\Attributes\LogicType=242 ; Cuboid
			If ObjectAdjusterData2\Absolute And ObjectAdjusterData3\Absolute
				CalculateCommandTargetIDs(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)
			EndIf
		EndIf
	EndIf

End Function

Function CalculateCommandTargetIDs(Command,Data1)

	Select Command
	Case 1,2,3,4,5,51,52,61,62,63
		;CurrentObjectTargetIDCount=1
		CurrentObjectTargetID(0)=Data1
		CurrentObjectTargetIDEnabled(0)=True
	Case 16,64
		If Data1<>-1 ; Ignore if targeting the player
			;CurrentObjectTargetIDCount=1
			CurrentObjectTargetID(0)=Data1
			CurrentObjectTargetIDEnabled(0)=True
		EndIf
	End Select

End Function

Function CalculateCurrentObjectActivateIDs()

	;CurrentObjectActivateIdCount=0
	For i=0 To CurrentObjectActivateIDCount-1
		CurrentObjectActivateIDEnabled(i)=False
	Next

	If ObjectAdjusterLogicType\Absolute
		If CurrentObject\Attributes\LogicType=90 Or CurrentObject\Attributes\LogicType=210 ; button or transporter
			;CurrentObjectActivateIdCount=1
			CurrentObjectActivateId(0)=CurrentObject\Attributes\Data8
			CurrentObjectActivateIdEnabled(0)=ObjectAdjusterData8\Absolute
		ElseIf ObjectAdjusterLogicSubType\Absolute
			If IsObjectLogicAutodoor(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
				;CurrentObjectActivateIdCount=3
				CurrentObjectActivateId(0)=CurrentObject\Attributes\Data4
				CurrentObjectActivateIdEnabled(0)=ObjectAdjusterData4\Absolute
				CurrentObjectActivateId(1)=CurrentObject\Attributes\Data5
				CurrentObjectActivateIdEnabled(1)=ObjectAdjusterData5\Absolute
				CurrentObjectActivateId(2)=CurrentObject\Attributes\Data6
				CurrentObjectActivateIdEnabled(2)=ObjectAdjusterData6\Absolute
			EndIf
		EndIf
	EndIf

End Function

Function SetWorldAdjusterPosition(index,x,y)

	ShowEntity WorldAdjusterPositionMarker(index)
	SetEntityPositionInWorld(WorldAdjusterPositionMarker(index),x+0.5,y+0.5,0.0)

End Function

Function ShowWorldAdjusterPositions()

	For i=0 To 3
		HideEntity WorldAdjusterPositionMarker(i)
	Next

	Select CurrentObject\Attributes\LogicType
	Case 50 ; spellball
		If CurrentObject\Attributes\Data0<>-1 And CurrentObject\Attributes\Data1<>-1
			SetWorldAdjusterPosition(0,CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1)
		EndIf
	Case 51,52 ; magic shooter, meteor shooter
		SetWorldAdjusterPosition(0,CurrentObject\Attributes\Data1,CurrentObject\Attributes\Data2)
	Case 80,81,82,83,84,85,86,87 ; Keyblock
		ShowWorldAdjusterPositionsCmd(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1,CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3,CurrentObject\Attributes\Data4)
	Case 90 ; button
		If CurrentObject\Attributes\LogicSubType=10 ; levelexit
			If CurrentObject\Attributes\Data1=CurrentLevelNumber
				SetWorldAdjusterPosition(0,CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)
			EndIf
		ElseIf CurrentObject\Attributes\LogicSubType=11 And CurrentObject\Attributes\Data0=0 ; NPC move
			SetWorldAdjusterPosition(0,CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)
		ElseIf CurrentObject\Attributes\LogicSubType=15 ; general command
			ShowWorldAdjusterPositionsCmd(CurrentObject\Attributes\Data0,CurrentObject\Attributes\Data1,CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3,CurrentObject\Attributes\Data4)
		EndIf
	Case 242 ; cuboid
		ShowWorldAdjusterPositionsCmd(CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3,CurrentObject\Attributes\Data4,CurrentObject\Attributes\Data5,CurrentObject\Attributes\Data6)
	Case 434 ; mothership
		SetWorldAdjusterPosition(0,CurrentObject\Attributes\Data2,CurrentObject\Attributes\Data3)
		SetWorldAdjusterPosition(1,CurrentObject\Attributes\Data4,CurrentObject\Attributes\Data5)
		SetWorldAdjusterPosition(2,CurrentObject\Attributes\Data6,CurrentObject\Attributes\Data7)
		SetWorldAdjusterPosition(3,CurrentObject\Attributes\Data8,CurrentObject\Attributes\Data9)
	End Select

End Function

Function ShowWorldAdjusterPositionsCmd(Cmd,Data1,Data2,Data3,Data4)

	Select Cmd
	Case 7,120
		If Data1=CurrentLevelNumber
			SetWorldAdjusterPosition(0,Data2,Data3)
		EndIf
	Case 11
		SetWorldAdjusterPosition(0,Data2,Data3)
	Case 17,18
		SetWorldAdjusterPosition(0,Data1,Data2)
	Case 41,42
		SetWorldAdjusterPosition(0,Data1,Data2)
		SetWorldAdjusterPosition(1,Data3,Data4)
	Case 61
		SetWorldAdjusterPosition(0,Data2,Data3)
	End Select

End Function

Function ShowCurrentObjectMoveXYGoal()

	; Check if we're using a pathfinding MovementType.
	If (ObjectAdjusterMovementType\Absolute And CurrentObject\Attributes\MovementType>9 And CurrentObject\Attributes\MovementType<19) Or (ObjectAdjusterMoveXGoal\Absolute And ObjectAdjusterMoveYGoal\Absolute And (CurrentObject\Attributes\MoveXGoal<>0 Or CurrentObject\Attributes\MoveYGoal<>0))
		ShowEntity CurrentObjectMoveXYGoalMarker
		SetEntityPositionInWorld(CurrentObjectMoveXYGoalMarker,CurrentObject\Attributes\MoveXGoal+0.5,CurrentObject\Attributes\MoveYGoal+0.5,0.0)
	Else
		HideEntity CurrentObjectMoveXYGoalMarker
	EndIf

End Function

Function SetCurrentObjectTargetLocation(x,y)

	Select CurrentObject\Attributes\LogicType
	Case 50 ; spellball
		CurrentObject\Attributes\Data0=x
		CurrentObject\Attributes\Data1=y
		CurrentObjectWasChanged()
	Case 51,52 ; magic shooter, meteor shooter
		CurrentObject\Attributes\Data1=x
		CurrentObject\Attributes\Data2=y
		CurrentObjectWasChanged()
	Case 80,81,82,83,84,85,86,87 ; Keyblock
		SetCurrentObjectTargetLocationCmd(CurrentObject\Attributes\Data0,1,2,3,4,x,y)
	Case 90 ; button
		If CurrentObject\Attributes\LogicSubType=10 ; levelexit
			CalculateLevelExitTo(1,2,3,4,CurrentLevelNumber,x,y)
			CurrentObjectWasChanged()
		ElseIf CurrentObject\Attributes\LogicSubType=11 And CurrentObject\Attributes\Data0=0 ; NPC move
			CurrentObject\Attributes\Data2=x
			CurrentObject\Attributes\Data3=y
			CurrentObjectWasChanged()
		ElseIf CurrentObject\Attributes\LogicSubType=15 ; general command
			SetCurrentObjectTargetLocationCmd(CurrentObject\Attributes\Data0,1,2,3,4,x,y)
		Else
			GenerateLevelExitTo(CurrentLevelNumber,x,y)
		EndIf
	Case 242 ; cuboid
		SetCurrentObjectTargetLocationCmd(CurrentObject\Attributes\Data2,3,4,5,6,x,y)
	Default
		GenerateLevelExitTo(CurrentLevelNumber,x,y)
	End Select

	; Necessary for if the changing Data values modify an object's appearance.
	BuildCurrentObjectModel()

End Function

Function SetCurrentObjectTargetLocationCmd(Cmd,D1,D2,D3,D4,x,y)

	Select Cmd
	Case 7,120
		CalculateLevelExitTo(D1,D2,D3,D4,CurrentLevelNumber,x,y)
		CurrentObjectWasChanged()
	Case 17,18
		SetDataByIndex(CurrentObject\Attributes,D1,x)
		SetDataByIndex(CurrentObject\Attributes,D2,y)
		CurrentObjectWasChanged()
	Case 11,61
		SetDataByIndex(CurrentObject\Attributes,D2,x)
		SetDataByIndex(CurrentObject\Attributes,D3,y)
		CurrentObjectWasChanged()
	Default
		GenerateLevelExitTo(CurrentLevelNumber,x,y)
	End Select

End Function

Function CalculateLevelExitTo(D1,D2,D3,D4,level,x,y)

	SetDataByIndex(CurrentObject\Attributes,D1,level)
	SetDataByIndex(CurrentObject\Attributes,D2,x)
	SetDataByIndex(CurrentObject\Attributes,D3,y)

	StartingYaw=0 ; south
	; examine surroundings to infer player facing direction
	For i=0 To NofObjects-1
		If LevelObjects(i)\Attributes\LogicType=90 And LevelObjects(i)\Attributes\LogicSubType=10 ; LevelExit
			TileX=LevelObjects(i)\Position\TileX
			TileY=LevelObjects(i)\Position\TileY
			If x=TileX
				If y+1=TileY
					; player face north
					StartingYaw=180
				EndIf
			ElseIf x+1=TileX
				If y-1=TileY
					; player face southwest
					StartingYaw=45
				ElseIf y=TileY
					; player face west
					StartingYaw=90
				ElseIf y+1=TileY
					; player face northwest
					StartingYaw=135
				EndIf
			ElseIf x-1=TileX
				If y-1=TileY
					; player face southeast
					StartingYaw=315
				ElseIf y=TileY
					; player face east
					StartingYaw=270
				ElseIf y+1=TileY
					; player face northeast
					StartingYaw=225
				EndIf
			EndIf
		EndIf
	Next

	SetDataByIndex(CurrentObject\Attributes,D4,StartingYaw)

	If CurrentObject\Attributes\LogicType=90 And CurrentObject\Attributes\LogicSubType=10 ; LevelExit
		CurrentObject\Attributes\YawAdjust=180-StartingYaw
		If CurrentObject\Attributes\YawAdjust<0
			CurrentObject\Attributes\YawAdjust=CurrentObject\Attributes\YawAdjust+360
		EndIf
	EndIf

End Function

Function GenerateLevelExitTo(level,x,y)

	BlankObjectPreset("!Button",90,10)
	CalculateLevelExitTo(1,2,3,4,level,x,y)
	ClearObjectSelection()

End Function

Function ResetLevel()

	ClearObjectSelection()

	For i=0 To MaxLevelCoordinate
		For j=0 To MaxLevelCoordinate
			LevelTiles(i,j)\Terrain\Texture=0 ; corresponding to squares in LevelTexture
			LevelTiles(i,j)\Terrain\Rotation=0 ; 0-3 , and 4-7 for "flipped"
			LevelTiles(i,j)\Terrain\SideTexture=13 ; texture for extrusion walls
			LevelTiles(i,j)\Terrain\SideRotation=0 ; 0-3 , and 4-7 for "flipped"
			LevelTiles(i,j)\Terrain\Random=0 ; random height pertubation of tile
			LevelTiles(i,j)\Terrain\Height=0 ; height of "center" - e.g. to make ditches and hills
			LevelTiles(i,j)\Terrain\Extrusion=0; extrusion with walls around it
			LevelTiles(i,j)\Terrain\Rounding=0; 0-no, 1-yes: are floors rounded if on a drop-off corner
			LevelTiles(i,j)\Terrain\EdgeRandom=0; 0-no, 1-yes: are edges rippled
			LevelTiles(i,j)\Terrain\Logic=0
		Next
	Next

	For i=0 To MaxLevelCoordinate
		For j=0 To MaxLevelCoordinate
			LevelTiles(i,j)\Water\Height=-0.2
			LevelTiles(i,j)\Water\Texture=0
			LevelTiles(i,j)\Water\Rotation=0
			LevelTiles(i,j)\Water\Turbulence=0.1
		Next
	Next

	ResetParticles("data/graphics/particles.bmp")

End Function

Function CopyLevel()

	For i=0 To MaxLevelCoordinate
		For j=0 To MaxLevelCoordinate
			CopyTile(LevelTiles(i,j),CopyLevelTiles(i,j))
		Next
	Next

End Function

Function CopyTile(Source.Tile,Dest.Tile)

	Dest.Tile\Terrain\Texture=Source.Tile\Terrain\Texture
	Dest.Tile\Terrain\Rotation=Source.Tile\Terrain\Rotation
	Dest.Tile\Terrain\SideTexture=Source.Tile\Terrain\SideTexture
	Dest.Tile\Terrain\SideRotation=Source.Tile\Terrain\SideRotation
	Dest.Tile\Terrain\Random=Source.Tile\Terrain\Random
	Dest.Tile\Terrain\Height=Source.Tile\Terrain\Height
	Dest.Tile\Terrain\Extrusion=Source.Tile\Terrain\Extrusion
	Dest.Tile\Terrain\Rounding=Source.Tile\Terrain\Rounding
	Dest.Tile\Terrain\EdgeRandom=Source.Tile\Terrain\EdgeRandom
	Dest.Tile\Terrain\Logic=Source.Tile\Terrain\Logic

	Dest.Tile\Water\Height=Source.Tile\Water\Height
	Dest.Tile\Water\Texture=Source.Tile\Water\Texture
	Dest.Tile\Water\Rotation=Source.Tile\Water\Rotation
	Dest.Tile\Water\Turbulence=Source.Tile\Water\Turbulence

End Function

Function SwapTiles(Tile1.Tile,Tile2.Tile)

	CopyTile(Tile1,TempTile)
	CopyTile(Tile2,Tile1)
	CopyTile(TempTile,Tile2)

End Function

Function CopyLevelTile(SourceX,SourceY,DestX,DestY)

	CopyTile(CopyLevelTiles(SourceX,SourceY),LevelTiles(DestX,DestY))

End Function

Function PasteLevelFromCopy()

	ResetLevel()
	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			CopyLevelTile(i,j,i,j)
		Next
	Next

End Function

Function ReSizeLevel()

	If LevelWidth+WidthLeftChange>MaxLevelSize
		WidthLeftChange=MaxLevelSize-LevelWidth
	EndIf
	If LevelWidth+WidthRightChange>MaxLevelSize
		WidthRightChange=MaxLevelSize-LevelWidth
	EndIf
	If LevelHeight+HeightTopChange>MaxLevelSize
		HeightTopChange=MaxLevelSize-LevelHeight
	EndIf
	If LevelHeight+HeightBottomChange>MaxLevelSize
		HeightBottomChange=MaxLevelSize-LevelHeight
	EndIf

	CopyLevel()
	ResetLevel()
	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			If (WidthLeftChange>=0 Or i>=-WidthLeftChange) And (HeightTopChange>=0 Or j>=-HeightTopChange)
				CopyLevelTile(i,j,i+WidthLeftChange,j+HeightTopChange)
			EndIf
		Next
	Next

	LevelWidthOld=LevelWidth
	LevelHeightOld=LevelHeight
	LevelWidth=LevelWidth+WidthLeftChange+WidthRightChange
	LevelHeight=LevelHeight+HeightTopChange+HeightBottomChange

	; and edge
	If BorderExpandOption=0
		; use current
		If WidthLeftChange>0
			For j=0 To LevelHeightOld-1
				For k=0 To WidthLeftChange-1
					ChangeLevelTile(k,j,False)
				Next
			Next
		EndIf
		If WidthRightChange>0
			For j=0 To LevelHeightOld-1
				For k=0 To WidthRightChange-1
					ChangeLevelTile(LevelWidthOld+k,j,False)
				Next
			Next
		EndIf
		If HeightTopChange>0
			For i=0 To LevelWidthOld-1
				For k=0 To HeightTopChange-1
					ChangeLevelTile(i,k,False)
				Next
			Next
		EndIf
		If HeightBottomChange>0
			For i=0 To LevelWidthOld-1
				For k=0 To HeightBottomChange-1
					ChangeLevelTile(i,LevelHeightOld+k,False)
				Next
			Next
		EndIf
	EndIf

	If BorderExpandOption=1
		; use duplicate
		If WidthLeftChange>0
			For j=0 To LevelHeightOld-1
				For k=0 To WidthLeftChange-1
					CopyLevelTile(0,j,k,j)
				Next
			Next
		EndIf
		If WidthRightChange>0
			For j=0 To LevelHeightOld-1
				For k=0 To WidthRightChange-1
					CopyLevelTile(LevelWidthOld-1,j,LevelWidthOld+k,j)
				Next
			Next
		EndIf
		If HeightTopChange>0
			For i=0 To LevelWidthOld-1
				For k=0 To HeightTopChange-1
					CopyLevelTile(i,0,i,k)
				Next
			Next
		EndIf
		If HeightBottomChange>0
			For i=0 To LevelWidthOld-1
				For k=0 To HeightBottomChange-1
					CopyLevelTile(i,LevelHeightOld-1,i,LevelHeightOld+k)
				Next
			Next
		EndIf
	EndIf

	;LevelWidth=LevelWidth+WidthLeftChange+WidthRightChange
	;LevelHeight=LevelHeight+HeightTopChange+HeightBottomChange
	ReBuildLevelModel()
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			UpdateTile(i,j)
		Next
	Next

	; and move the object
	If WidthLeftChange<>0
		For i=0 To NofObjects-1
			NewPosition#=LevelObjects(i)\Position\X+WidthLeftChange

			If Floor(NewPosition#)<0 Or Floor(NewPosition#)>100
				DeleteObject(i)
				i=i-1
			Else
				LevelObjects(i)\Position\X=NewPosition#
				ChangeObjectTileX(i,LevelObjects(i)\Position\TileX+WidthLeftChange) ; Also handles spellballs etc.
				ResizeLevelFixObjectTargets(LevelObjects(i))
				UpdateObjectPosition(i)
			EndIf
		Next
		RecalculateDragSize()
	EndIf
	If HeightTopChange<>0
		For i=0 To NofObjects-1
			NewPosition#=LevelObjects(i)\Position\Y+HeightTopChange

			If Floor(NewPosition#)<0 Or Floor(NewPosition#)>100
				DeleteObject(i)
				i=i-1
			Else
				LevelObjects(i)\Position\Y=NewPosition#
				ChangeObjectTileY(i,LevelObjects(i)\Position\TileY+HeightTopChange)
				ResizeLevelFixObjectTargets(LevelObjects(i))
				UpdateObjectPosition(i)
			EndIf
		Next
		RecalculateDragSize()
	EndIf

	ResizeLevelFixObjectTargets(CurrentObject)

	WidthLeftChange=0
	WidthRightChange=0
	HeightTopChange=0
	HeightBottomChange=0

	;SomeTileWasChanged()

	ObjectsWereChanged()
	AddUnsavedChange()

End Function

Function ResizeLevelFixObjectTargets(Obj.GameObject)

	If Obj\Attributes\MoveXGoal<>0
		Obj\Attributes\MoveXGoal=Obj\Attributes\MoveXGoal+WidthLeftChange
	EndIf
	If Obj\Attributes\MoveYGoal<>0
		Obj\Attributes\MoveYGoal=Obj\Attributes\MoveYGoal+HeightTopChange
	EndIf

	Select Obj\Attributes\LogicType
	Case 50 ; spellball
		Obj\Attributes\Data0=Obj\Attributes\Data0+WidthLeftChange
		Obj\Attributes\Data1=Obj\Attributes\Data1+HeightTopChange
	Case 51,52 ; magic shooter, meteor shooter
		Obj\Attributes\Data1=Obj\Attributes\Data1+WidthLeftChange
		Obj\Attributes\Data2=Obj\Attributes\Data2+HeightTopChange
	Case 80,81,82,83,84,85,86,87 ; Keyblock
		ResizeLevelFixObjectTargetsCmd(Obj\Attributes,Obj\Attributes\Data0,1,2,3,4)
	Case 90 ; button
		If Obj\Attributes\LogicSubType=10 ; levelexit
			If Obj\Attributes\Data1=CurrentLevelNumber
				Obj\Attributes\Data2=Obj\Attributes\Data2+WidthLeftChange
				Obj\Attributes\Data3=Obj\Attributes\Data3+HeightTopChange
			EndIf
		ElseIf Obj\Attributes\LogicSubType=11 And Obj\Attributes\Data0=0 ; NPC move
			Obj\Attributes\Data2=Obj\Attributes\Data2+WidthLeftChange
			Obj\Attributes\Data3=Obj\Attributes\Data3+HeightTopChange
		ElseIf CurrentObject\Attributes\LogicSubType=15 ; general command
			ResizeLevelFixObjectTargetsCmd(Obj\Attributes,Obj\Attributes\Data0,1,2,3,4)
		EndIf
	Case 242 ; cuboid
		ResizeLevelFixObjectTargetsCmd(Obj\Attributes,Obj\Attributes\Data2,3,4,5,6)
	Case 434 ; mothership
		Obj\Attributes\Data2=Obj\Attributes\Data2+WidthLeftChange
		Obj\Attributes\Data3=Obj\Attributes\Data3+HeightTopChange
		Obj\Attributes\Data4=Obj\Attributes\Data4+WidthLeftChange
		Obj\Attributes\Data5=Obj\Attributes\Data5+HeightTopChange
		Obj\Attributes\Data6=Obj\Attributes\Data6+WidthLeftChange
		Obj\Attributes\Data7=Obj\Attributes\Data7+HeightTopChange
		Obj\Attributes\Data8=Obj\Attributes\Data8+WidthLeftChange
		Obj\Attributes\Data9=Obj\Attributes\Data9+HeightTopChange
	End Select

End Function

Function ResizeLevelFixObjectTargetsCmd(Attributes.GameObjectAttributes,Cmd,D1,D2,D3,D4)

	Select Cmd
	Case 7
		If GetDataByIndex(Attributes,D1)=CurrentLevelNumber
			SetDataByIndex(Attributes,D2,GetDataByIndex(Attributes,D2)+WidthLeftChange)
			SetDataByIndex(Attributes,D3,GetDataByIndex(Attributes,D3)+HeightTopChange)
		EndIf
	Case 11,61
		SetDataByIndex(Attributes,D2,GetDataByIndex(Attributes,D2)+WidthLeftChange)
		SetDataByIndex(Attributes,D3,GetDataByIndex(Attributes,D3)+HeightTopChange)
	Case 41,42
		SetDataByIndex(Attributes,D1,GetDataByIndex(Attributes,D1)+WidthLeftChange)
		SetDataByIndex(Attributes,D2,GetDataByIndex(Attributes,D2)+HeightTopChange)
		SetDataByIndex(Attributes,D3,GetDataByIndex(Attributes,D3)+WidthLeftChange)
		SetDataByIndex(Attributes,D4,GetDataByIndex(Attributes,D4)+HeightTopChange)
	End Select

End Function

Function RawSetObjectTileX(i,tilex)

	Obj.GameObject=LevelObjects(i)
	Obj\Position\TileX=tilex
	Obj\Position\TileX2=tilex

	If Obj\Attributes\LogicType=50 ; spellball
		Obj\Attributes\Data2=Obj\Position\TileX
		Obj\Attributes\Data4=Obj\Position\TileX
		If CurrentObject\Attributes\LogicType=50 And (IsOnlyObjectSelected(i) Or i=NofObjects)
			CurrentObject\Attributes\Data2=Obj\Attributes\Data2
			CurrentObject\Attributes\Data4=Obj\Attributes\Data4
		EndIf
	EndIf

End Function

Function RawSetObjectTileY(i,tiley)

	Obj.GameObject=LevelObjects(i)
	Obj\Position\TileY=tiley
	Obj\Position\TileY2=tiley

	If Obj\Attributes\LogicType=50 ; spellball
		Obj\Attributes\Data3=Obj\Position\TileY
		Obj\Attributes\Data5=Obj\Position\TileY
		If CurrentObject\Attributes\LogicType=50 And (IsOnlyObjectSelected(i) Or i=NofObjects)
			CurrentObject\Attributes\Data3=Obj\Attributes\Data3
			CurrentObject\Attributes\Data5=Obj\Attributes\Data5
		EndIf
	EndIf

End Function

Function SetObjectTileX(i,tilex)

	RawSetObjectTileX(i,tilex)
	IncrementLevelTileObjectCountFor(LevelObjects(i)\Position)

End Function

Function SetObjectTileY(i,tiley)

	RawSetObjectTileY(i,tiley)
	IncrementLevelTileObjectCountFor(LevelObjects(i)\Position)

End Function

Function SetObjectTileXY(i,tilex,tiley)

	RawSetObjectTileX(i,tilex)
	RawSetObjectTileY(i,tiley)
	IncrementLevelTileObjectCountFor(LevelObjects(i)\Position)

End Function

Function ChangeObjectTileX(i,tilex)

	DecrementLevelTileObjectCountFor(LevelObjects(i)\Position)
	SetObjectTileX(i,tilex)

End Function

Function ChangeObjectTileY(i,tiley)

	DecrementLevelTileObjectCountFor(LevelObjects(i)\Position)
	SetObjectTileY(i,tiley)

End Function

Function ChangeObjectTileXY(i,tilex,tiley)

	DecrementLevelTileObjectCountFor(LevelObjects(i)\Position)
	SetObjectTileXY(i,tilex,tiley)

End Function

Function FlipLevelX()

	CopyLevel()
	ResetLevel()
	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			CopyLevelTile(LevelWidth-1-i,j,i,j)
		Next
	Next
	ReBuildLevelModel()

	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			UpdateTile(i,j)
		Next
	Next

	; and move the object

	For i=0 To NofObjects-1
		LevelObjects(i)\Position\X=Float(LevelWidth)-LevelObjects(i)\Position\X

		ChangeObjectTileX(i,LevelWidth-1-LevelObjects(i)\Position\TileX)

		UpdateObjectPosition(i)
	Next

	;SomeTileWasChanged()
	ObjectsWereChanged()
	AddUnsavedChange()

End Function

Function FlipLevelY()

	CopyLevel()
	ResetLevel()
	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			CopyLevelTile(i,LevelHeight-1-j,i,j)
		Next
	Next
	ReBuildLevelModel()

	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			UpdateTile(i,j)
		Next
	Next

	; and move the object

	For i=0 To NofObjects-1
		LevelObjects(i)\Position\Y=Float(LevelHeight)-LevelObjects(i)\Position\Y

		ChangeObjectTileY(i,LevelHeight-1-LevelObjects(i)\Position\TileY)

		UpdateObjectPosition(i)
	Next

	;SomeTileWasChanged()
	ObjectsWereChanged()
	AddUnsavedChange()

End Function

Function FlipLevelXY()

	CopyLevel()
	ResetLevel()
	For i=0 To LevelWidth-1
		For j=0 To LevelHeight-1
			CopyLevelTile(i,j,j,i)
		Next
	Next
	x=LevelWidth
	LevelWidth=LevelHeight
	LevelHeight=x

	ReBuildLevelModel()

	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			UpdateTile(i,j)
		Next
	Next

	; and move the object

	For i=0 To NofObjects-1
		x2#=LevelObjects(i)\Position\X
		LevelObjects(i)\Position\X=LevelObjects(i)\Position\Y
		LevelObjects(i)\Position\Y=x2#
		ChangeObjectTileXY(i,LevelObjects(i)\Position\TileY,LevelObjects(i)\Position\TileX)

		UpdateObjectPosition(i)
	Next

	;SomeTileWasChanged()
	ObjectsWereChanged()
	AddUnsavedChange()

End Function

Function CalculateUV(Texture,i2,j2,Rotation,size,Detail)

	; calculuates UV coordinates of a point on "texture" (0-7... ie the field on the 256x256 big texture)
	; at position i2/j2 (with resolution Detail) and given Rotation (0-7)

	; returns results as Globals ChunkTileU/ChunkTileV
	uoverlap#=0
	voverlap#=0

	If j2=0 Or j2=1 Or i2=0 Or i2=1
		If i2=0
			uoverlap#=.001
		Else If i2=1
			uoverlap#=-.001
		Else
			uoverlap#=0
		EndIf
		If j2=0
			voverlap#=.001
		Else If j2=1
			voverlap#=-.001
		Else
			voverlap#=0
		EndIf
	EndIf

	Select Rotation
	Case 0
		ChunkTileu#=Float((Texture Mod size))/size+(Float(i2)/Float(Detail))/size+uoverlap
		ChunkTilev#=Float((Texture)/size)/size+(Float(j2)/Float(Detail))/size+voverlap
	Case 1
		ChunkTileu#=Float(((Texture Mod size)+0))/size+(Float(j2)/Float(Detail))/size+voverlap
		ChunkTilev#=Float(((Texture)/size)+1)/size-(Float(i2)/Float(Detail))/size-uoverlap
	Case 2
		ChunkTileu#=Float(((Texture Mod size)+1))/size-(Float(i2)/Float(Detail))/size-uoverlap
		ChunkTilev#=Float(((Texture)/size)+1)/size-(Float(j2)/Float(Detail))/size-voverlap
	Case 3
		ChunkTileu#=Float(((Texture Mod size))+1)/size-(Float(j2)/Float(Detail))/size-voverlap
		ChunkTilev#=Float((Texture)/size)/size+(Float(i2)/Float(Detail))/size+uoverlap
	Case 4
		ChunkTileu#=Float(((Texture Mod size))+1)/size-(Float(i2)/Float(Detail))/size-uoverlap
		ChunkTilev#=Float((Texture)/size)/size+(Float(j2)/Float(Detail))/size+voverlap
	Case 7
		ChunkTileu#=Float(((Texture Mod size))+0)/size+(Float(j2)/Float(Detail))/size+voverlap
		ChunkTilev#=Float(((Texture)/size)+0)/size+(Float(i2)/Float(Detail))/size+uoverlap
	Case 6
		ChunkTileu#=Float(((Texture Mod size)+0))/size+(Float(i2)/Float(Detail))/size+uoverlap
		ChunkTilev#=Float(((Texture)/size)+1)/size-(Float(j2)/Float(Detail))/size-voverlap
	Case 5
		ChunkTileu#=Float(((Texture Mod size))+1)/size-(Float(j2)/Float(Detail))/size-voverlap
		ChunkTilev#=Float(((Texture)/size)+1)/size-(Float(i2)/Float(Detail))/size-uoverlap

	Default
		ChunkTileu#=Float((Texture Mod size))/size+(Float(i2)/Float(Detail))/size+uoverlap
		ChunkTilev#=Float((Texture)/size)/size+(Float(j2)/Float(Detail))/size+voverlap
	End Select

End Function

Function GetLevelEdgeStyleChar$(Value)

	Select Value
	Case 1
		Return "-"
	Case 2
		Return "B"
	Case 3
		Return "X"
	Case 4
		Return "N"
	Default
		Return Value
	End Select

End Function

Function GetLevelEdgeStyleName$(Value)

	Select Value
	Case 1
		Return "Default"
	Case 2
		Return "Border"
	Case 3
		Return "Border X"
	Case 4
		Return "None"
	Default
		Return Value
	End Select

End Function

Function GetBrushModeName$(Value)

	Select Value
	Case BrushModeNormal
		Return "NORMAL"
	Case BrushModeBlock
		Return "BLOCK"
	Case BrushModeBlockPlacing
		Return ">BLOCK<"
	Case BrushModeFill
		Return "FILL"
	Case BrushModeInlineSoft
		Return "INLINE SOFT"
	Case BrushModeInlineHard
		Return "INLINE HARD"
	Case BrushModeOutlineSoft
		Return "OUTLINE SOFT"
	Case BrushModeOutlineHard
		Return "OUTLINE HARD"
	Case BrushModeRow
		Return "ROW"
	Case BrushModeColumn
		Return "COLUMN"
	Case BrushModeDiagonalNE
		Return "DIAGONAL NE"
	Case BrushModeDiagonalSE
		Return "DIAGONAL SE"
	Case BrushModeTestLevel
		Return "TEST LEVEL"
	Case BrushModeSetMirror
		If DupeMode=DupeModeXPlusY
			Return "SET AXES"
		Else
			Return "SET AXIS"
		EndIf
	Default
		Return "UNKNOWN"
	End Select

End Function

Function GetBrushModeColor$(Value,index)

	If Value=BrushModeFill
		; yellow
		r=255
		g=255
		b=0
	ElseIf Value=BrushModeBlock Or Value=BrushModeBlockPlacing
		; blue
		r=0
		g=255
		b=255
	ElseIf Value=BrushModeInlineSoft
		; orange
		r=255
		g=80
		b=0
	ElseIf Value=BrushModeInlineHard
		; red
		r=255
		g=0
		b=0
	ElseIf Value=BrushModeOutlineHard
		; indigo
		r=0
		g=0
		b=255
	ElseIf Value=BrushModeOutlineSoft
		; lighter indigo
		r=0
		g=80
		b=255
	ElseIf Value=BrushModeRow Or Value=BrushModeColumn Or Value=BrushModeDiagonalNE Or Value=BrushModeDiagonalSE
		; green
		r=0
		g=255
		b=0
	ElseIf Value=BrushModeTestLevel
		; rainbow
		r=GetAnimatedRainbowRed()
		g=GetAnimatedRainbowGreen()
		b=GetAnimatedRainbowBlue()
	ElseIf Value=BrushModeSetMirror
		; gray
		r=140
		g=140
		b=140
	Else ; normal brush mode, AKA BrushModeNormal
		; white
		r=255
		g=255
		b=255
	EndIf

	If index=0
		Return r
	ElseIf index=1
		Return g
	Else
		Return b
	EndIf

End Function

Function GetAnimatedRainbowRed()
	Return 128+120*Sin(Leveltimer Mod 360)
End Function
Function GetAnimatedRainbowGreen()
	Return 128+120*Cos(Leveltimer Mod 360)
End Function
Function GetAnimatedRainbowBlue()
	Return 128-120*Sin(Leveltimer Mod 360)
End Function

Function GetAnimatedFlashing(Timer)
	Return 150+105*Sin(Timer*8)
End Function

Function ChangeBrushModeByDelta(Delta)

	SetBrushMode(BrushMode+Delta)
	While BrushMode=BrushModeBlockPlacing
		SetBrushMode(BrushMode+Delta)
	Wend

	If BrushMode<0
		SetBrushMode(MaxBrushMode)
	ElseIf BrushMode>MaxBrushMode
		SetBrushMode(0)
	EndIf

End Function

Function DefensePowerToSoundId(Value)

	Select Value
	Case 0,1,2,3,4,5,6,7,8,9
		Return 190+Value
	Case 10,11,12,13,14,15,16,17,18
		Return 40+Value
	Case 19,20,21
		Return 74-19+Value
	Case 22
		Return 141 ;zbot
	Case 23
		Return 145 ;zbot
	Case 24
		Return 149 ;zbot

	Case 25
		Return 86 ; chomper
	Case 26
		Return 107 ; thwart
	Case 27
		Return 87
	Case 28
		Return 113 ; troll
	Case 29
		Return 114 ; troll2
	Case 30
		Return 104 ; monster
	Case 31
		Return 187
	Case 32
		Return 188
	Case 33
		Return 189
	Default
		Return 0
	End Select

End Function

Function GetDupeModeName$(Value)

	Select Value
	Case DupeModeNormal
		Return "NO DUPE"
	Case DupeModeX
		Return "DUPE X"
	Case DupeModeY
		Return "DUPE Y"
	Case DupeModeXPlusY
		Return "DUPE X+Y"
	Default
		Return "DUPE UNKNOWN"
	End Select

End Function

Function GetAdventureName$(AdventureId)

	If HubMode=True
		If AdventureId<0 Or AdventureId>HubAdvMax
			Return "< INVALID ADVENTURE ID >"
		Else
			If HubAdventuresFilenames$(AdventureId)=""
				Return "< ADVENTURE DOES NOT EXIST >"
			Else
				Return HubAdventuresFilenames$(AdventureId)
			EndIf
		EndIf
	Else
		Return "( USE HUB MODE TO SEE ADVENTURE NAMES )"
	EndIf

End Function

Function GetAccessoryName$(AccessoryId)

	Select AccessoryId
	Case 0
		Return "None"
	Case 1
		Return "Cap"
	Case 2
		Return "Top Hat"
	Case 3
		Return "Builder"
	Case 4
		Return "Farmer"
	Case 5
		Return "Wizard"
	Case 6
		Return "Bowler"
	Case 7
		Return "BaseBall"
	Case 8
		Return "Beanie"
	Case 9
		Return "Crown"
	Case 10
		Return "Cape"
	Case 11
		Return "Clown"
	Case 12
		Return "Jewels"
	Case 13
		Return "Feather"
	Case 14
		Return "Flowerpot"
	Case 15
		Return "SillyBase"
	Case 16
		Return "Pirate"
	Case 17
		Return "Safari"
	Case 18
		Return "RobinHood"
	Case 19
		Return "Snowball"
	Case 20
		Return "Sombrero"
	Case 21
		Return "ZBot"
	Case 22
		Return "Santa"
	Case 23
		Return "Captain"
	Case 24
		Return "Bicorn"
	Case 25
		Return "Cowboy"
	Case 26
		Return "FlatRed"
	Case 27
		Return "Flower1"
	Case 28
		Return "Flower2"
	Case 29
		Return "Legion"
	Case 30
		Return "Hat-Ring"
	Case 31
		Return "BandRing1"
	Case 32
		Return "BandRing2"
	Case 33
		Return "Fedora"
	Case 34
		Return "Leaf"
	Case 35
		Return "Nest"
	Case 36
		Return "Pirate1"
	Case 37
		Return "Pirate2"
	Case 38
		Return "Sailor1"
	Case 39
		Return "Sailor2"
	Case 40
		Return "Wrap"
	Case 41
		Return "Sunhat"
	Case 42
		Return "Helmet"
	Case 43
		Return "Fez"
	Case 44
		Return "Sunhat2"
	Case 45
		Return "Chef"
	Case 46
		Return "Bowtie"
	Case 47
		Return "Helmet2"
	Case 48
		Return "Headphone"
	Case 49
		Return "Viking"
	Case 50
		Return "Welder"
	Case 51
		Return "Punk"
	Case 52
		Return "Ninja"
	Case 53
		Return "Bike"
	Case 54
		Return "RainbwCap"
	Case 55
		Return "Antenna"
	Case 56
		Return "Janet"
	Case 101
		Return "Thick Frame"
	Case 102
		Return "Thin Large"
	Case 103
		Return "Eyepatch L"
	Case 104
		Return "Eyepatch R"
	Case 105
		Return "Goggles"
	Case 106
		Return "Parrot"
	Case 107
		Return "Square"
	Case 108
		Return "Round"
	Case 109
		Return "Pink"
	Case 110
		Return "Sword"
	Case 111
		Return "Moustache"
	Case 112
		Return "Rose"
	Case 113
		Return "3D"
	Case 114
		Return "Bolt"
	Case 115
		Return "Monocle"
	Case 116
		Return "Bowtie"
	Default
		Return "NotVanilla"
	End Select

End Function

Function GetAccessoryColorName$(AccessoryId,ColorId)

	Select AccessoryId
	Case 1
		Select ColorId
		Case 1
			Return "Blue"
		Case 2
			Return "Rainbow"
		Case 3
			Return "Red"
		Case 4
			Return "Green"
		Case 5
			Return "Orange"
		Case 6
			Return "LightBlue"
		Case 7
			Return "Purple"
		Default
			Return "NotVanilla"
		End Select
	Case 2
		Select ColorId
		Case 1
			Return "Blue"
		Case 2
			Return "Purple"
		Case 3
			Return "Red"
		Case 4
			Return "Green"
		Case 5
			Return "Orange"
		Default
			Return "NotVanilla"
		End Select
	Case 3
		Select ColorId
		Case 1
			Return "Red"
		Case 2
			Return "Green"
		Case 3
			Return "Blue"
		Default
			Return "NotVanilla"

		End Select
	Case 5
		Select ColorId
		Case 1
			Return "Red"
		Case 2
			Return "Orange"
		Case 3
			Return "Yellow"
		Case 4
			Return "Green"
		Case 5
			Return "Blue"
		Case 6
			Return "Indigo"
		Case 7
			Return "Purple"
		Default
			Return "NotVanilla"

		End Select
	Case 6
		Select ColorId
		Case 1
			Return "Black"
		Case 2
			Return "Blue"
		Case 3
			Return "Red"
		Default
			Return "NotVanilla"

		End Select
	Case 7
		Select ColorId
		Case 1
			Return "WS"
		Case 2
			Return "Red"
		Case 3
			Return "Blue S"
		Default
			Return "NotVanilla"

		End Select
	Case 10
		Select ColorId
		Case 1
			Return "Blue"
		Case 2
			Return "Purple"
		Default
			Return "NotVanilla"

		End Select
	Case 27
		Select ColorId
		Case 1
			Return "Red"
		Case 2
			Return "Purple"
		Case 3
			Return "Gold"
		Case 4
			Return "Green"
		Default
			Return "NotVanilla"

		End Select
	Case 28
		Select ColorId
		Case 1
			Return "RedYel"
		Case 2
			Return "YelGreen"
		Case 3
			Return "BluePurp"
		Case 4
			Return "PurpRed"
		Default
			Return "NotVanilla"

		End Select

	Case 46
		Select ColorId
		Case 1
			Return "RedPink"
		Case 2
			Return "BlueGold"
		Case 3
			Return "GreeWhit"
		Case 4
			Return "Fall"
		Case 5
			Return "Frosty"
		Case 6
			Return "FullPink"
		Default
			Return "NotVanilla"

		End Select

	Case 101
		Select ColorId
		Case 1
			Return "Normal"
		Case 2
			Return "Sunglass"
		Default
			Return "NotVanilla"

		End Select
	Case 102
		Select ColorId
		Case 1
			Return "Black"
		Case 2
			Return "Red"
		Default
			Return "NotVanilla"

		End Select

	Default
		If AccessoryId<1
			Return "None"
		Else
			If ColorId=1 And IsAccessoryIdVanilla(AccessoryId)
				Return "Default"
			Else
				Return "NotVanilla"
			EndIf
		EndIf
	End Select

End Function

Function IsAccessoryIdVanilla(AccessoryId)

	Return (AccessoryId>-1 And AccessoryId<57) Or (AccessoryId>100 And AccessoryId<117)

End Function

Function GetAccessoryColorNameWithColorInt$(AccessoryId,ColorId)

	ColorName$=GetAccessoryColorName$(AccessoryId,ColorId)
	Return ColorId+"/"+ColorName$

End Function

Const AccessoryDirectory$="data/models/stinker/"

Function GetAccFilenameStart$(AccessoryId)

	; This is done because only the last three digits of the accessory ID are read in-game due to funky string handling.
	AccessoryId=AccessoryId Mod 1000

	If AccessoryId>99
		Prefix$="accessory"
	ElseIf AccessoryId>9 ; two digit
		Prefix$="accessory0"
	Else
		Prefix$="accessory00"
	EndIf

	Return Prefix$+Str$(AccessoryId)

End Function

Function GetAccFilenameModel$(AccessoryId)

	Return GetAccFilenameStart$(AccessoryId)+".3ds"

End Function

Function GetAccFilenameTexture$(AccessoryId,ColorId)

	Return GetAccFilenameStart$(AccessoryId)+Chr$(64+ColorId)+".jpg"

End Function

Function CreateAccEntity(AccessoryId)

	FilePath$=AccessoryDirectory$+GetAccFilenameModel$(AccessoryId)
	If FileExistsModel(FilePath$)
		Return MyLoadMesh(FilePath$,0)
	Else
		;ShowMessage("YOU FAIL!!! "+FileName$+" IS NOT EVEN REAL!!!", 1000)
		Return CreateAccEntityNotExist()
	EndIf

End Function

Function CreateAccEntityNotExist()

	Entity=CreateSphere()
	ScaleMesh Entity,10,10,10
	Return Entity

End Function

Function CreateAccTexture(AccessoryId,ColorId)

	FilePath$=AccessoryDirectory$+GetAccFilenameTexture$(AccessoryId,ColorId)
	If FileExistsTexture(FilePath$)
		Return MyLoadTexture(FilePath$,4)
	Else
		;ShowMessage("YOU FAIL!!! "+FileName$+" IS NOT EVEN REAL!!!", 1000)
		Return 0
	EndIf

End Function

Function CreateHatTexture(Data2,Data3)

	Return CreateAccTexture(Data2,Data3)

End Function

Function CreateGlassesTexture(Data4,Data5)

	; The color ID on glasses is ahead of hats by 1.
	Return CreateAccTexture(Data4,Data5+1)

End Function

Function TransformAccessoryEntityGeneric(Entity,XScale#,YScale#,ZScale#,Yaw#,Pitch#,Roll#,X#,Y#,Z#)

	ScaleEntity Entity,XScale#,ZScale#,YScale#

	GameLikeRotation(Entity,Yaw#-90.0,Pitch#,Roll#)

	PositionEntity Entity,X#,Z#+.1+.84*ZScale#/.035,-Y#

End Function

Function TransformAccessoryEntityOntoBone(Entity,BoneHaver)

	bone=FindChild(BoneHaver,"hat_bone")

	PositionEntity Entity,EntityX(bone,True),EntityY(bone,True),EntityZ(bone,True)

	RotateEntity Entity,EntityPitch(bone,True),EntityYaw(bone,True),EntityRoll(bone,True)

	;GameLikeRotation(Entity,EntityYaw(bone,True),EntityRoll(bone,True),-EntityPitch(bone,True))

End Function

Function BuildLevelObjectModel(Dest)

	Obj.GameObject=LevelObjects(Dest)

	BuildObjectModel(Obj,Obj\Position\X,Obj\Position\Y,Obj\Position\Z)

	UpdateObjectAnimation(Obj)

	UpdateObjectVisibility(Obj)

	;PositionEntity Obj\Model\Entity,ObjectSumX#(Obj),ObjectSumZ#(Obj),-ObjectSumY#(Obj)

	;If Obj\Model\HatEntity>0
	;	TransformAccessoryEntityOntoBone(Obj\Model\HatEntity,Obj\Model\Entity)
	;EndIf
	;If Obj\Model\AccEntity>0
	;	TransformAccessoryEntityOntoBone(Obj\Model\AccEntity,Obj\Model\Entity)
	;EndIf

End Function

Function UpdateLevelObjectModel(Dest)

	;ShowMessage("Freeing object model "+Dest+": "+ObjectModelName$(Dest),10)

	FreeObjectModel(LevelObjects(Dest)\Model)

	;ShowMessage("Creating object model "+Dest+": "+ObjectModelName$(Dest),10)

	BuildLevelObjectModel(Dest)

End Function

Function CountObjectTypes(TargetType)

	Count=0
	For i=0 To NofObjects-1
		If LevelObjects(i)\Attributes\LogicType=TargetType
			Count=Count+1
		EndIf
	Next
	Return Count

End Function

Function CountObjectEffectiveIDs(EffectiveID)

	Count=0
	For i=0 To NofObjects-1
		If CalculateEffectiveID(LevelObjects(i)\Attributes)=EffectiveID
			Count=Count+1
		EndIf
	Next
	Return Count

End Function

Function CountObjectLogics(TargetType,TargetSubType)

	Count=0
	For i=0 To NofObjects-1
		If LevelObjects(i)\Attributes\LogicType=TargetType And LevelObjects(i)\Attributes\LogicSubType=TargetSubType
			Count=Count+1
		EndIf
	Next
	Return Count

End Function

Function CountObjectModelNames(TargetModelName$)

	Count=0
	For i=0 To NofObjects-1
		If LevelObjects(i)\Attributes\ModelName$=TargetModelName$
			Count=Count+1
		EndIf
	Next
	Return Count

End Function

Function CountObjectTextureNames(TargetTextureName$)

	Count=0
	For i=0 To NofObjects-1
		If LevelObjects(i)\Attributes\TexName$=TargetTextureName$
			Count=Count+1
		EndIf
	Next
	Return Count

End Function

Function IsObjectSubTypeFourColorButton(TargetSubType)

	Return (TargetSubType Mod 32)<5 ; square, round, diamond, diamondonce, star

End Function

Function IsObjectLogicFourColorButton(TargetType,TargetSubType)

	If TargetType=90
		Return IsObjectSubTypeFourColorButton(TargetSubType)
	Else
		Return False
	EndIf

End Function

Function IsObjectLogicAutodoor(TargetType,TargetSubType)

	Return TargetType=10 And TargetSubType=9

End Function

Function IsObjectTypeKeyblock(TargetType)

	Select TargetType
	Case 80,81,82,83,84,85,86,87
		Return True
	Default
		Return False
	End Select

End Function

Function GetAutodoorActivateName$(DataValue)

	If DataValue>=0
		Return "ActivateID"
	Else
		Return "ActivateType"
	EndIf

End Function

Function GetAutodoorActivateValueName$(DataValue)

	If DataValue=0
		Return "Creatures" ;"The Living"
	ElseIf DataValue<0
		Return Str$(-DataValue)+"/"+GetTypeString$(-DataValue)
	Else
		Return DataValue
	EndIf

End Function

; i hate blitz because it won't let me have arrays in custom types
Function SetDataByIndex(Attributes.GameObjectAttributes,i,Value)
	Select i
	Case 0
		Attributes\Data0=Value
	Case 1
		Attributes\Data1=Value
	Case 2
		Attributes\Data2=Value
	Case 3
		Attributes\Data3=Value
	Case 4
		Attributes\Data4=Value
	Case 5
		Attributes\Data5=Value
	Case 6
		Attributes\Data6=Value
	Case 7
		Attributes\Data7=Value
	Case 8
		Attributes\Data8=Value
	Case 9
		Attributes\Data9=Value
	End Select
End Function

; screw blitzard
Function GetDataByIndex(Attributes.GameObjectAttributes,i)

	Select i
	Case 0
		Return Attributes\Data0
	Case 1
		Return Attributes\Data1
	Case 2
		Return Attributes\Data2
	Case 3
		Return Attributes\Data3
	Case 4
		Return Attributes\Data4
	Case 5
		Return Attributes\Data5
	Case 6
		Return Attributes\Data6
	Case 7
		Return Attributes\Data7
	Case 8
		Return Attributes\Data8
	Case 9
		Return Attributes\Data9
	End Select

End Function

Function SetThreeOtherDataIfNotEqual(DTo1,DTo2,DTo3,DFrom,OldData)

	Attributes.GameObjectAttributes=CurrentObject\Attributes
	If GetDataByIndex(Attributes,DTo1)=OldData And GetDataByIndex(Attributes,DTo2)=OldData And GetDataByIndex(Attributes,DTo3)=OldData
		NewValue=GetDataByIndex(Attributes,DFrom)
		SetDataByIndex(Attributes,DTo1,NewValue)
		SetDataByIndex(Attributes,DTo2,NewValue)
		SetDataByIndex(Attributes,DTo3,NewValue)
	EndIf

End Function

Function CameraControls()
	MouseDeltaX = MouseXSpeed()
	MouseDeltaY = MouseYSpeed()
	mx = MouseX()
	my = MouseY()

	Adj#=0.1
	IntAdj=1
	If ShiftDown()
		Adj#=0.4
		IntAdj=4
	EndIf

	; Still doesn't really work, and also doesn't behave well with orthographic mode yet.
	;If KeyPressed(idk) ; Formerly 20, which is T
	;	ToggleGameCamera()
	;EndIf

	If KeyDown(57) ; space bar
		CameraPanning=True
		If LeftMouse=True
			SpeedFactor#=0.25*Adj
			AdjX#=-MouseDeltaX * SpeedFactor
			AdjY#=MouseDeltaY * SpeedFactor

			Cs#=Cos(EntityYaw#(Camera1))
			Sn#=Sin(EntityYaw#(Camera1))
			VecX#=AdjX*Cs-AdjY*Sn
			VecY#=AdjX*Sn+AdjY*Cs

			TranslateEntity Camera1,VecX#,0,VecY#
		EndIf
	Else
		CameraPanning=False
	EndIf

	Target=-1

	If EditorMode=3 And mx>=SidebarX+195 And my>=SidebarY+305 And mx<=SidebarX+295 And my<=SidebarY+430 ; camera4 viewport space
		Target=Camera4 ; object camera
	;ElseIf EditorMode=0 And mx>=510 And mx<710 And my>=20 And my<240
	;	Target=-1 ; tile camera
	Else
		Target=Camera1 ; level camera
	EndIf

	If Target=-1
		Return
	EndIf

	If Not CtrlDown()

		If KeyDown(75) Or KeyDown(203) Or KeyDown(KeyMoveWest) ; numpad 4 or left arrow
			;TranslateEntity Target,-Adj,0,0
			TranslateEntity Target,-Adj*Cos(EntityYaw#(Target)),0,-Adj*Sin(EntityYaw#(Target))
		EndIf
		If KeyDown(77) Or KeyDown(205) Or KeyDown(KeyMoveEast) ; numpad 6 or right arrow
			;TranslateEntity Target,Adj,0,0
			TranslateEntity Target,Adj*Cos(EntityYaw#(Target)),0,Adj*Sin(EntityYaw#(Target))
		EndIf
		If KeyDown(72) Or KeyDown(200) Or KeyDown(KeyMoveNorth) ; numpad 8 or up arrow
			;TranslateEntity Target,0,0,Adj
			TranslateEntity Target,Adj*Cos(EntityYaw#(Target)+90),0,Adj*Sin(EntityYaw#(Target)+90)
		EndIf
		If KeyDown(80) Or KeyDown(208) Or KeyDown(KeyMoveSouth) ; numpad 2 or down arrow
			;TranslateEntity Target,0,0,-Adj
			TranslateEntity Target,-Adj*Cos(EntityYaw#(Target)+90),0,-Adj*Sin(EntityYaw#(Target)+90)
		EndIf
		If KeyDown(73) Or KeyDown(18) ; numpad 9 or E

			TranslateEntity Target,0,Adj,0
		EndIf
		If KeyDown(81) Or KeyDown(46) ; numpad 3 or C

			TranslateEntity Target,0,-Adj,0
		EndIf
		If KeyDown(71) Or KeyDown(16) ; numpad 7 or Q

			TurnEntity Target,IntAdj,0,0
		EndIf
		If KeyDown(79) Or KeyDown(44) ; numpad 1 or Z

			TurnEntity Target,-IntAdj,0,0
		EndIf
		If KeyDown(181) Or KeyDown(23) ;Or KeyDown(3) ; numpad / or I
			RotateEntity Target,EntityPitch#(Target),EntityYaw#(Target)+IntAdj,EntityRoll#(Target)
		EndIf
		If KeyDown(55) Or KeyDown(24) ;Or KeyDown(4) ; numpad * or O
			RotateEntity Target,EntityPitch#(Target),EntityYaw#(Target)-IntAdj,EntityRoll#(Target)
		EndIf

		If KeyDown(76) Or KeyDown(45) ; numpad 5 or X
			; reset camera position and rotation
			If Target=Camera1
				RotateEntity Camera1,65,0,0
				If ShiftDown()
					CenterCameraInLevel()
					If Camera1Proj=2 ; orthographic
						TheY#=Camera1StartY*2
					Else ; probably perspective
						TheY#=Camera1StartY
					EndIf
					PositionEntity Camera1,EntityX(Camera1),TheY#,EntityZ(Camera1)
				EndIf
			ElseIf Target=Camera4
				RotateEntity Camera4,25,0,0
				PositionEntity Camera4,0,303.8,-8
				Camera4Zoom#=8
				CameraZoom Camera4,Camera4Zoom#
			EndIf
		EndIf

	EndIf

	If MouseScroll<>0
		; mouse position check here because we don't want to move the camera when using scroll wheel on object adjusters
		If Target=Camera1 And mx<LevelViewportWidth+10 And my>=0 And my<LevelViewportHeight
			If ShiftDown()
				SetBrushWidth(BrushWidth+MouseScroll)
			EndIf
			If CtrlDown()
				SetBrushHeight(BrushHeight+MouseScroll)
			EndIf
			If (Not ShiftDown()) And (Not CtrlDown())
				; level camera
				If Camera1Proj=1 ; perspective
					SpeedFactor#=3.0*Adj
					TranslateEntity Camera1,0,-MouseScroll * SpeedFactor,0
				ElseIf Camera1Proj=2 ; orthographic
					ZoomSpeed#=12.0*Adj
					If MouseScroll>0
						Camera1Zoom#=Camera1Zoom#*ZoomSpeed
					ElseIf MouseScroll<0
						Camera1Zoom#=Camera1Zoom#/ZoomSpeed
					EndIf
					If Camera1Zoom#<0.001
						Camera1Zoom#=0.001
					EndIf
					CameraZoom Camera1,Camera1Zoom#
				EndIf
			EndIf
		ElseIf Target=Camera4
			; object camera
			ZoomSpeed#=12.0*Adj
			If MouseScroll>0
				Camera4Zoom#=Camera4Zoom#*ZoomSpeed
			ElseIf MouseScroll<0
				Camera4Zoom#=Camera4Zoom#/ZoomSpeed
			EndIf
			If Camera4Zoom#<0.1
				Camera4Zoom#=0.1
			EndIf
			CameraZoom Camera4,Camera4Zoom#
		EndIf
	EndIf

End Function

Function ToggleGameCamera()

	GameCamera=Not GameCamera

	If GameCamera
		RotateEntity Camera1,55,0,0
		PositionEntity Camera1,EntityX(Camera1),12,EntityZ(Camera1) ; y=12 in-game
		;If Not widescreen
		If Not displayfullscreen
			CameraZoom Camera1,2
		Else
			CameraZoom Camera1,1.5
		EndIf
	Else
		RotateEntity Camera1,65,0,0
		PositionEntity Camera1,EntityX(Camera1),6,EntityZ(Camera1) ; 7,6,-14
		CameraZoom Camera1,1
	EndIf

End Function

Function SaveLevel()

	file=WriteFile (GetAdventureDir$()+currentlevelnumber+".wlv")

	If (currentlevelnumber>94 And currentlevelnumber<99) Or Left$(Upper$(adventurefilename$),5)="ZACHY"
		; WA3 VAULTS
		WriteInt file,LevelWidth+121
	Else
		WriteInt file,LevelWidth
	EndIf
	WriteInt file,LevelHeight
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			WriteInt file,LevelTiles(i,j)\Terrain\Texture ; corresponding to squares in LevelTexture
			WriteInt file,LevelTiles(i,j)\Terrain\Rotation ; 0-3 , and 4-7 for "flipped"
			WriteInt file,LevelTiles(i,j)\Terrain\SideTexture ; texture for extrusion walls
			WriteInt file,LevelTiles(i,j)\Terrain\SideRotation ; 0-3 , and 4-7 for "flipped"
			WriteFloat file,LevelTiles(i,j)\Terrain\Random ; random height pertubation of tile
			WriteFloat file,LevelTiles(i,j)\Terrain\Height ; height of "center" - e.g. to make ditches and hills
			WriteFloat file,LevelTiles(i,j)\Terrain\Extrusion; extrusion with walls around it
			WriteInt file,LevelTiles(i,j)\Terrain\Rounding; 0-no, 1-yes: are floors rounded if on a drop-off corner
			WriteInt file,LevelTiles(i,j)\Terrain\EdgeRandom; 0-no, 1-yes: are edges rippled

			WriteInt file,LevelTiles(i,j)\Terrain\Logic

		Next
	Next

	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			WriteInt file,LevelTiles(i,j)\Water\Texture
			WriteInt file,LevelTiles(i,j)\Water\Rotation
			WriteFloat file,LevelTiles(i,j)\Water\Height
			WriteFloat file,LevelTiles(i,j)\Water\Turbulence
		Next
	Next

	; Globals
	WriteInt file,WaterFlow
	WriteInt file,WaterTransparent
	WriteInt file,WaterGlow

	WriteString file,CurrentLevelTextureName$()
	WriteString file,CurrentWaterTextureName$()

	; Objects

	PlayerIndex=ObjectIndexEditorToGameInner(NofObjects)

	WriteInt file,NofObjects
	For i=0 To NofObjects-1
		Obj.GameObject=LevelObjects(i)

		WriteString file,Obj\Attributes\ModelName$
		WriteString file,Obj\Attributes\TexName$
		WriteFloat file,Obj\Attributes\XScale
		WriteFloat file,Obj\Attributes\YScale
		WriteFloat file,Obj\Attributes\ZScale
		WriteFloat file,Obj\Attributes\XAdjust
		WriteFloat file,Obj\Attributes\YAdjust
		WriteFloat file,Obj\Attributes\ZAdjust
		WriteFloat file,Obj\Attributes\PitchAdjust
		WriteFloat file,Obj\Attributes\YawAdjust
		WriteFloat file,Obj\Attributes\RollAdjust

		WriteFloat file,Obj\Position\X
		WriteFloat file,Obj\Position\Y
		WriteFloat file,Obj\Position\Z
		WriteFloat file,Obj\Position\OldX
		WriteFloat file,Obj\Position\OldY
		WriteFloat file,Obj\Position\OldZ

		WriteFloat file,Obj\Attributes\DX
		WriteFloat file,Obj\Attributes\DY
		WriteFloat file,Obj\Attributes\DZ

		WriteFloat file,Obj\Attributes\Pitch
		WriteFloat file,Obj\Attributes\Yaw
		WriteFloat file,Obj\Attributes\Roll
		WriteFloat file,Obj\Attributes\Pitch2
		WriteFloat file,Obj\Attributes\Yaw2
		WriteFloat file,Obj\Attributes\Roll2

		WriteFloat file,Obj\Attributes\XGoal
		WriteFloat file,Obj\Attributes\YGoal
		WriteFloat file,Obj\Attributes\ZGoal

		WriteInt file,Obj\Attributes\MovementType
		WriteInt file,Obj\Attributes\MovementTypeData
		WriteFloat file,Obj\Attributes\Speed
		WriteFloat file,Obj\Attributes\Radius
		WriteInt file,Obj\Attributes\RadiusType

		WriteInt file,Obj\Attributes\Data10

		WriteFloat file,Obj\Attributes\PushDX
		WriteFloat file,Obj\Attributes\PushDY

		WriteInt file,Obj\Attributes\AttackPower
		WriteInt file,Obj\Attributes\DefensePower
		WriteInt file,Obj\Attributes\DestructionType

		WriteInt file,Obj\Attributes\ID
		WriteInt file,Obj\Attributes\LogicType
		WriteInt file,Obj\Attributes\LogicSubType

		WriteInt file,Obj\Attributes\Active
		WriteInt file,Obj\Attributes\LastActive
		WriteInt file,Obj\Attributes\ActivationType
		WriteInt file,Obj\Attributes\ActivationSpeed

		WriteInt file,Obj\Attributes\Status
		WriteInt file,Obj\Attributes\Timer
		WriteInt file,Obj\Attributes\TimerMax1
		WriteInt file,Obj\Attributes\TimerMax2

		WriteInt file,Obj\Attributes\Teleportable
		WriteInt file,Obj\Attributes\ButtonPush
		WriteInt file,Obj\Attributes\WaterReact

		WriteInt file,Obj\Attributes\Telekinesisable
		WriteInt file,Obj\Attributes\Freezable

		WriteInt file,Obj\Attributes\Reactive

		;WriteInt file,ObjectChild
		WriteInt file,ObjectIndexEditorToGame(Obj\Attributes\Child, PlayerIndex)
		;WriteInt file,ObjectParent
		WriteInt file,ObjectIndexEditorToGame(Obj\Attributes\Parent, PlayerIndex)

		WriteInt file,Obj\Attributes\Data0
		WriteInt file,Obj\Attributes\Data1
		WriteInt file,Obj\Attributes\Data2
		WriteInt file,Obj\Attributes\Data3
		WriteInt file,Obj\Attributes\Data4
		WriteInt file,Obj\Attributes\Data5
		WriteInt file,Obj\Attributes\Data6
		WriteInt file,Obj\Attributes\Data7
		WriteInt file,Obj\Attributes\Data8
		WriteInt file,Obj\Attributes\Data9
		WriteString file,Obj\Attributes\TextData0
		WriteString file,Obj\Attributes\TextData1
		WriteString file,Obj\Attributes\TextData2
		WriteString file,Obj\Attributes\TextData3

		WriteInt file,Obj\Attributes\Talkable
		WriteInt file,Obj\Attributes\CurrentAnim
		WriteInt file,Obj\Attributes\StandardAnim
		WriteInt file,Obj\Position\TileX
		WriteInt file,Obj\Position\TileY
		WriteInt file,Obj\Position\TileX2
		WriteInt file,Obj\Position\TileY2
		WriteInt file,Obj\Attributes\MovementTimer
		WriteInt file,Obj\Attributes\MovementSpeed
		WriteInt file,Obj\Attributes\MoveXGoal
		WriteInt file,Obj\Attributes\MoveYGoal
		WriteInt file,Obj\Attributes\TileTypeCollision
		WriteInt file,Obj\Attributes\ObjectTypeCollision
		WriteInt file,Obj\Attributes\Caged
		WriteInt file,Obj\Attributes\Dead
		WriteInt file,Obj\Attributes\DeadTimer
		WriteInt file,Obj\Attributes\Exclamation
		WriteInt file,Obj\Attributes\Shadow
		;WriteInt file,-1;ObjectLinked
		WriteInt file,ObjectIndexEditorToGame(Obj\Attributes\Linked, PlayerIndex)
		;WriteInt file,-1;ObjectLinkBack
		WriteInt file,ObjectIndexEditorToGame(Obj\Attributes\LinkBack, PlayerIndex)
		WriteInt file,Obj\Attributes\Flying
		WriteInt file,Obj\Attributes\Frozen
		WriteInt file,Obj\Attributes\Indigo
		WriteInt file,Obj\Attributes\FutureInt24
		WriteInt file,Obj\Attributes\FutureInt25
		WriteFloat file,Obj\Attributes\ScaleAdjust
		WriteFloat file,Obj\Attributes\ScaleXAdjust
		WriteFloat file,Obj\Attributes\ScaleYAdjust
		WriteFloat file,Obj\Attributes\ScaleZAdjust
		WriteFloat file,Obj\Attributes\FutureFloat5
		WriteFloat file,Obj\Attributes\FutureFloat6
		WriteFloat file,Obj\Attributes\FutureFloat7
		WriteFloat file,Obj\Attributes\FutureFloat8
		WriteFloat file,Obj\Attributes\FutureFloat9
		WriteFloat file,Obj\Attributes\FutureFloat10
		WriteString file,Obj\Attributes\FutureString1$
		WriteString file,Obj\Attributes\FutureString2$

		For k=0 To 30
			;WriteString file,ObjectAdjusterString$(Dest,k)
			WriteString file,""
		Next

	Next

	WriteInt file,LevelEdgeStyle

	WriteInt file,LightRed
	WriteInt file,LightGreen
	WriteInt file,LightBlue

	WriteInt file,AmbientRed
	WriteInt file,AmbientGreen
	WriteInt file,AmbientBlue

	WriteInt file,LevelMusic
	WriteInt file,LevelWeather

	WriteString file,adventuretitle$

	; NEVER save extra data at the end of the wlv file.
	; Saving extra bytes at the end of the file confuses the vanilla player into changing the state of every tile in the level.

	;WriteInt file,-2
	;WriteInt file,WidescreenRangeLevel

	CloseFile file

	UnsavedChanges=0

End Function

Function CenterCameraInLevel()

	PositionCameraInLevel(LevelWidth/2,LevelHeight/2)

End Function

Function AccessLevelAt(levelnumber,FocusOnTileX,FocusOnTileY)

	AccessLevel(levelnumber)
	PositionCameraInLevel(FocusOnTileX,FocusOnTileY)

End Function

Function AccessLevelAtCenter(levelnumber)

	AccessLevel(levelnumber)
	CenterCameraInLevel()

End Function

Function SetCurrentLevelNumber(levelnumber)

	If OpenedFirstLevelYet And CurrentLevelNumber<>levelnumber
		; Add the current level number to the ring buffer that's tracking previous level numbers.
		PreviousLevelNumberBuffer(PreviousLevelNumberBufferCurrent)=CurrentLevelNumber
		If PreviousLevelNumberBufferCurrent=PreviousLevelNumberBufferMax
			PreviousLevelNumberBufferCurrent=0
		Else
			PreviousLevelNumberBufferCurrent=PreviousLevelNumberBufferCurrent+1
		EndIf
		If PreviousLevelNumberBufferCurrent=PreviousLevelNumberBufferStart
			If PreviousLevelNumberBufferStart=PreviousLevelNumberBufferMax
				PreviousLevelNumberBufferStart=0
			Else
				PreviousLevelNumberBufferStart=PreviousLevelNumberBufferStart+1
			EndIf
		EndIf
	Else
		OpenedFirstLevelYet=True
	EndIf

	CurrentLevelNumber=levelnumber

End Function

Function TryPopPreviousLevel()

	If PreviousLevelNumberBufferStart<>PreviousLevelNumberBufferCurrent
		If AskToSaveLevelAndExit()
			If PreviousLevelNumberBufferCurrent=0
				PreviousLevelNumberBufferCurrent=PreviousLevelNumberBufferMax
			Else
				PreviousLevelNumberBufferCurrent=PreviousLevelNumberBufferCurrent-1
			EndIf
			CurrentLevelNumber=PreviousLevelNumberBuffer(PreviousLevelNumberBufferCurrent)
			AccessLevelAtCenter(CurrentLevelNumber)
		EndIf
	EndIf

End Function

Function LoadObjectAttributes(file,i)

	LevelObject.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=LevelObject\Attributes
	Position.GameObjectPosition=LevelObject\Position

	Attributes\ModelName$=ReadString$(file)
	Attributes\TexName$=ReadString$(file)
	Attributes\XScale=ReadFloat(file)
	Attributes\YScale=ReadFloat(file)
	Attributes\ZScale=ReadFloat(file)
	Attributes\XAdjust=ReadFloat(file)
	Attributes\YAdjust=ReadFloat(file)
	Attributes\ZAdjust=ReadFloat(file)
	Attributes\PitchAdjust=ReadFloat(file)
	Attributes\YawAdjust=ReadFloat(file)
	Attributes\RollAdjust=ReadFloat(file)

	Position\X=ReadFloat(file)
	Position\Y=ReadFloat(file)
	Position\Z=ReadFloat(file)
	Position\OldX=ReadFloat(file)
	Position\OldY=ReadFloat(file)
	Position\OldZ=ReadFloat(file)

	Attributes\DX=ReadFloat(file)
	Attributes\DY=ReadFloat(file)
	Attributes\DZ=ReadFloat(file)

	Attributes\Pitch=ReadFloat(file)
	Attributes\Yaw=ReadFloat(file)
	Attributes\Roll=ReadFloat(file)
	Attributes\Pitch2=ReadFloat(file)
	Attributes\Yaw2=ReadFloat(file)
	Attributes\Roll2=ReadFloat(file)

	Attributes\XGoal=ReadFloat(file)
	Attributes\YGoal=ReadFloat(file)
	Attributes\ZGoal=ReadFloat(file)

	Attributes\MovementType=ReadInt(file)
	Attributes\MovementTypeData=ReadInt(file)
	Attributes\Speed=ReadFloat(file)
	Attributes\Radius=ReadFloat(file)
	Attributes\RadiusType=ReadInt(file)

	Attributes\Data10=ReadInt(file)

	Attributes\PushDX=ReadFloat(file)
	Attributes\PushDY=ReadFloat(file)

	Attributes\AttackPower=ReadInt(file)
	Attributes\DefensePower=ReadInt(file)
	Attributes\DestructionType=ReadInt(file)

	Attributes\ID=ReadInt(file)

	Attributes\LogicType=ReadInt(file)
	Attributes\LogicSubType=ReadInt(file)

	Attributes\Active=ReadInt(file)
	Attributes\LastActive=ReadInt(file)
	Attributes\ActivationType=ReadInt(file)
	Attributes\ActivationSpeed=ReadInt(file)

	Attributes\Status=ReadInt(file)
	Attributes\Timer=ReadInt(file)
	Attributes\TimerMax1=ReadInt(file)
	Attributes\TimerMax2=ReadInt(file)

	Attributes\Teleportable=ReadInt(file)
	Attributes\ButtonPush=ReadInt(file)
	Attributes\WaterReact=ReadInt(file)

	Attributes\Telekinesisable=ReadInt(file)
	Attributes\Freezable=ReadInt(file)

	Attributes\Reactive=ReadInt(file)

	Attributes\Child=ReadInt(file)
	Attributes\Parent=ReadInt(file)

	Attributes\Data0=ReadInt(file)
	Attributes\Data1=ReadInt(file)
	Attributes\Data2=ReadInt(file)
	Attributes\Data3=ReadInt(file)
	Attributes\Data4=ReadInt(file)
	Attributes\Data5=ReadInt(file)
	Attributes\Data6=ReadInt(file)
	Attributes\Data7=ReadInt(file)
	Attributes\Data8=ReadInt(file)
	Attributes\Data9=ReadInt(file)

	Attributes\TextData0=ReadString$(file)
	Attributes\TextData1=ReadString$(file)
	Attributes\TextData2=ReadString$(file)
	Attributes\TextData3=ReadString$(file)

	Attributes\Talkable=ReadInt(file)
	Attributes\CurrentAnim=ReadInt(file)
	Attributes\StandardAnim=ReadInt(file)
	;ObjectTileX=ReadInt(file)
	;ObjectTileY=ReadInt(file)
	tilex=ReadInt(file)
	tiley=ReadInt(file)
	SetObjectTileXY(i,tilex,tiley)
	ReadInt(file) ;ObjectTileX2=ReadInt(file)
	ReadInt(file) ;ObjectTileY2=ReadInt(file)
	Attributes\MovementTimer=ReadInt(file)
	Attributes\MovementSpeed=ReadInt(file)
	Attributes\MoveXGoal=ReadInt(file)
	Attributes\MoveYGoal=ReadInt(file)
	Attributes\TileTypeCollision=ReadInt(file)
	Attributes\ObjectTypeCollision=ReadInt(file)
	Attributes\Caged=ReadInt(file)
	Attributes\Dead=ReadInt(file)
	Attributes\DeadTimer=ReadInt(file)
	Attributes\Exclamation=ReadInt(file)
	Attributes\Shadow=ReadInt(file)
	Attributes\Linked=ReadInt(file)
	Attributes\LinkBack=ReadInt(file)
	Attributes\Flying=ReadInt(file)
	Attributes\Frozen=ReadInt(file)
	Attributes\Indigo=ReadInt(file)
	Attributes\FutureInt24=ReadInt(file)
	Attributes\FutureInt25=ReadInt(file)
	Attributes\ScaleAdjust=ReadFloat(file)
	Attributes\ScaleXAdjust=ReadFloat(file)
	Attributes\ScaleYAdjust=ReadFloat(file)
	Attributes\ScaleZAdjust=ReadFloat(file)
	Attributes\ScaleXAdjust=1.0
	Attributes\ScaleYAdjust=1.0
	Attributes\ScaleZAdjust=1.0
	Attributes\FutureFloat5=ReadFloat(file)
	Attributes\FutureFloat6=ReadFloat(file)
	Attributes\FutureFloat7=ReadFloat(file)
	Attributes\FutureFloat8=ReadFloat(file)
	Attributes\FutureFloat9=ReadFloat(file)
	Attributes\FutureFloat10=ReadFloat(file)
	Attributes\FutureString1$=ReadString(file)
	Attributes\FutureString2$=ReadString(file)

End Function

Function NewLevel(levelnumber)

	; new level
	SetCurrentLevelNumber(levelnumber)
	resetlevel()
	; clear current objects

	For ig=0 To NofObjects-1
		FreeObject(ig)
	Next

	SetNofObjects(0)
	ObjectsWereChanged()

	; reset textures
	FreeTexture leveltexture
	FreeTexture watertexture
	CurrentLevelTexture=0
	LevelTexture=myLoadTexture("data\Leveltextures\"+LevelTexturename$(CurrentLevelTexture),1)
	CurrentWaterTexture=0
	waterTexture=myLoadTexture("data\Leveltextures\"+waterTexturename$(CurrentWaterTexture),1)

	UpdateLevelTexture()
	UpdateWaterTexture()

	rebuildlevelmodel()
	BuildCurrentTileModel()

	OpenedLevel()

End Function

Function CompileLevel()

End Function

Function OpenedLevel()

	UnsavedChanges=0

	; Don't restore master here, because AccessLevel is used by StartTestModeAt.
	RestoreOriginal1Wlv()

	WhereWeEndedUpAlpha#=0.0

	If BrushMode=BrushModeTestLevel
		BrushMode=BrushModeNormal
	EndIf

	BrushCursorProbablyModifiedTiles()

	UpdateMusic()

End Function

Function AccessLevel(levelnumber)

	If LevelExists(levelnumber)=True
		LoadLevel(levelnumber)
	Else
		NewLevel(levelnumber)
	EndIf

End Function

Function OpenTypedLevel()

	Typed$=InputString$("Enter wlv number to open: ")
	If Typed$<>""
		AccessLevelAtCenter(Typed$)
		Return True
	Else
		Return False
	EndIf

End Function

Function OpenTypedDialog()

	Typed$=InputString$("Enter dia number to open: ")
	If Typed$<>""
		AccessDialog(Typed$)
		Return True
	Else
		Return False
	EndIf

End Function

Function CanChangeAdventureCurrentArchive()

	Return Not (EditorMode=5 And HubMode=True)

End Function

Function GetAdventuresDir$(CurrentArchive)

	Select CurrentArchive ; The corresponding global variable is AdventureCurrentArchive.
	Case AdventureCurrentArchiveArchive
		Return globaldirname$+"\Custom\Editing\Archive\"
	Case AdventureCurrentArchivePlayer
		Return globaldirname$+"\Custom\Adventures\"
	Case AdventureCurrentArchiveDataAdventures
		Return "Data\Adventures\"
	Default
		Return globaldirname$+"\Custom\Editing\Current\"
	End Select

End Function

Function GetAdventureFolder$()

	ex2$=GetAdventuresDir$(AdventureCurrentArchive)
	Return ex2$+AdventureFileName$

End Function

Function GetAdventureDir$()

	Return GetAdventureFolder$()+"\"

End Function

Function MoveFile(numbersource,numberdest,ext$)

	dirbase$=GetAdventureDir$()
	CopyFile(dirbase$+numbersource+ext$,dirbase$+numberdest+ext$)
	DeleteFile(dirbase$+numbersource+ext$)

End Function

Function DuplicateMaster(SourceName$,DestinationName$)

	dirbase$=GetAdventureDir$()
	CopyFile(dirbase$+SourceName$+".dat",dirbase$+DestinationName$+".dat")

End Function

Function DeleteMaster(TargetName$)

	dirbase$=GetAdventureDir$()
	DeleteFile(dirbase$+TargetName$+".dat")

End Function

Function RestoreOriginalMaster()

	dirbase$=GetAdventureDir$()
	If FileExists(dirbase$+OriginalMasterDat$+".dat")
		DeleteMaster("master")
		DuplicateMaster(OriginalMasterDat$,"master")
		DeleteMaster(OriginalMasterDat$)
	EndIf

End Function

Function RestoreOriginal1Wlv()

	dirbase$=GetAdventureDir$()
	If FileExists(dirbase$+Original1Wlv$+".wlv")
		DeleteLevel(1)
		DuplicateLevel(Original1Wlv$,1)
		DeleteLevel(Original1Wlv$)
	EndIf

End Function

Function DeleteLevel(TargetLevelName$)

	dirbase$=GetAdventureDir$()
	DeleteFile(dirbase$+TargetLevelName$+".wlv")

End Function

Function DuplicateLevel(levelnumbersource$,levelnumberdest$)

	dirbase$=GetAdventureDir$()
	CopyFile(dirbase$+levelnumbersource+".wlv",dirbase$+levelnumberdest+".wlv")

End Function

Function MoveLevel(levelnumbersource,levelnumberdest)

	MoveFile(levelnumbersource,levelnumberdest,".wlv")
	MasterLevelList(levelnumbersource)=0
	MasterLevelList(levelnumberdest)=1

End Function

Function MoveDialog(levelnumbersource,levelnumberdest)

	MoveFile(levelnumbersource,levelnumberdest,".wlv")
	MasterLevelList(levelnumbersource)=0
	MasterLevelList(levelnumberdest)=1

End Function

Function SwapFile(levelnumber1,levelnumber2,ext$,Exists1,Exists2)

	If Exists1 And Exists2
		dirbase$=GetAdventureDir$()
		CopyFile(dirbase$+levelnumber1+ext$,dirbase$+"temp"+levelnumber1+ext$)
		DeleteFile(dirbase$+levelnumber1+ext$)
		CopyFile(dirbase$+levelnumber2+ext$,dirbase$+levelnumber1+ext$)
		DeleteFile(dirbase$+levelnumber2+ext$)
		CopyFile(dirbase$+"temp"+levelnumber1+ext$,dirbase$+levelnumber2+ext$)
		DeleteFile(dirbase$+"temp"+levelnumber1+ext$)
	ElseIf Exists1=True And Exists2=False
		MoveFile(levelnumber1,levelnumber2,ext$)
	ElseIf Exists1=False And Exists2=True
		MoveFile(levelnumber2,levelnumber1,ext$)
	EndIf

End Function

Function SwapLevel(levelnumber1,levelnumber2)

	Exists1=LevelExists(levelnumber1)
	Exists2=LevelExists(levelnumber2)
	SwapFile(levelnumber1,levelnumber2,".wlv",Exists1,Exists2)
	MasterLevelList(levelnumber1)=Exists2
	MasterLevelList(levelnumber2)=Exists1

End Function

Function SwapDialog(dialognumber1,dialognumber2)

	Exists1=DialogExists(dialognumber1)
	Exists2=DialogExists(dialognumber2)
	SwapFile(dialognumber1,dialognumber2,".dia",Exists1,Exists2)
	MasterDialogList(dialognumber1)=Exists2
	MasterDialogList(dialognumber2)=Exists1

End Function

Function CreateGeneralCommandTextMesh(col1)

	ExtraEntity=CreateMesh()
	Surface=CreateSurface(ExtraEntity)

	Command$=col1

	r=GetCommandColor(col1,0)
	g=GetCommandColor(col1,1)
	b=GetCommandColor(col1,2)

	scaling#=0.3
	yoffset#=-0.10

	;outlinesize#=0.01

	BuildTextSurface(Surface,Command$,-0.015,scaling#,0,0.0,yoffset#)

	EntityColor ExtraEntity,r,g,b

	RotateMesh ExtraEntity,90,0,0

	UpdateNormals ExtraEntity

	BackdropEntity=CreateMesh(ExtraEntity)
	Surface=CreateSurface(BackdropEntity)

	;scaling#=scaling#*2

	offset#=0.02

	BuildTextSurface(Surface,Command$,-0.014,scaling#,0,offset#,yoffset#)
	BuildTextSurface(Surface,Command$,-0.014,scaling#,1,-offset#,yoffset#)
	BuildTextSurface(Surface,Command$,-0.014,scaling#,2,0.0,yoffset#+offset#)
	BuildTextSurface(Surface,Command$,-0.014,scaling#,3,0.0,yoffset#-offset#)

	;radius#=0.45

	;AddVertex (surface,-radius,0.01,radius)
	;AddVertex (surface,radius,0.01,radius)
	;AddVertex (surface,-radius,0.01,-radius)
	;AddVertex (surface,radius,0.01,-radius)
	;AddTriangle (surface,0,1,2)
	;AddTriangle (surface,1,3,2)

	;EntityAlpha BackdropEntity,0.3

	EntityColor BackdropEntity,0,0,0
	;EntityColor BackdropEntity,255,255,255
	;EntityColor BackdropEntity,255-r,255-g,255-b

	RotateMesh BackdropEntity,90,0,0

	UpdateNormals BackdropEntity

	; Comment these out to debug the glyph placement.
	EntityTexture ExtraEntity,TextTexture
	EntityTexture BackdropEntity,TextTexture

	Return ExtraEntity

End Function

Function BuildTextSurface(mySurface,myString$,z#,scaling#,letteroffset=0,xoffset#=0.0,yoffset#=0.0)

	xsize#=scaling#
	ysize#=scaling#

	For i=1 To Len(myString$)
		let=Asc(Mid$(myString$,i,1))-32
		letternumber=i-1

		x#=-xsize#*(Len(myString$)-1)*0.5+(i-1)*xsize#
		y#=0
		AddTextToSurface(mySurface,letternumber+letteroffset*Len(myString$),let,x#+xoffset#,y#+yoffset#,z#,0.5*scaling#)
	Next

End Function

Function GetTextureNames()

	dir=ReadDir("data\leveltextures")

	NofLevelTextures=0
	NofWaterTextures=0

	file$=NextFile$(dir)
	While file$<>""
		If Lower$(Right$(file$,4))=".wdf" And Lower$(Left$(file$,9))="mfwfmufy "
			file$=decode$(Mid$(file$,10,Len(file$)-13))
			Leveltexturename$(NofLevelTextures)="leveltex "+file$+".bmp"
			NofLevelTextures=NofLevelTextures+1
		EndIf
		If Lower$(Right$(file$,4))=".wdf" And Lower$(Left$(file$,9))="xbufsufy "
			file$=decode$(Mid$(file$,10,Len(file$)-13))
			Watertexturename$(NofWaterTextures)="watertex "+file$+".jpg"
			NofWaterTextures=NofWaterTextures+1
		EndIf
		file$=NextFile$(dir)

	Wend

	CloseDir dir

End Function

Function MyProcessFileNameTexture$(ex$)

	j=Len(ex$)
	Repeat
		j=j-1
	Until Mid$(ex$,j,1)="/" Or Mid$(ex$,j,1)="\" Or j=1

	If j=1
		ex2$=""
		j=0
	Else
		ex2$=Left$(ex$,j)
	EndIf

	Repeat
		j=j+1
		b=Asc(Mid$(Lower$(ex$),j,1))
		If b>=97 And b<=122
			b=b+1
			If b=123 Then b=97
		EndIf
		ex2$=ex2$+Chr$(b)
	Until Mid$(ex$,j,1)="."
	ex2$=ex2$+"wdf"

	Return ex2$

End Function

Function MyProcessFileNameModel$(ex$)

	j=Len(ex$)
	Repeat
		j=j-1
	Until Mid$(ex$,j,1)="/" Or Mid$(ex$,j,1)="\" Or j=1

	If j=1
		ex2$=""
		j=0
	Else
		ex2$=Left$(ex$,j)
	EndIf

	Repeat
		j=j+1
		b=Asc(Mid$(Lower$(ex$),j,1))
		If b>=97 And b<=122
			b=b+1
			If b=123 Then b=97
		EndIf
		ex2$=ex2$+Chr$(b)
	Until Mid$(ex$,j,1)="."
	If Lower$(Right$(ex$,3))="3ds"
		ex2$=ex2$+"wd3"
	Else
		ex2$=ex2$+"wd1"
	EndIf

	Return ex2$

End Function

Function MyLoadSound(ex$)
;	MyWriteString(debugfile,"Sound: "+ex$)

	j=Len(ex$)
	Repeat
		j=j-1
	Until Mid$(ex$,j,1)="/" Or Mid$(ex$,j,1)="\" Or j=1

	If j=1
		ex2$=""
		j=0
	Else
		ex2$=Left$(ex$,j)
	EndIf

	Repeat
		j=j+1
		b=Asc(Mid$(Lower$(ex$),j,1))
		If b>=97 And b<=122
			b=b+1
			If b=123 Then b=97
		EndIf
		ex2$=ex2$+Chr$(b)
	Until Mid$(ex$,j,1)="."
	ex2$=ex2$+"wdf"

	;Print ex2$
	a=LoadSound(ex2$)
	If a=0
		jj=0
		Repeat
			jj=jj+1
		Until FileType("debug."+Str$(jj))=0

		debugfile=WriteFile ("debug."+Str$(jj))
	;	Print "Couldn't Load MD2:"+ex$
	;	Delay 5000
		WriteString debugfile,ex$
		WriteString debugfile,ex2$

		WriteInt debugfile,TotalVidMem()
		WriteInt debugfile,AvailVidMem()
	;	End
	EndIf

	Return a
End Function

Function DisplayText2(mytext$,x#,y#,red,green,blue,widthmult#=1.0)

	For i=1 To Len(mytext$)
		let=Asc(Mid$(mytext$,i,1))-32
		AddLetter(let,-.97+(x+(i-1)*widthmult)*CharWidth#,DisplayText2Y#(y),1,0,.04,0,0,0,0,0,0,0,0,0,red,green,blue)
	Next

End Function

Function DisplayText2Y#(y)

	Return .5-(y-4)*CharHeight#

End Function

Function DisplayCenteredText2(mytext$,x#,y#,red,green,blue,widthmult#=1.0)

	DisplayText2(mytext$,x#-Len(mytext$)/2,y#,red,green,blue,widthmult#)

End Function

Function AbsoluteDifference(a,b)

	Return Abs(a-b)

End Function

Function MouseTextEntryTrackMouseMovement()

	If AbsoluteDifference(MouseX(),OldMouseX)>3 Or AbsoluteDifference(MouseY(),OldMouseY)>3
		ShowPointer
	EndIf

End Function

Function GetNextMouseTextEntryLine(ScreenId,y)

	Max=MouseTextEntryLineMax(ScreenId)
	i=Max
	While i>=0
		If y>=MouseTextEntryLineY(ScreenId,i)
			If i>=Max
				Return Max
			Else
				Return i+1
			EndIf
		EndIf
		i=i-1
	Wend
	Return Max

End Function

Function GetPreviousMouseTextEntryLine(ScreenId,y)

	Max=MouseTextEntryLineMax(ScreenId)
	i=1
	While i<=Max
		If y<=MouseTextEntryLineY(ScreenId,i)
			Return i-1
		EndIf
		i=i+1
	Wend
	Return 0

End Function

Function MouseTextEntryGetMoveX(x)

	Return LetterX(x)+LetterWidth/3 ;x*18+9

End Function

Function MouseTextEntryGetMoveY(y,yadjust)

	Return LetterHeight*4.8+y*LetterHeight+yadjust ;87+y*20+yadjust+10

End Function

Function MouseTextEntryMoveMouse(x,y,yadjust)

	newx=MouseTextEntryGetMoveX(x)
	newy=MouseTextEntryGetMoveY(y,yadjust)
	MoveMouse newx,newy
	OldMouseX=newx
	OldMouseY=newy
	HidePointer()

End Function

Function MouseTextEntry$(tex$,let,x,y,yadjust,ScreenId)

	MouseTextEntryDelete=False

	If let>=32 And let<=122
		; place letter (let is the letter to place)
		tex$=Left$(tex$,x)+Chr$(let)+Right$(tex$,Len(tex$)-x)
		If Len(tex$)=39
			tex$=Left$(tex$,38)
		EndIf
		; and advance cursor
		If x<37
			MouseTextEntryMoveMouse(x+1,y,yadjust)
		EndIf
		ColEffect=-1
		TxtEffect=-1

		AddUnsavedChange()
	EndIf
	If CtrlDown() And MouseDown(1)
		; ctrl+click
		tex$=Left$(tex$,x)+InputString$("String to insert: ")+Right$(tex$,Len(tex$)-x)

		ColEffect=-1
		TxtEffect=-1

		AddUnsavedChange()
	EndIf
	If KeyDown(14)
		; backspace
		If x>0
			tex$=Left$(tex$,x-1)+Right$(tex$,Len(tex$)-x)
			MouseTextEntryMoveMouse(x-1,y,yadjust)
			Delay CharacterDeleteDelay
		EndIf
		ColEffect=-1
		TxtEffect=-1

		AddUnsavedChange()
		MouseTextEntryDelete=True
	EndIf
	If KeyDown(211)
		; delete
		If x<Len(tex$)
			tex$=Left$(tex$,x)+Right$(tex$,Len(tex$)-x-1)
			HidePointer
			Delay CharacterDeleteDelay
		EndIf
		ColEffect=-1
		TxtEffect=-1

		AddUnsavedChange()
		MouseTextEntryDelete=True
	EndIf

	; cursor movement
	If (KeyDown(200) Or KeyDown(72))
		; up arrow or numpad 8
		i=GetPreviousMouseTextEntryLine(ScreenId,y)
		MouseTextEntryMoveMouse(x,MouseTextEntryLineY(ScreenId,i),MouseTextEntryLineYAdjust(ScreenId,i))
		Delay 100
		ColEffect=-1
		TxtEffect=-1
	EndIf
	If (KeyDown(208) Or KeyDown(28) Or KeyDown(156)) And ReturnKeyReleased=True
		; down arrow or enter or numpad enter
		i=GetNextMouseTextEntryLine(ScreenId,y)
		MouseTextEntryMoveMouse(x,MouseTextEntryLineY(ScreenId,i),MouseTextEntryLineYAdjust(ScreenId,i))
		Delay 100
		ColEffect=-1
		TxtEffect=-1
	EndIf

	If (KeyDown(203)) And x>0
		; left arrow
		MouseTextEntryMoveMouse(x-1,y,yadjust)
		Delay 100
		ColEffect=-1
		TxtEffect=-1
	EndIf
	If (KeyDown(205)) And x<Len(tex$)
		; right arrow
		MouseTextEntryMoveMouse(x+1,y,yadjust)
		Delay 100
		ColEffect=-1
		TxtEffect=-1
	EndIf

	If KeyDown(199) ; home
		MouseTextEntryMoveMouse(0,y,yadjust)
		ColEffect=-1
		TxtEffect=-1
	EndIf

	If KeyDown(207) ; end
		endx=Len(tex$)
		If endx>37
			endx=37
		EndIf
		MouseTextEntryMoveMouse(endx,y,yadjust)
		ColEffect=-1
		TxtEffect=-1
	EndIf

	Return tex$

End Function

Function GetMouseLetterX()

	x=(MouseX()-LetterX(0))/LetterWidth-0.5
	If x<0
		x=0
	EndIf
	Return x

End Function

Function StartUserSelectScreen()
	SetEditorMode(4)

	EditorUserNameEntered$=""
	NofEditorUserNames=0

	dirfile=ReadDir(GlobalDirName$+"\Custom\Editing\Profiles")

	Repeat
		ex$=NextFile$(dirfile)
		If ex$<>"." And ex$<>".." And ex$<>"" And FileType(GlobalDirName$+"\custom\Editing\Profiles\"+ex$)=2
			EditorUserNamesListed$(NofEditorUserNames)=ex$
			NofEditorUserNames=NofEditorUserNames+1
		EndIf
	Until ex$=""

	CloseDir dirfile

	Camera1Proj=0
	Camera2Proj=0
	Camera3Proj=0
	Camera4Proj=0
	CameraProj=1
	UpdateCameraProj()

End Function

Function UserSelectScreen()

	leveltimer=leveltimer+1

	my=MouseY()
	StartY=LetterHeight*15
	If mY>=StartY
		EditorUserNameSelected=(my-StartY-LetterHeight*0.5)/LetterHeight
	Else
		EditorUserNameSelected=-1
	EndIf

	DisplayText2("Editor Profile Name Selector",0,0,255,255,255)
	DisplayText2("-------------------------------------------",0,1,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("Select your Editor Profile Name. This name",0,2,155,155,155)
	DisplayText2("is attached to all levels you create. You",0,3,155,155,155)
	DisplayText2("should use the same name as your Wonderland",0,4,155,155,155)
	DisplayText2("Forum login, so other players can identify",0,5,155,155,155)
	DisplayText2("your levels easily.",0,6,155,155,155)

	DisplayText2("-------------------------------------------",0,7,TextMenusR,TextMenusG,TextMenusB)

	DisplayText2("Enter New Profile Name (use Keyboard):",0,10,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2(EditorUserNameEntered$,0,11,255,255,255)
	If leveltimer Mod 100 <50
		DisplayText2(":",Len(EditorUserNameEntered$),11,255,255,255)
	EndIf

	If NofEditorUserNames>0
		DisplayText2("Or Select Existing Profile (use Mouse):",0,14,TextMenusR,TextMenusG,TextMenusB)
		For i=0 To NofEditorUserNames-1
			If i=EditorUserNameSelected
				DisplayText2(EditorUserNamesListed$(i),5,15+i,255,255,255)
			Else
				DisplayText2(EditorUserNamesListed$(i),5,15+i,155,155,155)
			EndIf

		Next
	EndIf

;	DisplayText2("---CANCEL---",16.5,27,TextMenusR,TextMenusG,TextMenusB)

	; Entering New Name
	let=GetKey()
	If let>=32 And let<=122 And Len(editorusernameentered$)<20
		EditorUserNameEntered$=EditorUserNameEntered$+Chr$(let)
	EndIf
	If KeyDown(14)
		; backspace key
		If Len(EditorUserNameEntered$)>0
			EditorUserNameEntered$=Left$(EditorUserNameEntered$,Len(EditorUserNameEntered$)-1)
			Delay CharacterDeleteDelay
		EndIf
	EndIf
	If KeyDown(211)
		; delete key
		EditorUserNameEntered$=""
		Delay CharacterDeleteDelay
	EndIf
	If (KeyPressed(28) Or KeyPressed(156)) And ReturnKeyReleased=True
		; Enter key

		If EditorUserNameEntered$=""
			DisplayText2("INVALID PROFILE NAME - Empty Name!",0,12,TextMenusR,TextMenusG,TextMenusB)
		Else If FileType(GlobalDirName$+"\Editor Profiles\"+EditorUserNameEntered$)=2
			DisplayText2("INVALID PROFILE NAME - Already Exists!",0,12,TextMenusR,TextMenusG,TextMenusB)
		Else
			DisplayText2("SAVING PROFILE NAME - Please Wait!",0,12,TextMenusR,TextMenusG,TextMenusB)
			CreateDir GlobalDirName$+"\custom\Editing\Profiles\"+EditorUserNameEntered$

			EditorUserName$=EditorUserNameEntered$
			file=WriteFile(GlobalDirName$+"\custom\Editing\Profiles\currentuser.dat")
			WriteString file,EditorUserName$
			CloseFile file

			StartAdventureSelectScreen()
		EndIf
		waitflag=True
	EndIf

	If LeftMouse
		If FileType(GlobalDirName$+"\custom\Editing\Profiles\"+EditorUserNamesListed$(EditorUserNameSelected))=2 And EditorUserNamesListed$(EditorUserNameSelected)<>""
			EditorUserName$=EditorUserNamesListed$(EditorUserNameSelected)
			file=WriteFile(GlobalDirName$+"\custom\Editing\Profiles\currentuser.dat")
			WriteString file,EditorUserName$
			CloseFile file
			StartAdventureSelectScreen()
			DisplayText2("---->",0,15+EditorUserNameSelected,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2("<----",39,15+EditorUserNameSelected,TextMenusR,TextMenusG,TextMenusB)
			waitflag=True
		EndIf
	EndIf

	RenderLetters()
	UpdateWorld
	RenderWorld

	FinishDrawing()

	If waitflag=True Delay 2000

End Function

Function SettingsMainLoop()
	MX=MouseX()
	my=MouseY()
	StartY=LetterHeight*9
	If mx>LetterX(15) And mx<LetterX(29) And my>StartY And my<StartY+LetterHeight*8
		Selected=(my-StartY-LetterHeight*0.5)/(LetterHeight*2)
	Else
		Selected=-1
	EndIf

	DisplayText2("Settings",0,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("============================================",0,1,TextMenusR,TextMenusG,TextMenusB)

	If Selected=0
		DisplayCenteredText2("Controls",22,9,255,255,255)
	Else
		DisplayCenteredText2("Controls",22,9,155,155,155)
	EndIf
	ex$=Str$(MyGfxModeWidth(GfxMode))+"x"+Str$(MyGfxModeHeight(GfxMode))+"x"+Str$(MyGfxModeDepth(GfxMode))
	If Selected=1
		DisplayCenteredText2("Resolution "+ex$,22,11,255,255,255)
	Else
		DisplayCenteredText2("Resolution "+ex$,22,11,155,155,155)
	EndIf
	If Selected=2
		DisplayCenteredText2("Apply Resolution",22,13,255,255,255)
	Else
		DisplayCenteredText2("Apply Resolution",22,13,155,155,155)
	EndIf
	If Selected=3
		DisplayText2("Back",20,15,255,255,255)
	Else
		DisplayText2("Back",20,15,155,155,155)
	EndIf

	If LeftMouse
		If selected=0
			ConfigureControls()

			Repeat
			Until LeftMouseDown()=0
		EndIf
		If selected=1
			GfxMode=GfxMode+1
			If GfxMode=NofMyGfxModes Then GfxMode=0

			Repeat
			Until LeftMouseDown()=0
		EndIf
		If selected=2
			GfxWidth=MyGfxModeWidth(gfxmode)
			GfxHeight=MyGfxModeHeight(gfxmode)
			GfxDepth=MyGfxModeDepth(gfxmode)

			;Graphics3D GfxWidth,GfxHeight,GfxDepth,GfxWindowed
			;ResolutionWasChanged()

			WriteDisplayFile()

			Repeat
			Until LeftMouseDown()=0

			ExecFile ("editor3d.exe")
			EndApplication()

		EndIf
		If selected=3
			SetEditorMode(5)

			Repeat
			Until LeftMouseDown()=0
		EndIf
	EndIf
	If RightMouse
		If selected=1
			GfxMode=GfxMode-1
			If GfxMode=-1 Then GfxMode=NofMyGfxModes-1

			Repeat
			Until RightMouseDown()=0
		EndIf
	EndIf

	RenderLetters()
	UpdateWorld
	RenderWorld

	FinishDrawing()

End Function

Function StartMaster()
	RestoreOriginalMaster()
	RestoreOriginal1Wlv()

	StopMusic()

	AdventureUserName$=GetAuthorFromAdventureTitle$(AdventureFileName$)

	ResetPreviousLevelNumberBuffer()

	PreviewCurrentDialog=0

	SetEditorMode(8)

	CopyingLevel=StateNotSpecial
	CopyingDialog=StateNotSpecial

	Camera1Proj=0
	Camera2Proj=0
	Camera3Proj=0
	Camera4Proj=0
	CameraProj=1
	UpdateCameraProj()

	For i=1 To MaxLevel
		; check existence of wlv and dia files
		MasterLevelList(i)=0
		If LevelExists(i)
			MasterLevelList(i)=1
		EndIf
	Next

	For i=1 To MaxDialog
		MasterDialogList(i)=0
		If DialogExists(i)
			MasterDialogList(i)=1
		EndIf
	Next

	; check existence of a master.dat file
	If IconTextureCustom>0
		FreeTexture IconTextureCustom
		IconTextureCustom=0
	EndIf
	If FileType(GetAdventureDir$()+"master.dat")=1
		LoadMasterFile()
		If customiconname$="Standard"
			IconTextureCustom=myLoadTexture("data\Graphics\icons-custom.bmp",4)
		Else
			IconTextureCustom=myLoadTexture(globaldirname$+"\Custom\Icons\icons "+customiconname$+".bmp",4)
		EndIf
	Else
		CustomIconName$="Standard"
		CustomMapName$=""
		IconTextureCustom=myLoadTexture("data\Graphics\icons-custom.bmp",4)
		AdventureStartX=1
		AdventureStartY=1 ; x/y position of player start
		AdventureStartDir=180 ;0
		AdventureTitle$=""
		For i=0 To 4
			AdventureTextLine$(i)=""
		Next
		MasterDialogListStart=0
		MasterLevelListStart=0
		AdventureExitWonLevel=0
		AdventureExitWonX=0
		AdventureExitWonY=0 ; at what hub level and x/y do you reappear if won.
		AdventureExitLostLevel=0
		AdventureExitLostX=0
		AdventureExitLostY=0; at what hub level And x/y do you reappear If won.
		AdventureGoal=0	; when is adventure done
							; 1-NofWeeStinkersInAdventure=0
		For i=0 To 2
			For j=0 To 5
				AdventureWonCommand(i,j)=0
			Next	; 3 commands, each with level/command/fourdata
		Next

		StarterItems=7 ;to do: decide the default value for new adventures
		WidescreenRange=0

		SelectedShard=0
		SelectedGlyph=0
		CustomShardEnabled=0
		CustomGlyphEnabled=0
		For i=0 To NoOfShards-1
			For j=0 To 4
				CustomShardCMD(i,j)=0
			Next
		Next
		For i=0 To NoOfGlyphs-1
			For j=0 To 4
				CustomGlyphCMD(i,j)=0
			Next
		Next
	EndIf

End Function

Function StartHub()

	SetEditorMode(11)
	HubAdvStart=0
	HubSelectedAdventure=-1
	CopyingLevel=StateNotSpecial

	ClearHub()

	If FileType(globaldirname$+"\Custom\editing\Hubs\"+HubFileName$+"\hub.dat")=1
		LoadHubFile()
	Else
		HubTitle$=""
		HubDescription$=""
		HubTotalAdventures=0
	EndIf

End Function

Function ClearHub()

	For i=0 To HubAdvMax
		HubAdventuresFilenames$(i)=""
		HubAdventuresMissing(i)=False
		HubAdventuresIncludeInTotals(i)=True
	Next

End Function

Function ResumeMaster()

	RestoreOriginalMaster()
	RestoreOriginal1Wlv()

	StopMusic()

	PreviewCurrentDialog=0

	SetEditorMode(8)

	CopyingLevel=StateNotSpecial
	CopyingDialog=StateNotSpecial

	Camera1Proj=0
	Camera2Proj=0
	Camera3Proj=0
	Camera4Proj=0
	CameraProj=1
	UpdateCameraProj()

	For i=1 To MaxLevel
		; check existence of wlv and dia files
		MasterLevelList(i)=0
		If LevelExists(i)
			MasterLevelList(i)=1
		EndIf
	Next
	For i=1 To MaxDialog

		MasterDialogList(i)=0
		If DialogExists(i)
			MasterDialogList(i)=1
		EndIf
	Next

End Function

Function ReadTestFile()

	testfile=ReadFile(globaldirname$+"\temp\test.dat")
	If testfile<>0 ;FileType(globaldirname$+"\custom\editing\")
		AdventureFileName$=ReadString$(testfile)
		HubFileName$=ReadString$(testfile)
		If HubFileName$<>""
			hubmode=True
			StartHub()
		EndIf
		StartMaster()
		SetEditorMode(ReadInt(testfile))

		If EditorMode=EditorModeTile Or EditorMode=EditorModeObject
			level=ReadInt(testfile)
			x=ReadInt(testfile)
			y=ReadInt(testfile)
			AccessLevelAt(level,x,y)
			StartEditorMainLoop()
		EndIf

		CloseFile testfile
		DeleteFile globaldirname$+"\temp\test.dat"

	EndIf

End Function

; AKA Save and Test / SaveAndTest / Save+Test
Function StartTestMode(TestAtX=0,TestAtY=0)

	WaitFlag=True
	SaveMasterFile()

	file=WriteFile(globaldirname$+"\temp\test.dat")
	WriteString file,AdventureFileName$
	If hubmode
		WriteString file,HubFileName$
	Else
		WriteString file,""
	EndIf

	WriteInt file, EditorMode

	If EditorMode=EditorModeTile Or EditorMode=EditorModeObject
		WriteInt file,CurrentLevelNumber
		WriteInt file,TestAtX
		WriteInt file,TestAtY
	EndIf

	CloseFile file

	If hubmode
		SaveHubFile()
	EndIf

	ExecFile ("wg.exe")
	EndApplication()

End Function

Const OriginalMasterDat$="__master_ORIGINAL__.bak"
Const Original1Wlv$="__1_ORIGINAL__.bak"

; This is hacky, but it should work regardless of the modded executable's version. Most likely does not work in vanilla.
Function StartTestModeAt(level,x,y)

	MakeAllObjectAdjustersNotRandomized() ; Prevents field randomization on the test arrow.

	RestoreOriginalMaster()
	RestoreOriginal1Wlv()

	SaveMasterFile()
	DuplicateMaster("master",OriginalMasterDat$)
	; Change adventure start coordinates to likely be out-of-bounds.
	; Negative coordinates were formerly used, but this caused a variety of memory issues, including some tile logics apparently changing.
	AdventureStartX=100
	AdventureStartY=100
	; master.dat gets saved in StartTestMode, so we don't have to save it here.

	If CurrentLevelNumber<>1
		AccessLevel(1)
		; If 1.wlv does not exist, create the file.
		If Not LevelExists(1)
			SaveLevel()
		EndIf
	EndIf
	DuplicateLevel(1,Original1Wlv$)
	; Place a level transition at the adventure start coordinates.
	GenerateLevelExitTo(level,x,y)
	CurrentObject\Attributes\ZAdjust=1000.0 ; Move the LevelExit out of view in-game.
	PreventPlacingObjectsOutsideLevel=False
	PlaceThisObject(AdventureStartX,AdventureStartY,CurrentObject)
	ObjectsWereChanged()
	SaveLevel()
	CurrentLevelNumber=level ; Necessary so that the editor knows what level to return to when re-opening after testing.
	StartTestMode(x,y)

End Function

Function FileExists(FileName$)

	If FileType(FileName$)=1
		Return True
	Else
		Return False
	EndIf

End Function

Function FileExistsModel(FileName$)

	Return FileExists(MyProcessFileNameModel$(FileName$))

End Function

Function FileExistsTexture(FileName$)

	Return FileExists(MyProcessFileNameTexture$(FileName$))

End Function

Function MasterFileExists()

	Return FileExists(GetAdventureDir$()+"master.dat")

End Function

Function LoadAdventureTitle$()

	file=ReadFile (GetAdventureDir$()+"master.dat")
	ex$=ReadString$(file)
	CloseFile file
	Return ex$

End Function

Function LoadMasterFile()

	file=ReadFile (GetAdventureDir$()+"master.dat")

	adventuretitle$=ReadString$(file)
	For i=0 To 4
		adventuretextline$(i)=ReadString$(file)
	Next

	ReadString$(file) ;user (not loaded)
	CustomIconName$=ReadString$(file)
	If CustomIconName$="" Or CustomIconName$="Standard"
		CustomIconName$="Standard"
	Else
		If FileType(globaldirname$+"\custom\icons\icons "+CustomIconName$+".bmp")<>1
			Cls
			Print "Error: Custom Icon File '"+customiconname$+"' not found."
			Print "Reverting to 'Standard' Custom Icon Texture."
			Delay 2000
			CustomIconName$="Standard"
		EndIf

	EndIf

	CustomMapName$=ReadString$(file)
	If CustomMapName$<>""
		For i=0 To 8
			If FileType(GlobalDirName$+"\Custom\Maps\"+CustomMapName$+"\mappiece"+Str$(i)+".bmp")<>1
				Cls
				Print "Error: Custom Map piece'"+CustomMapName$+"\mappiece"+Str$(i)+".bmp' not found."
				Print "Reverting to no map."
				Delay 2000
				CustomMapName$=""
				Exit
			EndIf
		Next
	EndIf
	ReadString$(file)
	ReadString$(file)
	ReadString$(file)
	ReadString$(file)

	AdventureExitWonLevel=ReadInt(file)
	AdventureExitWonX=ReadInt(file)
	AdventureExitWonY =ReadInt(file); at what hub level and x/y do you reappear if won.
	AdventureExitLostLevel=ReadInt(file)
	AdventureExitLostX=ReadInt(file)
	AdventureExitLostY =ReadInt(file); at what hub level and x/y do you reappear if won.

	AdventureGoal=ReadInt(file)	; when is adventure done
						; 1-NofWeeStinkersInAdventure=0

	For i=0 To 2
		For j=0 To 5
			AdventureWonCommand(i,j)=ReadInt(file)
		Next
	Next

	adventurestartx=ReadInt(file)
	adventurestarty=ReadInt(file)
	GateKeyVersion=1
	If Eof(file)=False GateKeyVersion=ReadInt(file)

	adventurestartdir=180 ;0
	If Eof(file)=False Adventurestartdir=ReadInt(file)+180

	StarterItems=7
	If Eof(file)=False StarterItems=ReadInt(file)
	WidescreenRange=0
	If Eof(file)=False WidescreenRange=ReadInt(file)

	; reset
	CustomShardEnabled=0
	CustomGlyphEnabled=0
	SelectedShard=0
	SelectedGlyph=0
	For i=0 To NoOfShards-1
		For j=0 To 4
			CustomShardCMD(i,j)=0
		Next
	Next
	For i=0 To NoOfGlyphs-1
		For j=0 To 4
			CustomGlyphCMD(i,j)=0
		Next
	Next

	; load
	If Eof(file)=False
		CustomShardEnabled=ReadInt(file)
		If CustomShardEnabled>0
			For i=0 To CustomShardEnabled-1
				For j=0 To 4
					CustomShardCMD(i,j)=ReadInt(file)
				Next
			Next
		EndIf
		CustomGlyphEnabled=ReadInt(file)
		If CustomGlyphEnabled>0
			For i=0 To CustomGlyphEnabled-1
				For j=0 To 4
					CustomGlyphCMD(i,j)=ReadInt(file)
				Next
			Next
		EndIf
	EndIf
	CloseFile file

	FreeTexture buttontexture
	FreeTexture gatetexture
	ButtonTexture=MyLoadTexture("data\graphics\buttons"+Str$(GateKeyVersion)+".bmp",4)
	GateTexture=MyLoadTexture("data\graphics\gates"+Str$(GateKeyVersion)+".bmp",1)

End Function

Function RemoveLeft$(TheString$,Length)

	Return Right$(TheString$,Len(TheString$)-Length)

End Function

Function LoadHubFile()
	file=ReadFile(globaldirname$+"\Custom\editing\Hubs\"+HubFileName$+"\hub.dat")
	version=ReadInt(file)
;	flag=False
	If version=0
		HubTitle$=ReadString(file)
		HubDescription$=ReadString(file)
		HubTotalAdventures=ReadInt(file)
		For i=0 To HubTotalAdventures
			HubAdventuresFilenames$(i)=ReadString(file)
			HubAdventuresMissing(i)=False
			If FileType(GetHubAdventurePath$(HubAdventuresFilenames$(i)))<>2
				HubAdventuresMissing(i)=True
				;HubAdventuresFilenames$(i)="" ; remove
;				If HubTotalAdventures=i
					;find the new HubTotalAdventures
;					For i=HubTotalAdventures To 1 Step -1
;						If HubAdventuresFilenames$(i)<>""
;							Exit
;						EndIf
;						HubTotalAdventures=HubTotalAdventures-1
;					Next
;					flag=True
;				EndIf
			EndIf
		Next
;		If flag
;			Cls
;			Locate 0,0
;			Print "Warning: At least one adventure is missing."
;			Print "Missing adventures are removed from the hub automatically."
;			Delay 3000
;		EndIf
	Else
		Cls
		Locate 0,0
		Print "Error: Unsupported hub file version."
		Print "Please update your editor."
		Delay 2000
	EndIf
End Function

Function SaveHubFile()
	file=WriteFile(globaldirname$+"\Custom\editing\Hubs\"+HubFileName$+"\hub.dat")
	WriteInt file,0
	WriteString file,HubTitle$
	WriteString file,HubDescription$
	WriteInt file,HubTotalAdventures
	For i=0 To HubTotalAdventures
		WriteString file,HubAdventuresFilenames$(i)
	Next
	CloseFile file
End Function

Function SaveMasterFile()

	file=WriteFile (GetAdventureDir$()+"master.dat")

	WriteString file,adventuretitle$
	For i=0 To 4
		WriteString file,adventuretextline$(i)
	Next

	WriteString file,AdventureUserName$
	WriteString file,CustomIconName$
	WriteString file,CustomMapName$ ;""
	WriteString file,""
	WriteString file,""
	WriteString file,""
	WriteString file,""

	WriteInt file, AdventureExitWonLevel
	WriteInt file, AdventureExitWonX
	WriteInt file, AdventureExitWonY ; at what hub level and x/y do you reappear if won.
	WriteInt file, AdventureExitLostLevel
	WriteInt file, AdventureExitLostX
	WriteInt file, AdventureExitLostY ; at what hub level and x/y do you reappear if won.

	WriteInt file, AdventureGoal	; when is adventure done
						; 1-NofWeeStinkersInAdventure=0

	For i=0 To 2
		For j=0 To 5
			WriteInt file, AdventureWonCommand(i,j)
		Next
	Next

	WriteInt file,adventurestartx
	WriteInt file,adventurestarty

	WriteInt file,GateKeyVersion

	WriteInt file,AdventureStartDir-180

	WriteInt file,StarterItems
	WriteInt file,WidescreenRange

	CustomShardEnabled=0
	For i=0 To NoOfShards-1
		If CustomShardCMD(i,0)>0
			CustomShardEnabled=NoOfShards
		EndIf
	Next

	CustomGlyphEnabled=0
	For i=0 To NoOfGlyphs-1
		If CustomGlyphCMD(i,0)>0
			CustomGlyphEnabled=NoOfGlyphs
		EndIf
	Next

	WriteInt file,CustomShardEnabled
	If CustomShardEnabled>0
		For i=0 To CustomShardEnabled-1
			For j=0 To 4
				WriteInt file,CustomShardCMD(i,j)
			Next
		Next
	EndIf

	WriteInt file,CustomGlyphEnabled
	If CustomGlyphEnabled>0
		For i=0 To CustomGlyphEnabled-1
			For j=0 To 4
				WriteInt file,CustomGlyphCMD(i,j)
			Next
		Next
	EndIf
	CloseFile file

End Function

Function HubChecks()

	If HubTitle$=""
		Print "ERROR: No Hub Title set."
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf

	If HubAdventuresFilenames$(0)=""
		Print "ERROR: No Hub defined."
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf

	If IsHubMissingAdventures()
		Print "ERROR: Hub is missing adventures (see red names in hub editor)."
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf

	Return True

End Function

Function BuildHub()
	Cls
	Locate 0,0
	Print ""
	Print "Building..."
	Print "------------"
	Print ""
	Print ""

	If HubChecks()=False
		Return False
	EndIf

	fn$=HubTitle$
	If HubDescription$<>""
		fn$=HubTitle$+"#"+HubDescription$
	EndIf
	HubDir$=globaldirname$+"\Custom\hubs\"+fn$
	CreateDir(HubDir$)

	If FileType(HubDir$)<>2
		Print "ERROR: Unable to create directory."
		Print "Check that any characters in the hub title or description can be part of a folder name."
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf

	; clear directory first
	dirfileclear=ReadDir(HubDir$)
	Repeat
		f$=NextFile$(dirfileclear)
		If FileType(HubDir$+"\"+f$)=1 And f$<>""
			DeleteFile HubDir$+"\"+f$
		ElseIf FileType(HubDir$+"\"+f$)=2 And f$<>"." And f$<>".." And f$<>""
			dirfileclearsub=ReadDir(HubDir$+"\"+f$)
			Repeat
				f1$=NextFile$(dirfileclearsub)
				If FileType(HubDir$+"\"+f$+"\"+f1$)=1
					DeleteFile HubDir$+"\"+f$+"\"+f1$
				EndIf
			Until f1$=""
			DeleteDir HubDir$+"\"+f$
		EndIf
	Until f$=""
	;WaitKey()

	NofWlvFiles=0
	NofDiaFiles=0
	NofAdventures=0

	; copy files
	For i=0 To HubTotalAdventures
		AdvFilename$=""
		If i=0
			AdvFilename$="Hub"
		Else
			AdvFilename$="Adventure"+Str(i)
		EndIf
		If HubAdventuresFilenames$(i)<>""
			CreateDir(HubDir$+"\"+AdvFilename$)
			dirfile=ReadDir(GetHubAdventurePath$(HubAdventuresFilenames$(i)))
			Print "Building "+AdvFilename$+"..."

			Repeat
				ex$=NextFile$(dirfile)
				If FileSatisfiesCompiler(ex$,HubAdventuresIncludeInTotals(i),i=0)
					Print "Copying... "+ex$
					CopyFile GetHubAdventurePath$(HubAdventuresFilenames$(i))+"\"+ex$, HubDir$+"\"+AdvFilename$+"\"+ex$
				EndIf
			Until ex$=""
		EndIf
	Next

	If FileType(globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\hublogo.jpg")
		Print "Copying hublogo.jpg..."
		CopyFile globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\hublogo.jpg", HubDir$+"\hublogo.jpg"
	EndIf

	If FileType(globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\wonderlandadventures.bmp")
		Print "Copying wonderlandadventures.bmp..."		CopyFile globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\wonderlandadventures.bmp", HubDir$+"\wonderlandadventures.bmp"
	EndIf

	Print ""
	Print ""
	Color 0,255,0
	Print NofWlvFiles+" wlv files and "+NofDiaFiles+" dia files in total."
	Print NofAdventures+" adventures in total."
	Color 255,255,255
	Print ""
	Print ""

	Print "Build Completed..."
	Print "You can now play/test your hub."

	Delay 500
	Print ""
	Print "Click to Continue."
	Repeat
	Until LeftMouseDown()=True
	Repeat
	Until LeftMouseDown()=False
	Return True

End Function

Function CompileHub(PackContent)
	Cls
	Locate 0,0

	Print ""
	Print "Compiling..."
	Print "------------"
	Print ""

	If HubChecks()=False
		Return False
	EndIf

	fn$=HubTitle$
	If HubDescription$<>""
		fn$=HubTitle$+"#"+HubDescription$
	EndIf

	file1=WriteFile(globaldirname$+"\Custom\downloads inbox\"+fn$+".wah")

	If file1=0
		Print "ERROR: Unable to create WAH file."
		Print "Check that any characters in the hub title or description can be part of a filename."
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf

	NofWlvFiles=0
	NofDiaFiles=0
	NofAdventures=0

	For i=0 To HubTotalAdventures
		AdvFilename$=""
		If i=0
			AdvFilename$="Hub"
		Else
			AdvFilename$="Adventure"+Str(i)
		EndIf
		If HubAdventuresFilenames$(i)<>""

			ThePath$=GetHubAdventurePath$(HubAdventuresFilenames$(i))
			If PackContent
				SearchForCustomContent(ThePath$)
			EndIf
			dirfile=ReadDir(ThePath$)
			Print ""
			Print "Reading "+AdvFilename$+"..."
			NofHubCompilerFiles(i)=0
			Repeat
				ex$=NextFile$(dirfile)
				If FileSatisfiesCompiler(ex$,HubAdventuresIncludeInTotals(i),i=0)
					Print "Reading... "+ex$
					HubCompilerFileName$(i,NofHubCompilerFiles(i))=ex$
					HubCompilerFileSize(i,NofHubCompilerFiles(i))=FileSize(ThePath$+"\"+ex$)
					NofHubCompilerFiles(i)=NofHubCompilerFiles(i)+1
				EndIf
			Until ex$=""

		EndIf
	Next
	Delay  1000
	Print ""
	Print ""
	Print "Writing WAH File to Downloads Inbox..."
	Print ""
	Print ""

	HubTotal=0
	For k=0 To HubTotalAdventures
		If HubAdventuresFilenames$(k)<>""
			HubTotal=HubTotal+1
		EndIf
	Next

	WriteInt file1,HubTotal
	For k=0 To HubTotalAdventures
		If HubAdventuresFilenames$(k)<>""
			Print ""
			If k=0
				Print "Writing Hub..."
			Else
				Print "Writing Adventure"+Str$(k)+"..."
			EndIf
			WriteInt file1,k
			WriteInt file1,NofHubCompilerFiles(k)
			For i=0 To NofHubCompilerFiles(k)-1
				Print "Writing... "+HubCompilerFileName$(k,i)

				WriteString file1,HubCompilerFileName$(k,i)
				WriteInt file1,HubCompilerFileSize(k,i)

				file2=ReadFile(GetHubAdventurePath$(HubAdventuresFilenames$(k))+"\"+HubCompilerFileName$(k,i))

				For j=0 To HubCompilerFileSize(k,i)-1
					WriteByte file1,ReadByte (file2)
				Next
				CloseFile file2

			Next
		EndIf
	Next

	If PackContent
		Print
		Print "Packing custom content..."
		WriteInt file1, NofCustomContentFiles
		For i=0 To NofCustomContentFiles-1
			Print "Writing... " + CustomContentFile$(i)
			size=FileSize(CustomContentFile$(i))
			WriteString file1, CustomContentFile$(i)
			WriteInt file1, size
			file2=ReadFile(CustomContentFile$(i))
			For j=0 To size-1
				WriteByte file1, ReadByte(file2)
			Next
			CloseFile file2
		Next
	Else
		WriteInt file1,0
	EndIf

	;hublogo.jpg
	If FileType(globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\hublogo.jpg")
		file2=ReadFile(globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\hublogo.jpg")
		logosize=FileSize(globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\hublogo.jpg")
		Print "Packing hublogo.jpg..."+logosize
		WriteInt file1,logosize
		For j=0 To logosize-1
			WriteByte file1,ReadByte (file2)
		Next
	Else
		WriteInt file1,0
	EndIf

	;wonderlandadventures.bmp
	If FileType(globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\wonderlandadventures.bmp")
		file2=ReadFile(globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\wonderlandadventures.bmp")
		logosize=FileSize(globaldirname$+"\Custom\editing\hubs\"+HubFileName$+"\wonderlandadventures.bmp")
		Print "Packing wonderlandadventures.bmp..."+logosize
		WriteInt file1,logosize
		For j=0 To logosize-1
			WriteByte file1,ReadByte (file2)
		Next
	Else
		WriteInt file1,0
	EndIf

	CloseFile file1

	Print ""
	Print ""
	Color 0,255,0
	Print NofWlvFiles+" wlv files and "+NofDiaFiles+" dia files in total."
	Print NofAdventures+" adventures in total."
	Color 255,255,255

	Delay 1000
	Print ""
	Print ""
	Print "Copying File to Downloads Outbox..."
	CopyFile globaldirname$+"\Custom\downloads inbox\"+fn$+".wah",globaldirname$+"\Custom\downloads outbox\"+fn$+".wah"
	Print ""
	Delay 1000
	Print "Compile Completed... Filename: "+fn$+".wah"
	Print "You can now play/test your hub."

	Delay 500
	Print ""
	Print "Click to Continue."
	Repeat
	Until LeftMouseDown()=True
	Repeat
	Until LeftMouseDown()=False
	Return True
End Function

Function CompileAdventure(PackCustomContent)

	AdventureFileNameWithoutAuthor$=AdventureTitleWithoutAuthor$(AdventureFileName$)

	If AdventureFileNameWithoutAuthor$=AdventureFileName$
		OutputFileName$=AdventureUserName$+"#"+AdventureFileName$+".wa3"
	Else
		OutputFileName$=AdventureFileName$+".wa3"
	EndIf

	Cls
	Locate 0,0
	Print ""
	Print "Compiling..."
	Print "------------"
	Print ""
	; do some basic checks
	If adventuretitle$=""
		Print "ERROR: No Adventure Title set."
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf

	AdventureFolder$=GetAdventureFolder$()

	If FileType(AdventureFolder$+"\1.wlv")=0
		Print "ERROR: No Level 1 present."
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf

	If PackCustomContent
		; find custom content
		Print "Finding Custom Content..."
		SearchForCustomContent(AdventureFolder$)
		Print "Number of Custom Content Files: " + NofCustomContentFiles
		Print
	EndIf

	If DialogKillLineEight
		For i=1 To 100
			If DialogExists(i)
				CurrentDialog=i
				LoadDialogFile()
				SaveDialogFile()
			EndIf
		Next
	EndIf

	; now go through directory and check names and sizes of files
	dirfile=ReadDir(AdventureFolder$)

	NofCompilerFiles=0
	NofWlvFiles=0
	NofDiaFiles=0

	Repeat
		ex$=NextFile$(dirfile)

		If FileSatisfiesCompiler(ex$,True,False)
			Print "Reading... "+ex$
			CompilerFileName$(NofCompilerFiles)=ex$
			CompilerFileSize(NofCompilerFiles)=FileSize(AdventureFolder$+"\"+ex$)
			NofCompilerFiles=NofCompilerFiles+1
		EndIf
	Until ex$=""

	If PackCustomContent And NofCustomContentFiles>0
		; read custom content
		Print "Copying custom content files to: "+GlobalDirName$+"/Custom/Downloads Outbox/"+AdventureFileName$+"_Content"
		If FileType(GlobalDirName$+"/Custom/Downloads Outbox/"+AdventureFileName$+"_Content")=2
			CleanDir(GlobalDirName$+"/Custom/Downloads Outbox/"+AdventureFileName$+"_Content")
		EndIf
		CreateDir(GlobalDirName$+"/Custom/Downloads Outbox/"+AdventureFileName$+"_Content")
		For i=0 To NofCustomContentFiles-1
			Print "Copying... "+GetFileNameFromPath$(CustomContentFile$(i))
			MyCreateDir GlobalDirName$+"/Custom/Downloads Outbox/"+AdventureFileName$+"_Content/"+CustomContentFile$(i)
			CopyFile CustomContentFile$(i),GlobalDirName$+"/Custom/Downloads Outbox/"+AdventureFileName$+"_Content/"+CustomContentFile$(i)
		Next
		; now cleanup old Values
		For i=0 To 499
			CustomContentFile$(i)=""
		Next
		Print ""
		Print ""
	EndIf

	Delay 1000
	; and now make the master file
	Print ""
	Print ""
	Print "Writing WA3 File to Downloads Inbox..."
	Print ""
	Print ""
	file1=WriteFile(globaldirname$+"\Custom\downloads inbox\"+OutputFileName$)
	If file1=0
		Print "ERROR: Cannot Write "+globaldirname$+"\Custom\downloads inbox\"+OutputFileName$
		Print "Aborting..."
		Delay 3000
		Return False
	EndIf

	WriteInt file1,NofCompilerFiles

	For i=0 To NofCompilerFiles-1
		Print "Writing... "+CompilerFileName$(i)

		WriteString file1,CompilerFileName$(i)
		WriteInt file1,CompilerFileSize(i)

		file2=ReadFile(AdventureFolder$+"\"+CompilerFileName$(i))
		If Not file2
			file2=ReadFile(CompilerFileName$(i))
		EndIf

		For j=0 To CompilerFileSize(i)-1
			WriteByte file1,ReadByte (file2)
		Next

		CloseFile file2
	Next
	CloseFile file1

	Print ""
	Color 0,255,0
	Print NofWlvFiles+" wlv files and "+NofDiaFiles+" dia files in total."
	Color 255,255,255

	Delay 1000
	Print ""
	Print ""
	Print "Copying File to Downloads Outbox..."
	CopyFile globaldirname$+"\Custom\downloads inbox\"+OutputFileName$,globaldirname$+"\Custom\downloads outbox\"+OutputFileName$
	Print ""
	Delay 1000
	Print "Compile Completed... Filename: "+OutputFileName$
	Print "You can now play/test your level."

	Delay 500
	Print ""
	Print "Click to Continue."
	Repeat
	Until LeftMouseDown()=True
	Repeat
	Until LeftMouseDown()=False

	Return True

End Function

Function decode$(ex$)
	output$=""
	For i=1 To Len(ex$)
		b=Asc(Mid$(ex$,i,1))
		If b>=97 And b<=122
			output$=output$+Chr$(b-1)
		Else
			output$=output$+Chr$(b)
		EndIf
	Next
	Return output$
End Function

Function SpecialFolderLocation$(folderID)

	str_bank = CreateBank(256)
	If GetSpecialFolder(folderID,str_bank) = 1 Then
		For loop = 0 To 255
			byte = PeekByte(str_bank,loop)
			If byte <> 0
				location$ = location$ + Chr(byte)
			EndIf
		Next
	Else
		location$ = ""
	EndIf

	FreeBank str_bank

	Return location$

End Function

Function ObjectIndexEditorToGameInner(Index)

	Result=Index

	For i=0 To Index-1
		Result=Result+ObjectCountExtraInstantiations(LevelObjects(i)\Attributes)
	Next

	Return Result

End Function

Function ObjectIndexEditorToGame(Index, PlayerIndex)

	If Index=-2
		Return PlayerIndex
	Else
		Return ObjectIndexEditorToGameInner(Index)
	EndIf

End Function

Function ObjectIndexGameToEditorInner(Index)

	Result=Index

	For i=0 To Result-1
		Result=Result-ObjectCountExtraInstantiations(LevelObjects(i)\Attributes)
	Next

	Return Result

End Function

Function ObjectIndexGameToEditor(Index, PlayerIndex)

	Result=ObjectIndexGameToEditorInner(Index)
	If Result=PlayerIndex
		Return -2
	Else
		Return Result
	EndIf

	;If Index=PlayerIndex
	;	Return -2
	;Else
	;	Return ObjectIndexGameToEditorInner(Index)
	;EndIf

End Function

; Reflects the in-game logic for spawning shadows (see CreateObjectShadow in adventures.bb).
Function ObjectHasShadow(ModelName$)

	Select ModelName$

	Case "!StinkerWee","!Scritter","!BabyBoomer","!RainbowBubble"
		Return True
	Case "!Turtle","!Thwart","!Troll"
		Return True
	Case "!Chomper","!Bowler","!Kaboom"
		Return True
	Case "!Crab"
		Return True
	; Q - player NPC functionality
	Case "!NPC","!PlayerNPC" ; Normally this shadow is created by the CreateStinkerModel function rather than CreateObjectShadow.
		Return True
	Default
		Return False
	End Select

End Function

Function ObjectCountAccessories(Attributes.GameObjectAttributes)

	; Q - player NPC functionality
	If IsModelNPC(Attributes\ModelName$)
		Code$="!T"
		If Attributes\Data0<10
			Code$=Code$+"00"+Str$(Attributes\Data0)
		Else If Attributes\Data0<100
			Code$=Code$+"0"+Str$(Attributes\Data0)
		Else
			Code$=Code$+Str$(Attributes\Data0)
		EndIf
		Code$=Code$+Chr$(65+Attributes\Data1)
		If Attributes\Data2>0
			If Attributes\Data2<10
				 Code$=Code$+"00"+Str$(Attributes\Data2)
			Else If Attributes\Data2<100
				 Code$=Code$+"0"+Str$(Attributes\Data2)
			Else
				 Code$=Code$+""+Str$(Attributes\Data2)
			EndIf
			Code$=Code$+Chr$(64+Attributes\Data3)+"0"
		EndIf
		If Attributes\Data4>0 And Attributes\Data2>0 Then Code$=Code$+" "
		If Attributes\Data4>0
			If Attributes\Data4<10
				 Code$=Code$+"00"+Str$(Attributes\Data4)
			Else If Attributes\Data4<100
				 Code$=Code$+"0"+Str$(Attributes\Data4)
			Else
				 Code$=Code$+""+Str$(Attributes\Data4)
			EndIf
			Code$=Code$+Chr$(65+Attributes\Data5)+"0"
		EndIf

		Return CodeCountAccessories(Code$)
	Else
		Return 0
	EndIf

End Function

Function CodeCountAccessories(code$)

	Result=0

	For j=1 To (Len(code$)-5)/6
		Result=Result+1
	Next

	Return Result

End Function

Function ObjectCountExtraInstantiations(Attributes.GameObjectAttributes)

	Result=0
	If ObjectHasShadow(Attributes\ModelName$)
		Result=Result+1
	EndIf
	Result=Result+ObjectCountAccessories(Attributes)
	Return Result

End Function

; Returns False if the current object Type has no default TrueMovement.
Function RetrieveDefaultTrueMovement()

	Select CurrentObject\Attributes\LogicType

	Case 1 ; Player
		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^3+2^4+2^5+2^6+2^8

	Case 50 ; Spellball
		CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^5+2^9+2^10+2^11+2^12+2^13+2^14
		CurrentObject\Attributes\ObjectTypeCollision=0 ; -1 in-game, but probably doesn't make a difference.

	Case 110 ; Stinker NPC
		CurrentObject\Attributes\Data10=-1

		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^6
		;If CurrentObject\Attributes\MoveXGoal=0 And CurrentObject\Attributes\MoveYGoal=0
			;CurrentObject\Attributes\MoveXGoal=Floor(CurrentObject\Position\X)
			;CurrentObject\Attributes\MoveYGoal=Floor(CurrentObject\Position\Y)
			;CurrentObject\Attributes\CurrentAnim=10
		;EndIf

	Case 120 ; Wee Stinker
		CurrentObject\Attributes\MovementType=0
		CurrentObject\Attributes\MovementSpeed=35
		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^6+2^8

		CurrentObject\Attributes\XScale=0.025
		CurrentObject\Attributes\YScale=0.025
		CurrentObject\Attributes\ZScale=0.025

	Case 150 ; Scritter
		CurrentObject\Attributes\MovementSpeed=50
		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^6

	Case 151 ; Rainbow Bubble
		CurrentObject\Attributes\MovementType=33
		CurrentObject\Attributes\MovementSpeed=25
		CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^5+2^9+2^10+2^11+2^12+2^14

	Case 220 ; Dragon Turtle
		CurrentObject\Attributes\MovementType=41+CurrentObject\Attributes\Data0*2+CurrentObject\Attributes\Data1
		CurrentObject\Attributes\MovementSpeed=25
		CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^6

	Case 230 ; Fireflower
		CurrentObject\Attributes\TileTypeCollision=2^0

	Case 250 ; Chomper
		CurrentObject\Attributes\MovementType=13
		CurrentObject\Attributes\MovementSpeed=20+5*CurrentObject\Attributes\Data0

		If CurrentObject\Attributes\LogicSubType=0 ; Non-Water Chomper
			CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		Else ; Water Chomper
			CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		EndIf

		If CurrentObject\Attributes\Data1=1 ; Ghost Chomper
			CurrentObject\Attributes\ObjectTypeCollision=2^1+2^4+2^6
		Else ; Non-Ghost Chomper
			CurrentObject\Attributes\ObjectTypeCollision=2^1+2^3+2^6
		EndIf

	Case 260 ; Spikeyball
		CurrentObject\Attributes\MovementSpeed=25+5*CurrentObject\Attributes\Data2
		CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^5+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^1+2^2+2^3+2^6+2^9

		Data0=CurrentObject\Attributes\Data0 Mod 8
		If CurrentObject\Attributes\Data1=0 Or CurrentObject\Attributes\Data1=1
			; zbot movement
			CurrentObject\Attributes\MovementType=41+Data0*2+CurrentObject\Attributes\Data1
		Else If CurrentObject\Attributes\Data1=2
			; bounce movement
			CurrentObject\Attributes\MovementType=71+Data0
		EndIf

	Case 270 ; Busterfly/Glowworm
		CurrentObject\Attributes\TileTypeCollision=1 ; -1 in-game, but probably doesn't matter.

		If CurrentObject\Attributes\ModelName$="!Busterfly"

			CurrentObject\Attributes\XScale=.01
			CurrentObject\Attributes\YScale=.01
			CurrentObject\Attributes\ZScale=.01
			CurrentObject\Attributes\Roll2=90

		EndIf

	Case 271 ; Zipper
		CurrentObject\Attributes\TileTypeCollision=1 ; -1 in-game, but probably doesn't matter.
		CurrentObject\Attributes\Data1=Rand(0,360)
		CurrentObject\Attributes\Data2=Rand(1,4)

	Case 290 ; Thwart
		CurrentObject\Attributes\Data10=-1

		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^4+2^6
		;If CurrentObject\Attributes\MoveXGoal=0 And CurrentObject\Attributes\MoveYGoal=0
			;CurrentObject\Attributes\MoveXGoal=Floor(CurrentObject\Position\X)
			;CurrentObject\Attributes\MoveYGoal=Floor(CurrentObject\Position\Y)
			;CurrentObject\Attributes\CurrentAnim=10
		;EndIf

	Case 310 ; Rubberducky
		CurrentObject\Attributes\MovementSpeed=4
		CurrentObject\Attributes\TileTypeCollision=2^2 ; -1 in-game, but probably doesn't make a difference.
		CurrentObject\Attributes\Data1=Rand(1,3)
		CurrentObject\Attributes\Data2=Rand(0,360)

	Case 330 ; Wysp
		CurrentObject\Attributes\Data10=-1
		;CurrentObject\Attributes\MoveXGoal=Floor(CurrentObject\Position\X)
		;CurrentObject\Attributes\MoveYGoal=Floor(CurrentObject\Position\Y)

		CurrentObject\Attributes\MovementType=10
		CurrentObject\Attributes\MovementSpeed=45
		CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^6+2^8

	Case 370 ; Crab
		CurrentObject\Attributes\MovementSpeed=40
		CurrentObject\Attributes\ObjectTypeCollision=2^6

		If CurrentObject\Attributes\LogicSubType=0 ; green
			CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
			Select CurrentObject\Attributes\Data1
			Case 0
				; normal
				CurrentObject\Attributes\MovementType=0
			Case 1
				; curious
				CurrentObject\Attributes\MovementType=14
			Case 2,3
				; asleep
				CurrentObject\Attributes\MovementType=0
				;AnimateMD2 ObjectEntity(i),3,1,48,49
			End Select
			CurrentObject\Attributes\XScale=0.006
			CurrentObject\Attributes\YScale=0.006
			CurrentObject\Attributes\ZScale=0.006
		Else ; red
			CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^9+2^10+2^11+2^12+2^14
			Select CurrentObject\Attributes\Data1
			Case 0
				; normal
				CurrentObject\Attributes\MovementType=32
			Case 1
				; curious
				CurrentObject\Attributes\MovementType=14
			Case 2,3
				; asleep
				CurrentObject\Attributes\MovementType=0
				;AnimateMD2 ObjectEntity(i),3,1,48,49
			End Select

		EndIf

	Case 380 ; Ice Troll
		CurrentObject\Attributes\Data10=-1

		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^4+2^6

	Case 390 ; Kaboom! NPC
		CurrentObject\Attributes\Data10=-1

		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^6

	Case 400 ; Baby Boomer
		CurrentObject\Attributes\MovementType=0
		CurrentObject\Attributes\MovementSpeed=35
		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^6+2^8
		CurrentObject\Attributes\LogicSubType=1  ; -2 dying, -1 exiting, 0- asleep, 1-follow, 2-directive, 3-about to fall asleep (still walking), 4 caged when directed 5 caged when follow
		CurrentObject\Attributes\CurrentAnim=3 ; 1-asleep, 2-getting up, 3-idle, 4-wave, 5-tap, 6-walk, 7 sit down, 8-fly, 9-sit on ice

	Case 420 ; Coily
		CurrentObject\Attributes\MovementType=41+2*CurrentObject\Attributes\Data0+CurrentObject\Attributes\Data1
		CurrentObject\Attributes\MovementSpeed=30
		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^9+2^10+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^1+2^3+2^6

	Case 422 ; UFO
		CurrentObject\Attributes\MovementType=41+2*CurrentObject\Attributes\Data0+CurrentObject\Attributes\Data1
		CurrentObject\Attributes\MovementSpeed=20
		CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^5+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^3+2^6

	Case 423 ; Retro Z-Bot
		CurrentObject\Attributes\MovementType=41+2*CurrentObject\Attributes\Data0+CurrentObject\Attributes\Data1
		CurrentObject\Attributes\MovementSpeed=60
		CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^5+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^1+2^3+2^6

	Case 430 ; Zipbot
		CurrentObject\Attributes\MovementType=41+2*CurrentObject\Attributes\Data0+CurrentObject\Attributes\Data1
		CurrentObject\Attributes\MovementSpeed=120
		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^5+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^1+2^3+2^6

	Case 431 ; Zapbot
		CurrentObject\Attributes\MovementType=41+2*CurrentObject\Attributes\Data0+CurrentObject\Attributes\Data1
		CurrentObject\Attributes\MovementSpeed=20*CurrentObject\Attributes\Data2
		CurrentObject\Attributes\TileTypeCollision=2^0+2^2+2^3+2^4+2^5+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^3+2^6

	Case 432 ; Moobot
		CurrentObject\Attributes\MovementType=0
		CurrentObject\Attributes\MovementSpeed=60
		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^6

		CurrentObject\Attributes\ID=500+CurrentObject\Attributes\Data0*5+CurrentObject\Attributes\Data1

	Case 433 ; Z-Bot NPC
		CurrentObject\Attributes\Data10=-1

		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^6

	Case 434 ; Mothership
		CurrentObject\Attributes\MovementSpeed=10
		CurrentObject\Attributes\TileTypeCollision=0
		CurrentObject\Attributes\ObjectTypeCollision=0

		CurrentObject\Attributes\Data1=-1
		CurrentObject\Position\Z=4

	Case 470 ; Ghost
		CurrentObject\Attributes\MovementType=0
		CurrentObject\Attributes\MovementSpeed=5+5*CurrentObject\Attributes\Data1
		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^1+2^3+2^6

	Case 471 ; Wraith
		CurrentObject\Attributes\MovementType=0
		CurrentObject\Attributes\MovementSpeed=20+5*CurrentObject\Attributes\Data0
		CurrentObject\Attributes\TileTypeCollision=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		CurrentObject\Attributes\ObjectTypeCollision=2^1+2^3+2^6

	Default
		Return False
	End Select

	CurrentObjectWasChanged()
	BuildCurrentObjectModel()
	Return True

End Function

; Checks if the level is dark enough to use a GlowGem/Lamp.
Function TooDark()
	If SimulatedLightRedgoal+SimulatedLightBluegoal+SimulatedLightGreengoal+SimulatedAmbientRedgoal+SimulatedAmbientBluegoal+SimulatedAmbientGreengoal<500
		Return True
	EndIf
	Return False
End Function

Function TurnObjectTowardDirection(i,dx#,dy#,speed,Adjust)

	; Turns Object by speed degrees toward the angle made by dx/dy.
	; Adjust is a fixed angle added to goal (in case the base models orientation is off, for example)

	If dx<>0 Or dy<>0

		ObjectYawGoal=ATan2(-dy,dx)+90+Adjust
		While ObjectYawGoal>180
			ObjectYawGoal=ObjectYawGoal-360
		Wend
		While ObjectYawGoal<=-180
			ObjectYawGoal=ObjectYawGoal+360
		Wend

		If Abs(ObjectYawGoal-SimulatedObjectYaw(i))>speed
			dyaw=speed
		Else
			dyaw=1
		EndIf

		If SimulatedObjectYaw(i)>ObjectYawGoal
			If SimulatedObjectYaw(i)-ObjectYawGoal<180
				SimulatedObjectYaw(i)=SimulatedObjectYaw(i)-dyaw
			Else
				SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+dyaw
			EndIf
		Else If SimulatedObjectYaw(i)<ObjectYawGoal
			If ObjectYawGoal-SimulatedObjectYaw(i)<180
				SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+dyaw
			Else
				SimulatedObjectYaw(i)=SimulatedObjectYaw(i)-dyaw
			EndIf
		EndIf
		If SimulatedObjectYaw(i)>180 Then SimulatedObjectYaw(i)=SimulatedObjectYaw(i)-360
		If SimulatedObjectYaw(i)<-180 Then SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+360

	EndIf

End Function

Function SetLight(red,green,blue,speed,ared,agreen,ablue,aspeed)
	SimulatedLightRedGoal=Red
	SimulatedLightGreenGoal=Green
	SimulatedLightBlueGoal=Blue
	SimulatedLightChangeSpeed=speed

	SimulatedAmbientRedGoal=aRed
	SimulatedAmbientGreenGoal=aGreen
	SimulatedAmbientBlueGoal=aBlue
	SimulatedAmbientChangeSpeed=aSpeed
End Function

Function SetLightNow(red,green,blue,ared,agreen,ablue)
	SimulatedLightRed=Red
	SimulatedLightGreen=Green
	SimulatedLightBlue=Blue
	SimulatedLightRedGoal=Red
	SimulatedLightGreenGoal=Green
	SimulatedLightBlueGoal=Blue

	SimulatedAmbientRed=aRed
	SimulatedAmbientBlue=aBlue
	SimulatedAmbientGreen=aGreen
	SimulatedAmbientRedGoal=aRed
	SimulatedAmbientGreenGoal=aGreen
	SimulatedAmbientBlueGoal=aBlue
End Function

Function StartMusic()

	LevelMusicCustomVolume=100

	If levelmusic<>CurrentMusic And GlobalMusicVolume2>0
		StopMusic()

		If levelmusic>0
			If levelmusic=21
				MusicChannel=PlayMusic ("data\models\ladder\valetfile.ogg")
			Else
				MusicChannel=PlayMusic ("data\music\"+levelmusic+".ogg")
			EndIf
		EndIf

		CurrentMusic=levelmusic
	EndIf

	ChannelVolume musicchannel,GlobalMusicVolume*Float(LevelMusicCustomVolume)/100.0

	LevelMusicCustomPitch=44
	If LevelMusic=12 Then LevelMusicCustomPitch=22
	ChannelPitch MusicChannel,LevelMusicCustomPitch*1000

End Function

Function StopMusic()

	If ChannelPlaying (MusicChannel)=1
		StopChannel (MusicChannel)
	EndIf

End Function

Function UpdateMusic()

	If SimulationLevel>=SimulationLevelMusic
		StartMusic()
	Else
		StopMusic()
		CurrentMusic=0
	EndIf

End Function

Function LoopMusic()

	If currentmusic>0 ;And (gamemode<10 Or currentmenu<>10)
		; music looping
		If ChannelPlaying(musicchannel)=0

			If currentmusic=21
				MusicChannel=PlayMusic("data\models\ladder\valetfile.ogg")

			Else
				MusicChannel=PlayMusic("Data\music\"+currentmusic+".ogg")
			EndIf
			;ChannelVolume musicchannel,GlobalMusicVolume
			ChannelVolume MusicChannel,GlobalMusicVolume * Float(LevelMusicCustomVolume)/100.0
			ChannelPitch MusicChannel,LevelMusicCustomPitch*1000

		EndIf
	EndIf

End Function

Function ControlSoundscapes()

	If currentmusic=-1 ; beach
		If leveltimer Mod 250 = 0
			sfxa=126+Rand(0,1)
			SoundPitch SoundFX(sfxa),Rand(10000,12000)
			playSoundfx(sfxa,-1,-1)
		EndIf
		If Rand(0,300)=0
			sfxa=128+Rand(0,1)
			If sfxa=128
				If Rand(0,10)=4
					SoundPitch SoundFX(sfxa),Rand(10000,12000)
				Else
					SoundPitch SoundFX(sfxa),Rand(19000,22000)
				EndIf
			Else
				SoundPitch SoundFX(sfxa),Rand(10000,12000)
			EndIf
			playSoundfx(sfxa,-1,-1)
		EndIf
	EndIf

End Function

Function ControlLight()

	If SimulationLevel>=3

		If SimulatedLightRed>SimulatedLightRedGoal
			SimulatedLightRed=SimulatedLightRed-SimulatedLightChangeSpeed
			If SimulatedLightRed<SimulatedLightRedGoal Then SimulatedLightRed=SimulatedLightRedGoal
		Else If SimulatedLightRed<SimulatedLightRedGoal
			SimulatedLightRed=SimulatedLightRed+SimulatedLightChangeSpeed
			If SimulatedLightRed>SimulatedLightRedGoal Then SimulatedLightRed=SimulatedLightRedGoal
		EndIf
		If SimulatedLightGreen>SimulatedLightGreenGoal
			SimulatedLightGreen=SimulatedLightGreen-SimulatedLightChangeSpeed
			If SimulatedLightGreen<SimulatedLightGreenGoal Then SimulatedLightGreen=SimulatedLightGreenGoal
		Else If SimulatedLightGreen<SimulatedLightGreenGoal
			SimulatedLightGreen=SimulatedLightGreen+SimulatedLightChangeSpeed
			If SimulatedLightGreen>SimulatedLightGreenGoal Then SimulatedLightGreen=SimulatedLightGreenGoal
		EndIf
		If SimulatedLightBlue>SimulatedLightBlueGoal
			SimulatedLightBlue=SimulatedLightBlue-SimulatedLightChangeSpeed
			If SimulatedLightBlue<SimulatedLightBlueGoal Then SimulatedLightBlue=SimulatedLightBlueGoal
		Else If SimulatedLightBlue<SimulatedLightBlueGoal
			SimulatedLightBlue=SimulatedLightBlue+SimulatedLightChangeSpeed
			If SimulatedLightBlue>SimulatedLightBlueGoal Then SimulatedLightBlue=SimulatedLightBlueGoal
		EndIf

		If SimulatedAmbientRed>SimulatedAmbientRedGoal

			SimulatedAmbientRed=SimulatedAmbientRed-SimulatedAmbientChangeSpeed
			If SimulatedAmbientRed<SimulatedAmbientRedGoal Then SimulatedAmbientRed=SimulatedAmbientRedGoal
		Else If SimulatedAmbientRed<SimulatedAmbientRedGoal

			SimulatedAmbientRed=SimulatedAmbientRed+SimulatedAmbientChangeSpeed
			If SimulatedAmbientRed>SimulatedAmbientRedGoal Then SimulatedAmbientRed=SimulatedAmbientRedGoal
		EndIf
		If SimulatedAmbientGreen>SimulatedAmbientGreenGoal
			SimulatedAmbientGreen=SimulatedAmbientGreen-SimulatedAmbientChangeSpeed
			If SimulatedAmbientGreen<SimulatedAmbientGreenGoal Then SimulatedAmbientGreen=SimulatedAmbientGreenGoal
		Else If SimulatedAmbientGreen<SimulatedAmbientGreenGoal
			SimulatedAmbientGreen=SimulatedAmbientGreen+SimulatedAmbientChangeSpeed
			If SimulatedAmbientGreen>SimulatedAmbientGreenGoal Then SimulatedAmbientGreen=SimulatedAmbientGreenGoal
		EndIf
		If SimulatedAmbientBlue>SimulatedAmbientBlueGoal
			SimulatedAmbientBlue=SimulatedAmbientBlue-SimulatedAmbientChangeSpeed
			If SimulatedAmbientBlue<SimulatedAmbientBlueGoal Then SimulatedAmbientBlue=SimulatedAmbientBlueGoal
		Else If SimulatedAmbientBlue<SimulatedAmbientBlueGoal
			SimulatedAmbientBlue=SimulatedAmbientBlue+SimulatedAmbientChangeSpeed
			If SimulatedAmbientBlue>SimulatedAmbientBlueGoal Then SimulatedAmbientBlue=SimulatedAmbientBlueGoal
		EndIf

		LightColor Light,SimulatedLightRed,SimulatedLightGreen,SimulatedLightBlue
		AmbientLight SimulatedAmbientRed,SimulatedAmbientGreen,SimulatedAmbientBlue
		RotateEntity Light,35,-35,0

	Else

		LightColor Light,255,255,255
		AmbientLight 155,155,155
		RotateEntity Light,80,15,0

	EndIf

	If SimulationLevel>=4 And TooDark()
		LightColor SpotLight,10,4,0
		PositionEntity SpotLight,BrushCursorX+0.5,5,-BrushCursorY-3.5
	Else
		LightColor SpotLight,0,0,0
		;PositionEntity SpotLight,-10000,-10000,-10000
	EndIf

End Function

Function ControlWeather()

	CentreX#=EntityX(Camera1)
	CentreY#=EntityZ(Camera1)

	Select levelweather
	Case 1
		; light snow
		If Rand(1,3)=3 	AddParticle(40,CentreX+Rnd(-10,10),8,CentreY+Rnd(-10,10),0,.2,Rnd(-.01,0.01),-.03,Rnd(-.01,0.01),2,0,0,0,0,200,3)

	Case 2
		; heavy snow
		AddParticle(40,CentreX+Rnd(-10,10),8,CentreY+Rnd(-10,10),0,.3,0,-.05,0,2,0,0,0,0,120,3)
	Case 3
		; very heavy snow right to left
		AddParticle(40,CentreX+Rnd(5,10),5,CentreY+Rnd(-8,4),0,.4,-0.3,-.09,0,2,0,0,0,0,80,3)
	Case 4
		; very heavy snow left to right
		AddParticle(40,CentreX+Rnd(-10,-5),5,CentreY+Rnd(-8,4),0,.4,0.3,-.09,0,2,0,0,0,0,80,3)

	Case 5
		; rain
		AddParticle(41,CentreX+Rnd(-10,10),8,CentreY+Rnd(-10,10),0,.2,0,-.2,0,0,0,0,0,0,60,2)

		; leaves
	;	If Rand(1,3)=1 AddParticle(42,Objectx(CameraFocusObject)+Rnd(-10,10),5,-ObjectY(CameraFocusObject)+Rnd(-10,10),0,1,Rnd(0,.2),Rnd(-.1,0),0,Rand(1,5),0,0,0,0,60,3)

	Case 6
		If leveltimer<1000000000
			; void
			If Rand(0,200)<2
				SetLight(Rand(0,255),Rand(0,255),Rand(0,255),2,Rand(0,255),Rand(0,255),Rand(0,255),2)
			EndIf
		EndIf
	Case 7
		If leveltimer<1000000000

			;lightning storm
			If lightningstorm<100 Then lightningstorm=100
			AddParticle(41,CentreX+Rnd(-10,10),8,CentreY+Rnd(-10,10),0,.2,0.05,-.2,0,0,0,0,0,0,60,2)
			AddParticle(41,CentreX+Rnd(-10,10),8,CentreY+Rnd(-10,10),0,.2,0.05,-.2,0,0,0,0,0,0,60,2)
			If lightningstorm>100
				If (lightningstorm-100)<7 lightningstorm=Rand(180,255)
				lightningstorm=lightningstorm-10
				If lightningstorm<100 Then lightningstorm=100
			Else
				If Rand(0,300)=10
					lightningstorm=Rand(180,255)
					;playsoundfx(Rand(155,157),-1,-1)
				EndIf
			EndIf

			SetLight(lightningstorm,lightningstorm,lightningstorm,10,70,70,70,10)
		EndIf
	Case 8
		If leveltimer<1000000000
			; red alert

			alarm=leveltimer Mod 240
			;If alarm=1 Then playsoundfxnow(98)
			If alarm <120

				SetLight(alarm*2,0,0,10,70,20,20,10)
			Else
				SetLight(240-alarm*2,0,0,10,70,20,20,10)
			EndIf
		EndIf

	Case 9

		; light rising
		If Rand(1,8)=3 	AddParticle(1,CentreX+Rnd(-10,10),0,CentreY+Rnd(-10,10),0,.2,0,+.03,0,2,0,0,0,0,200,3)

	Case 10

		; light falling
		If Rand(1,8)=3 	AddParticle(1,CentreX+Rnd(-10,10),8,CentreY+Rnd(-10,10),0,.2,0,-.03,0,2,0,0,0,0,200,3)

	Case 11

		; stars rising
		If Rand(1,5)=3 	AddParticle(Rand(32,38),CentreX+Rnd(-10,10),0,CentreY+Rnd(-10,10),0,.8,0,+.03,0,2,0,0,0,0,200,3)

	Case 12

		; stars rising
		If Rand(1,5)=3 	AddParticle(Rand(32,38),CentreX+Rnd(-10,10),8,CentreY+Rnd(-10,10),0,.8,0,-.03,0,2,0,0,0,0,200,3)

	Case 13

		; foggy
		If Rand(1,3)=3 	AddParticle(0,CentreX+Rnd(-10,10),-.8,CentreY+Rnd(-10,10),0,2,0,+.005,0,2,0,0,0,0,500,2)

	Case 14

		; green foggy
		If Rand(1,3)=3 	AddParticle(27,CentreX+Rnd(-10,10),-.8,CentreY+Rnd(-10,10),0,2,0,+.005,0,2,0,0,0,0,500,2)

	Case 15
		; leaves
		If Rand(1,8)=3 	AddParticle(42,CentreX+Rnd(-10,10),8,CentreY+Rnd(-10,10),0,1,Rnd(-.03,0.01),-.03,Rnd(-.03,0.01),2,0,0,0,0,200,3)

	Case 16
		; sandstorm
		AddParticle(Rand(25,26),CentreX+Rnd(5,10),5,CentreY+Rnd(-8,4),0,.2,-0.3,-.09,0,2,0,0,0,0,80,3)
		AddParticle(Rand(24,26),CentreX+Rnd(5,10),5,CentreY+Rnd(-8,4),0,.1,-0.3,-.09,0,2,0,0,0,0,80,3)

	Case 17
		; abstract
		If Rand(1,40)=3 	AddParticle(Rand(43,45),CentreX+Rnd(-10,10),Rnd(1,2),CentreY+Rnd(-10,10),0,Rnd(1,4),0,Rnd(.001,0.01),0,2,0,0,0,0,200,Rand(2,3))

	End Select

End Function

Function ControlObjects()

	For i=0 To NofObjects-1

		Obj.GameObject=LevelObjects(i)
		Attributes.GameObjectAttributes=Obj\Attributes
		Pos.GameObjectPosition=Obj\Position

		If Attributes\Reactive=True

			; Get Scale
			ObjXScale#=SimulatedObjectXScale(i)*SimulatedObjectScaleXAdjust(i)
			ObjYScale#=SimulatedObjectYScale(i)*SimulatedObjectScaleYAdjust(i)
			ObjZScale#=SimulatedObjectZScale(i)*SimulatedObjectScaleZAdjust(i)

			;If (SimulatedObjectActive(i)<>0 And SimulatedObjectActive(i)<>1001) Or SimulationLevel>=2
			If SimulatedObjectActive(i)<>0 Or SimulationLevel>=2

				; Select Visual Animation
				Select Attributes\ActivationType
				Case 0
					; nothing
				Case 1
					; Scale Vertical 0-1
					ObjZScale=ObjZScale*Float(SimulatedObjectActive(i))/1001.0

				Case 2
					; scale all directions 0-1
					ObjXScale=ObjXScale*Float(SimulatedObjectActive(i))/1001.0
					ObjYScale=ObjYScale*Float(SimulatedObjectActive(i))/1001.0
					ObjZScale=ObjZScale*Float(SimulatedObjectActive(i))/1001.0
				Case 3
					; scale planar only
					ObjXScale=ObjXScale*Float(SimulatedObjectActive(i))/1001.0
					ObjYScale=ObjYScale*Float(SimulatedObjectActive(i))/1001.0

				Case 11
					; push up from -1.01 to -0.01
					SimulatedObjectZ#(i)=-0.99+Float(SimulatedObjectActive(i))/1001.0

				Case 12,13,14,15,16
					; push up from -x.01 to -5.01 (used for stepping stones)
					SimulatedObjectZ#(i)=-(Attributes\ActivationType-6)-.01+(Attributes\ActivationType-11)*Float(SimulatedObjectActive(i))/1001.0

				Case 17 ; *** THESE ONLY WORK FOR AUTODOORS - OBJECTTILEX MUST BE PRE_SET
					; push north
					SimulatedObjectY#(i)=Pos\TileY+0.5-SimulatedObjectYScale(i)*(0.99-Float(SimulatedObjectActive(i))/1001.0)
				Case 18
					; push East
					SimulatedObjectX#(i)=Pos\TileX+0.5+SimulatedObjectXScale(i)*(0.99-Float(SimulatedObjectActive(i))/1001.0)

				Case 19
					; push south
					SimulatedObjectY#(i)=Pos\TileY+0.5+SimulatedObjectYScale(i)*(0.99-Float(SimulatedObjectActive(i))/1001.0)

				Case 20
					; push west
					SimulatedObjectX#(i)=Pos\TileX+0.5-SimulatedObjectXScale(i)*(0.99-Float(SimulatedObjectActive(i))/1001.0)

				Case 21
					; Q - player NPC functionality
					If IsModelNPC(Attributes\ModelName$) Or Attributes\ModelName$="!Tentacle"
						Entity=GetChild(Obj\Model\Entity,3)
					;ElseIf Attributes\ModelName$="!FlipBridge"
					;	Entity=GetChild(Obj\Model\Entity,1)
					Else
						Entity=Obj\Model\Entity
					EndIf
					; Fade in
					EntityAlpha Entity,Float(SimulatedObjectActive(i))/1001.0

				Case 31
					; push down from 1.01 to 0.01
					SimulatedObjectZ#(i)=1.01-Float(SimulatedObjectActive(i))/1001.0

				Case 41
					; rotate out (doors)
					SimulatedObjectYaw#(i)=-160+160*Float(SimulatedObjectActive(i))/1001.0

				End Select

			EndIf

			Select Attributes\LogicType

			Case 20
				ControlTrap(i)
			Case 30
				ControlTeleporter(i)
			Case 40
				ControlSteppingStone(i)
			Case 45
				ControlConveyorLead(i)
			Case 46
				ControlConveyorTail(i)
			Case 50
				ControlSpellBall(i)
			Case 53
				ControlMeteorite(i)
			Case 54
				ControlMirror(i)
			Case 70
				ControlPickUpItem(i)
			Case 71
				ControlUsedItem(i)
			Case 110
				ControlNPC(i)
			Case 120
				ControlStinkerWee(i)
			Case 130
				ControlStinkerWeeExit(i)
			Case 150
				ControlScritter(i)
			Case 151
				ControlRainbowBubble(i)
			Case 160
				ControlObstacle(i)
			Case 161
				ControlWaterfall(i)
			Case 163
				ControlWindmillRotor(i)
			Case 164
				ControlFountain(i)
			Case 170
				ControlGoldStar(i)
			Case 171,174
				ControlGoldCoin(i)
			Case 172
				ControlKey(i)
			Case 173
				ControlGem(i)
			Case 179
				ControlCustomItem(i)
			Case 180
				ControlSigns(i)
			Case 190
				ControlParticleEmitters(i)
			Case 200
				ControlGloveCharge(i)
			Case 230
				ControlFireFlower(i)
			Case 242
				ControlCuboid(i)
			Case 250
				ControlChomper(i)
			Case 260
				ControlBowler(i)
			Case 270
				ControlButterfly(i)
			Case 271
				ControlZipper(i)
			Case 280
				ControlSpring(i)
			Case 281
				ControlSucTube(i)
			Case 290
				ControlThwart(i)
			Case 300
				ControlIceFloat(i)
			Case 301
				ControlPlantFloat(i)
			Case 310
				ControlRubberducky(i)
			Case 320
				ControlVoid(i)
			Case 330
				ControlWisp(i)
			Case 340
				ControlTentacle(i)
			Case 370
				ControlCrab(i)
			Case 380
				ControlTroll(i)
			Case 390
				ControlKaboom(i)
			Case 400
				ControlBabyBoomer(i)
			Case 410
				ControlFlipBridge(i)
			Case 420
				ControlRetroCoily(i)
			Case 422,423,430,431
				ControlRetroZbotUfo(i)
			Case 424
				ControlRetroLaserGate(i)
			Case 425
				ControlRetroRainbowCoin(i)
			Case 433
				ControlZbotNPC(i)
			Case 434
				ControlMothership(i)
			Case 441
				ControlSunSphere1(i)
			Case 442
				ControlSunSphere2(i)
			Case 450
				ControlLurker(i)
			Case 460
				ControlBurstFlower(i)
			Case 470
				ControlGhost(i)
			Case 471
				ControlWraith(i)

			End Select

			SimulateObjectPosition(i)
			SimulateObjectRotation(i)
			ScaleEntity Obj\Model\Entity,ObjXScale#,ObjZScale#,ObjYScale#

			SimulatedObjectLastActive(i)=SimulatedObjectActive(i)

		;Else
		;	AddParticle(2,ObjectXAdjust(i)+Pos\TileX+.5,ObjectZAdjust(i),-ObjectYAdjust(i)-Pos\TileY-.5,0,.2,0,.03,0,0,.01,0,0,0,100,3)

			If Obj\Model\HatEntity>0
				TransformAccessoryEntityOntoBone(Obj\Model\HatEntity,Obj\Model\Entity)
			EndIf
			If Obj\Model\AccEntity>0
				TransformAccessoryEntityOntoBone(Obj\Model\AccEntity,Obj\Model\Entity)
			EndIf

		EndIf

	Next

	; Scroll Teleporters
	For i=0 To 9
		If OldTeleporterTexture(i)>0
			PositionTexture OldTeleporterTexture(i),0,-Float((LevelTimer/3) Mod 100)/100.0
		EndIf
	Next

	PositionTexture StarTexture,0,Float(leveltimer Mod 1000) / 1000.0
	PositionTexture RainbowTexture,0,Float(leveltimer Mod 1000) / 1000.0
	PositionTexture GhostTexture,0,Float(leveltimer Mod 1000) / 1000.0
	For i=0 To 2
		PositionTexture WraithTexture(i),Float(leveltimer Mod 100) / 100.0,0
	Next

	For i=0 To 2
		PositionTexture WaterFallTexture(i),0,((LevelTimer) Mod 50)/50.0
	Next
	PositionTexture FloingTexture,(leveltimer Mod 700)/700.0,(leveltimer Mod 100)/100.0
	PositionTexture PlasmaTexture,3*Sin((LevelTimer/20.0) Mod 360),4*Cos((LevelTimer/20.0) Mod 360)
	ScaleTexture Plasmatexture,1.1+Sin((LevelTimer/2) Mod 360),1.1+Sin((LevelTimer/2) Mod 360)
	PositionTexture RainbowTexture2,(leveltimer Mod 7000)/7000.0,(leveltimer Mod 1000)/1000.0

End Function

;Include "editor/editor-adventureselect.bb"
;Include "editor/editor-loadlevel.bb"
Include "sound.bb"
Include "customcontent.bb"

; OpenWA Includes
Include "shared/includes.bb"
Include "editor/editor-objadjust-display.bb"
Include "editor/editor-objadjust-control.bb"
Include "editor/editor-LoadLevel.bb"
Include "editor/editor-adventureselect.bb"
Include "editor/editor-menuloops.bb"
Include "editor/editor-objectcontrol.bb"
Include "editor/editor-dialog.bb"
Include "editor/editor-getnames.bb"
Include "editor/editor-meshcontrol.bb"
Include "editor/editor-objectmodels.bb"
Include "editor/editor-typeconvert.bb"
Include "editor/editor-cutscenes.bb"
Include "shared/clipboard.bb"