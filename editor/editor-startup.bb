; ==============================
; editor-startup.bb
; ------------------
; Globals, Defines, preloads etc.
; ==============================

Global VersionText$

If OpenSource
	VersionText$=LSet$("WA Editor",32-Len(GameVersion$))+"OpenSource v"+GameVersion$
Else
	VersionText$=LSet$("Wonderland Adventures Editor",43-Len(GameVersion$))+"v"+GameVersion$
EndIf

VersionText$="WA Editor       MNIKSource v10.04 ("+VersionDate$+")" ; set this for now

Global MASTERUSER=True

SeedRnd MilliSecs() ; Seed the randomizer with the current system time in milliseconds.

Global LeftMouse,LeftMouseReleased,RightMouse,RightMouseReleased
Global MouseScroll=0
Global MouseDebounceTimer=0
Global ReturnKey,ReturnKeyReleased,DeleteKey,DeleteKeyReleased
Const KeyCount=237 ; How many keys to track for their state.
Dim KeyReleased(KeyCount)

Global EditorMode=0		;0-level, 1-textures, 2-sidetextures, 3-objects
						;4-user Select screen
						;5,6,7-adventure select screen (6="edit/delete/move/cancel",7="delete sure?")
						;8-master edit screen
						;9-dialog edit screen

Const EditorModeTile=0
Const EditorModeTextureTop=1
Const EditorModeTextureSide=2
Const EditorModeObject=3
Const EditorModeDialog=9

Global EditorModeBeforeMasterEdit=0

; Whether or not the level or dialog has unsaved changes.
Global UnsavedChanges=0

Function AddUnsavedChange()

	If UnsavedChanges<999999
		UnsavedChanges=UnsavedChanges+1
	EndIf

End Function

Const DialogKillLineEight=False

; KEYS

Global KeyMoveNorth=17 ; W key
Global KeyMoveWest=30 ; A key
Global KeyMoveSouth=31 ; S key
Global KeyMoveEast=32 ; D key

; COLORS

Global RectOnR=255
Global RectOnG=0
Global RectOnB=0
Global RectOffR=100
Global RectOffG=0
Global RectOffB=0
Global RectGlobalsR=50
Global RectGlobalsG=0
Global RectGlobalsB=0
Global RectMarginR=0
Global RectMarginG=0
Global RectMarginB=0
Global RectToolbarR=0
Global RectToolbarG=0
Global RectToolbarB=0
Global TextLevelR=255
Global TextLevelG=255
Global TextLevelB=255
Global TextAdjusterR=255
Global TextAdjusterG=255
Global TextAdjusterB=255
Global TextAdjusterHighlightedR=255
Global TextAdjusterHighlightedG=255
Global TextAdjusterHighlightedB=0
Global TextMenusR=255
Global TextMenusG=255
Global TextMenusB=0
Global TextMenuButtonR=155
Global TextMenuButtonG=155
Global TextMenuButtonB=0
Global TextMenuXR=100
Global TextMenuXG=100
Global TextMenuXB=0
Global TextWarningR=255
Global TextWarningG=100
Global TextWarningB=100

Global ModelErrorR=255
Global ModelErrorG=0
Global ModelErrorB=255

Const ColorsConfig$="colors.cfg"
Const TexturePrefixesConfig$="texture_prefixes.cfg"

; Set at runtime
Global ObjectColorR
Global ObjectColorG
Global ObjectColorB
Global TileColorR
Global TileColorG
Global TileColorB

Const MaxTexturePrefix=99
Dim TexturePrefix$(MaxTexturePrefix)
Global CurrentTexturePrefix=-1

; EDITOR DIALOG DATA

Global CurrentDialog
Global PreviewCurrentDialog

Global DialogCurrentRed,DialogCurrentGreen,DialogCurrentBlue,DialogCurrentEffect

Const MaxInterChanges=100
Const MaxAskAbouts=100
Const MaxReply=8
Const MaxInterChangeTextLine=7

Global StartingInterChange

Global NofInterchanges
Dim NofInterChangeTextLines(MaxInterChanges)
Dim InterChangeTextLine$(MaxInterChanges,MaxInterChangeTextLine)
Dim DialogTextCommand$(MaxInterChanges,200),DialogTextCommandPos(MaxInterChanges,200), NofTextCommands(MaxInterChanges)
Dim NofInterChangeReplies(MaxInterChanges)
Dim InterChangeReplyText$(MaxInterChanges,MaxReply)
Dim InterChangeReplyFunction(MaxInterChanges,MaxReply)
Dim InterChangeReplyData(MaxInterChanges,MaxReply)
Dim InterChangeReplyCommand(MaxInterChanges,MaxReply)
Dim InterChangeReplyCommandData(MaxInterChanges,MaxReply,4)

Global NofAskAbouts
Global AskAboutTopText$
Dim AskAboutText$(MaxAskAbouts)
Dim AskAboutActive(MaxAskAbouts)
Dim AskAboutInterchange(MaxAskAbouts)
Dim AskAboutRepeat(MaxAskAbouts)

Global PreviewNofInterchanges
Dim PreviewNofInterChangeTextLines(MaxInterChanges)
Dim PreviewInterChangeTextLine$(MaxInterChanges,MaxInterChangeTextLine)
Dim PreviewDialogTextCommand$(MaxInterChanges,200),PreviewDialogTextCommandPos(MaxInterChanges,200), PreviewNofTextCommands(MaxInterChanges)
Dim PreviewNofInterChangeReplies(MaxInterChanges)
Dim PreviewInterChangeReplyText$(MaxInterChanges,MaxReply)
Dim PreviewInterChangeReplyFunction(MaxInterChanges,MaxReply)
Dim PreviewInterChangeReplyData(MaxInterChanges,MaxReply)
Dim PreviewInterChangeReplyCommand(MaxInterChanges,MaxReply)
Dim PreviewInterChangeReplyCommandData(MaxInterChanges,MaxReply,4)

Global PreviewNofAskAbouts
Global PreviewAskAboutTopText$
Dim PreviewAskAboutText$(MaxAskAbouts)
Dim PreviewAskAboutActive(MaxAskAbouts)
Dim PreviewAskAboutInterchange(MaxAskAbouts)
Dim PreviewAskAboutRepeat(MaxAskAbouts)

Global CopiedNofInterChangeTextLines
Dim CopiedInterChangeTextLine$(MaxInterChangeTextLine)
Dim CopiedDialogTextCommand$(200),CopiedDialogTextCommandPos(200)
Global CopiedNofTextCommands
Global CopiedNofInterChangeReplies
Dim CopiedInterChangeReplyText$(MaxReply)
Dim CopiedInterChangeReplyFunction(MaxReply)
Dim CopiedInterChangeReplyData(MaxReply)
Dim CopiedInterChangeReplyCommand(MaxReply)
Dim CopiedInterChangeReplyCommandData(MaxReply,4)

Global ColEffect=-1
Global TxtEffect=-1
Dim CCommands$(20),TCommands$(20)

;Restore CommandNames
;For i=0 To 11
;	Read CCommands$(i)
;Next
;For i=0 To 11
;	Read TCommands$(i)
;Next

CCommands$(0)="CWHI"
CCommands$(1)="CGRY"
CCommands$(2)="CRED"
CCommands$(3)="CORA"
CCommands$(4)="CYEL"
CCommands$(5)="CGRE"
CCommands$(6)="CCYA"
CCommands$(7)="CBLU"
CCommands$(8)="CPUR"
CCommands$(9)="CRAI"
CCommands$(10)="CBLI"
CCommands$(11)="CWAR"
TCommands$(0)="ENON"
TCommands$(1)="ESHI"
TCommands$(2)="EJIT"
TCommands$(3)="EWAV"
TCommands$(4)="EBOU"
TCommands$(5)="EZOO"
TCommands$(6)="EZSH"
TCommands$(7)="ECIR"
TCommands$(8)="EEIG"
TCommands$(9)="EUPD"
TCommands$(10)="ELER"
TCommands$(11)="EROT"

;.CommandNames
;Data "CWHI","CGRY","CRED","CORA","CYEL","CGRE","CCYA","CBLU","CPUR","CRAI","CBLI","CWAR"
;Data "ENON","ESHI","EJIT","EWAV","EBOU","EZOO","EZSH","ECIR","EEIG","EUPD","ELER","EROT"

Const CharactersPerLine=38
Const CharacterDeleteDelay=50

Global WhichInterChange=0
Global WhichAnswer=0
Global WhichAskAbout=0

; 0 is master.dat editor, 1 is dialog editor, 2 is hub editor
Dim MouseTextEntryLineMax(2)
MouseTextEntryLineMax(0)=5
MouseTextEntryLineMax(1)=10
MouseTextEntryLineMax(2)=1

Dim MouseTextEntryLineY(2,10)
Dim MouseTextEntryLineYAdjust(2,10)

MouseTextEntryLineY(0,0)=0
MouseTextEntryLineY(0,1)=3
MouseTextEntryLineY(0,2)=4
MouseTextEntryLineY(0,3)=5
MouseTextEntryLineY(0,4)=6
MouseTextEntryLineY(0,5)=7

MouseTextEntryLineY(1,0)=0
MouseTextEntryLineY(1,1)=1
MouseTextEntryLineY(1,2)=2
MouseTextEntryLineY(1,3)=3
MouseTextEntryLineY(1,4)=4
MouseTextEntryLineY(1,5)=5
MouseTextEntryLineY(1,6)=6
MouseTextEntryLineY(1,7)=7
MouseTextEntryLineY(1,8)=10
MouseTextEntryLineY(1,9)=19
MouseTextEntryLineY(1,10)=24
MouseTextEntryLineYAdjust(1,9)=-8
MouseTextEntryLineYAdjust(1,10)=-8

MouseTextEntryLineY(2,0)=0
MouseTextEntryLineY(2,1)=3

Global MouseTextEntryDelete=False

; COMPILER DATA

Const MaxCompilerFile=700 ; 500

Global NofCompilerFiles
Global NofCustomContentFiles
Dim CustomContentFile$(MaxCompilerFile)

Dim CompilerFileName$(MaxCompilerFile)
Dim CompilerFileSize(MaxCompilerFile)

Dim NofHubCompilerFiles(MaxCompilerFile)
Dim HubCompilerFileName$(MaxCompilerFile,MaxCompilerFile)
Dim HubCompilerFileSize(MaxCompilerFile,MaxCompilerFile)

Global NofWlvFiles
Global NofDiaFiles
Global NofAdventures

Function FileSatisfiesCompiler(ex$,AddToCounts,IsHub)

	If Upper$(ex$)="MASTER.DAT"
		If AddToCounts And IsHub=False
			NofAdventures=NofAdventures+1
		EndIf
		Return True
	ElseIf Upper$(Right$(ex$,4))=".WLV"
		If AddToCounts
			NofWlvFiles=NofWlvFiles+1
		EndIf
		Return True
	ElseIf Upper$(Right$(ex$,4))=".DIA"
		If AddToCounts
			NofDiaFiles=NofDiaFiles+1
		EndIf
		Return True
	Else
		Return False
	EndIf

End Function

; EDITOR MASTER DATA

Global Dialogtimer

Global Oldx,Oldy,OldMouseX,OldMouseY

Global AdventureStartX,AdventureStartY,AdventureStartDir ; x/y position of player start

Global GateKeyVersion=1

Global AdventureTitle$
Dim AdventureTextLine$(5)

Global MasterDialogListStart=0
Global MasterLevelListStart=0
Const MaxDialog=999
Const MaxLevel=999
Const MaxInterchange=100
Dim MasterDialogList(1000),MasterLevelList(1000)

Const StateNotSpecial=0
Const StateCopying=1
Const StateSwapping=2
Global CopyingLevel=StateNotSpecial
Global CopiedLevel=-1
Global CopyingDialog=StateNotSpecial
Global CopiedDialog=-1

Global AdventureExitWonLevel, AdventureExitWonX, AdventureExitWonY ; at what hub level and x/y do you reappear if won.
Global AdventureExitLostLevel, AdventureExitLostX, AdventureExitLostY ; at what hub level and x/y do you reappear if won.

Global AdventureGoal	; when is adventure done
						; 1-NofWeeStinkersInAdventure=0

Dim AdventureWonCommand(3,6)	; 3 commands, each with level/command/fourdata

Global StarterItems
Global WidescreenRange=0
Global WidescreenRangeLevel=-1

Dim winningcondition$(20)
Global nofwinningconditions=10

;Restore winning
;Repeat
;	Read winningcondition$(nofwinningconditions)
;	nofwinningconditions=nofwinningconditions+1
;Until winningcondition$(nofwinningconditions-1)="Done"
;nofwinningconditions=nofwinningconditions-1

winningcondition$(0)="None (e.g. collect star)"
winningcondition$(1)="Rescue All Stinkers"
winningcondition$(2)="Capture/Destroy Scritters"
winningcondition$(3)="Collect All Gems"
winningcondition$(4)="Destroy All Bricks"
winningcondition$(5)="Destroy FireFlowers"
winningcondition$(6)="Race"
winningcondition$(7)="Capture/Destroy Crabs"
winningcondition$(8)="Rescue All BabyBoomers"
winningcondition$(9)="Destroy All ZBots"

;.winning
;Data "None (e.g. collect star)","Rescue All Stinkers","Capture/Destroy Scritters","Collect All Gems","Destroy All Bricks","Destroy FireFlowers","Race","Capture/Destroy Crabs","Rescue All BabyBoomers","Destroy All ZBots"
;Data "Done"

Global CurrentLevelNumber=0

; Use a ring buffer to track which wlvs have been visited.
Const PreviousLevelNumberBufferSize=100
; Waste a slot in the buffer to differentiate between empty and full states.
; The alternative is using a bool, but since bools and ints seem to be the same type in Blitz and thus the same size, there's no reason to put forth the implementation effort.
Const PreviousLevelNumberBufferMax=PreviousLevelNumberBufferSize
Dim PreviousLevelNumberBuffer(PreviousLevelNumberBufferMax)
Global PreviousLevelNumberBufferStart
Global PreviousLevelNumberBufferCurrent
Global OpenedFirstLevelYet ; This is necessary to prevent CurrentLevelNumber's initial value from being added to the ring buffer.

Function ResetPreviousLevelNumberBuffer()

	PreviousLevelNumberBufferStart=0
	PreviousLevelNumberBufferCurrent=0
	OpenedFirstLevelYet=False

End Function

Global ShowingError=False
Global UsingWireFrame=False

; END EDITOR MASTER DATA

Global CustomIconName$="Standard"
Global CustomMapName$

; The default level coordinates to focus on when opening a level.
; No longer in use since the camera is now centered when opening a level.
Const DefaultCameraFocusX=7
Const DefaultCameraFocusY=10

Dim LevelMesh(MaxLevelCoordinate),LevelSurface(MaxLevelCoordinate) ; one for each row
Dim WaterMesh(MaxLevelCoordinate),WaterSurface(MaxLevelCoordinate)
Dim LogicMesh(MaxLevelCoordinate),LogicSurface(MaxLevelCoordinate)
Const LogicVerticesPerTile=8
Dim DirtyNormals(MaxLevelCoordinate)
Global ShowLogicMesh=False

Const ShowLevelMeshHide=0
Const ShowLevelMeshTransparent=1
Const ShowLevelMeshShow=2

Const ShowLevelMeshMax=2
Global ShowLevelMesh=ShowLevelMeshShow

Const ShowObjectHide=0
Const ShowObjectNormal=1
Const ShowObjectMeshIds=2
Const ShowObjectMeshIndices=3
Const ShowObjectMeshCount=4

Const ShowObjectMeshMax=4
Global ShowObjectMesh=ShowObjectNormal

Const BrushWrapRelative=0
Const BrushWrapModulus=1
Const BrushWrapRandom=2
Const BrushWrapMirrorX=200
Const BrushWrapMirrorY=300
Const BrushWrapMirrorXY=400

Const BrushWrapMax=2
Global BrushWrap=BrushWrapRelative

Const StepPerClick=0
Const StepPerPlacement=1
Const StepPerTile=2

Const StepPerMax=2
Global StepPer=StepPerPlacement

Global DidStepPerClick=False

Global ShowObjectPositions=False ; this is the marker feature suggested by Samuel
Global BorderExpandOption=0 ;0-current, 1-duplicate

Global PreventPlacingObjectsOutsideLevel=True

Function ToolbarPositionX(StepsFromToolbarLeft)

	PartitionCount=8
	Partition=GfxWidth/PartitionCount
	Return Partition*(StepsFromToolbarLeft-1)+Partition/2

End Function

Function ToolbarPositionY(StepsFromToolbarTop)

	Return GfxHeight-125+45*StepsFromToolbarTop

End Function

Function IsMouseOverToolbarItem(x,y)

	mx=MouseX()
	my=MouseY()
	Return mx>=x-50 And mx<x+50 And my>y And my<y+30

End Function

; The position of the level editor cursor.
Const BrushCursorInvalid=-100000
Global BrushCursorX=BrushCursorInvalid
Global BrushCursorY=BrushCursorInvalid

Const BrushModeNormal=0
Const BrushModeBlock=1
Const BrushModeBlockPlacing=2
Const BrushModeFill=3
Const BrushModeInlineHard=4
Const BrushModeInlineSoft=5
Const BrushModeOutlineHard=6
Const BrushModeOutlineSoft=7
Const BrushModeDiagonalNE=8
Const BrushModeDiagonalSE=9
Const BrushModeRow=10
Const BrushModeColumn=11

; Negative brush mode IDs can't be selected from the normal brush mode menu.
; These are placed here to be adjacent to normal brush mode.
Const BrushModeTestLevel=-2
Const BrushModeSetMirror=-3

Const MaxBrushMode=11
Global BrushMode=BrushModeNormal

Const BlockPlacingModeNone=-1
Const BlockPlacingModePlace=0
Const BlockPlacingModeCopy=1
Const BlockPlacingModeDelete=2
Global BlockPlacingMode=BlockPlacingModeNone

Const DupeModeNone=0
Const DupeModeX=1
Const DupeModeY=2
Const DupeModeXPlusY=3

Const DupeModeMax=3
Global DupeMode=DupeModeNone

Global MirrorPositionX=-1
Global MirrorPositionY=-1

Global MirrorEntityX
Global MirrorEntityY

Function IsBrushInBlockMode()
	Return BrushMode=BrushModeBlock Or BrushMode=BrushModeBlockPlacing
End Function

Function IsBrushInInlineMode()
	Return BrushMode=BrushModeInlineSoft Or BrushMode=BrushModeInlineHard
End Function

Function IsBrushInOutlineMode()
	Return BrushMode=BrushModeOutlineSoft Or BrushMode=BrushModeOutlineHard
End Function

Function StartBlockModeBlock(NewBlockPlacingMode)

	BlockCornerX=BrushCursorX
	BlockCornerY=BrushCursorY
	cornleft=BlockCornerX
	cornright=BlockCornerX
	cornup=BlockCornerY
	corndown=BlockCornerY
	SetBrushMode(BrushModeBlockPlacing)
	BlockPlacingMode=NewBlockPlacingMode

End Function

Global PlacementDensity#=1.0

Global BlockModeMesh,BlockModeSurface,BlockCornerX,BlockCornerY
Global cornleft,cornright,cornup,corndown
Global LevelTextureNum, WaterTextureNum
Dim LevelTextureName$(30),WaterTextureName$(20)
Global LevelTextureCustomName$,WaterTextureCustomName$
Global NofLevelTextures,NofWatertextures,CurrentLevelTexture,CurrentWaterTexture

Global LevelTexture ; the actual texture
Global WaterTexture
Global LevelMusic,LevelWeather

Global Leveltimer

Global BrushWidth=1
Global BrushHeight=1

Dim LogicName$(14)
LogicName$(0)="Floor"
LogicName$(1)="Wall"
LogicName$(2)="Water"
LogicName$(3)="Teleporter"
LogicName$(4)="Bridge"
LogicName$(5)="Lava"
LogicName$(6)="06"
LogicName$(7)="07"
LogicName$(8)="Cage"
LogicName$(9)="Button"
LogicName$(10)="Stinker Exit"
LogicName$(11)="Ice"
LogicName$(12)="Ice Corner"
LogicName$(13)="Ice Wall"
LogicName$(14)="Ice Float"

; Directory Names
Global GlobalDirName$ = "UserData"; SpecialFolderLocation($1c)+"\Midnight Synergy\WA Editor"
CreateDir GlobalDirName$
CreateDir GlobalDirName$+"\Temp"
CreateDir GlobalDirName$+"\Custom"
CreateDir GlobalDirName$+"\Custom\Editing"
CreateDir GlobalDirName$+"\Custom\Editing\Archive"
CreateDir GlobalDirName$+"\Custom\Editing\Current"
CreateDir GlobalDirName$+"\Custom\Editing\Profiles"
CreateDir GlobalDirName$+"\Custom\Editing\Hubs"
CreateDir GlobalDirName$+"\Custom\Downloads Inbox"
CreateDir GlobalDirName$+"\Custom\Downloads Outbox"
CreateDir GlobalDirName$+"\Custom\Leveltextures"
CreateDir GlobalDirName$+"\Custom\Icons"
CreateDir GlobalDirName$+"\Custom\Maps"
CreateDir "Data"
CreateDir "Data\Adventures"

Global EditorUserName$=""
Global AdventureUserName$=""
Global NofEditorUserNames
Dim EditorUserNamesListed$(100)
Global EditorUserNameEntered$=""

Global AdventureFileName$
Global NofAdventureFileNames
Const MaxNofAdventureFileNames=10000
Dim AdventureFileNamesListed$(MaxNofAdventureFileNames)
;Dim AdventureTitlesListed$(MaxNofAdventureFileNames)
Global AdventureNameEntered$=""
Global AdventureFileNamesListedStart
Global AdventureNameSelected

Const AdventureCurrentArchiveCurrent=0
Const AdventureCurrentArchiveArchive=1
Const AdventureCurrentArchivePlayer=2
Const AdventureCurrentArchiveDataAdventures=3
Const MaxAdventureCurrentArchive=3

Global AdventureCurrentArchive=AdventureCurrentArchiveCurrent

Global DisplayFullScreen=False

;filed=ReadFile (globaldirname$+"\display-ed.wdf")
;If filed>0
;
;	DisplayFullScreen=ReadInt(filed)
;	CloseFile filed
;EndIf

Global EditorControls$=GlobalDirName$+"\editorcontrols.wdf"

GetTextureNames()

; LEVEL SIZE
; ============
Global LevelWidth=40 ; in tiles
Global LevelHeight=40 ; in tiles

Const MaxLevelSize=101
Const MaxLevelCoordinate=MaxLevelSize-1
Const MaxTilesPerLevel=MaxLevelSize*MaxLevelSize

Global LevelEdgeStyle=1

Global WidthLeftChange,WidthRightChange,HeightTopChange,HeightBottomChange

; TILES
; ============

Type TerrainTile

Field Texture ; corresponding to squares in LevelTexture
Field Rotation ; 0-3, and 4-7 for "flipped"
Field SideTexture ; texture for extrusion walls
Field SideRotation ; 0-3, and 4-7 for "flipped"
Field Random# ; random height pertubation of tile
Field Height# ; height of "center" - e.g. to make ditches and hills
Field Extrusion# ; extrusion with walls around it
Field Rounding ; 0-no, 1-yes: are floors rounded if on a drop-off corner
Field EdgeRandom ; 0-no, 1-yes: are edges rippled
Field Logic

End Type

Type WaterTile

Field Texture
Field Rotation
Field Height#
Field Turbulence#

End Type

Type Tile

Field Terrain.TerrainTile
Field Water.WaterTile

End Type

Function NewTile.Tile()

	Result.Tile=New Tile
	Result\Terrain=New TerrainTile
	Result\Terrain\Texture=8
	Result\Terrain\SideTexture=6
	Result\Water=New WaterTile
	Return Result

End Function

Dim LevelTiles.Tile(MaxLevelCoordinate,MaxLevelCoordinate)
Dim CopyLevelTiles.Tile(MaxLevelCoordinate,MaxLevelCoordinate)
Dim BrushTiles.Tile(MaxLevelCoordinate,MaxLevelCoordinate)

Dim DraggedTiles.Tile(MaxLevelCoordinate,MaxLevelCoordinate)
Dim DraggedTilesEnabled(MaxLevelCoordinate,MaxLevelCoordinate)
Global DraggedTilesSpotX=-1
Global DraggedTilesSpotY=-1

For i=0 To MaxLevelCoordinate
	For j=0 To MaxLevelCoordinate
		LevelTiles(i,j)=NewTile()
		CopyLevelTiles(i,j)=NewTile()
		BrushTiles(i,j)=NewTile()
		DraggedTiles(i,j)=NewTile()
		DraggedTilesEnabled(i,j)=False
	Next
Next

Global CurrentTile.Tile=NewTile()
Global TargetTile.Tile=NewTile()
Global TempTile.Tile=NewTile()

Dim LevelTileObjectCount(MaxLevelCoordinate,MaxLevelCoordinate) ; for changing the marker color when there's more than one object present

Global ChunkTileU#,ChunkTileV#

Global CurrentMesh,CurrentSurface ; for tile rendering in tile camera

Global LevelDetail=4
Global CurrentVertex=0

Global CurrentTileTextureUse=True
Global CurrentTileSideTextureUse=True
Global CurrentTileHeightUse=True
Global CurrentTileExtrusionUse=True
Global CurrentTileRandomUse=True
Global CurrentTileRoundingUse=True
Global CurrentTileEdgeRandomUse=True
Global CurrentTileLogicUse=True

Global CurrentWaterTileTextureUse=True
Global CurrentWaterTileHeightUse=True
Global CurrentWaterTileTurbulenceUse=True

Global StepSizeTileRandom#
Global StepSizeTileHeight#
Global StepSizeTileExtrusion#
Global StepSizeWaterTileHeight#
Global StepSizeWaterTileTurbulence#

Global OnceTilePlacement=True

Function IsAnyStepSizeActive()

	Return StepSizeTileRandom#<>0 Or StepSizeTileHeight#<>0 Or StepSizeTileExtrusion#<>0 Or StepSizeWaterTileHeight#<>0 Or StepSizeWaterTileTurbulence#<>0

End Function

Global TargetTileTextureUse=True
Global TargetTileSideTextureUse=True
Global TargetTileHeightUse=True
Global TargetTileExtrusionUse=True
Global TargetTileRandomUse=True
Global TargetTileRoundingUse=True
Global TargetTileEdgeRandomUse=True
Global TargetTileLogicUse=True

Global TargetWaterTileUse=True
Global TargetWaterTileHeightUse=True
Global TargetWaterTileTurbulenceUse=True

; used for flood fill algorithm
Dim LevelTileVisited(MaxLevelCoordinate,MaxLevelCoordinate)
Dim FloodStackX(MaxTilesPerLevel) ; no pun intended hahahahaha
Dim FloodStackY(MaxTilesPerLevel)
Dim FloodedStackX(MaxTilesPerLevel) ; Stores positions to flood when done visiting. Useful for flood fill as used in inline mode.
Dim FloodedStackY(MaxTilesPerLevel)
Global FloodElementCount ; I wish I had OOP
Global FloodedElementCount
Global FloodOutsideAdjacent

Global ParticlePreview
Global ParticlePreviewSurface
Const CameraParticlePreviewSize=50

; TILE PRESETS
; ========================
Global CurrentTilePresetCategory, NofTilePresetCategories
Global CurrentTilePresetTile, NofTilePresetTiles
Dim TilePresetCategoryName$(1000)
Dim TilePresetTileName$(1000)
Dir=ReadDir("Data\Editor\TilePresets")
file$=NextFile$(Dir)
While file$<>""
	If file$<>"." And file$<>".." And FileType("Data\Editor\TilePresets\"+file$)=2
		TilePresetCategoryName$(NofTilePresetCategories)=file$
		NofTilePresetCategories=NofTilePresetCategories+1
	EndIf
	file$=NextFile$(Dir)
Wend
CloseDir dir
i=0
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
Until NofTilePresetTiles>0

; OBJECTS
; ------------------------

Global NofObjectsInstantiated=0

Dim ObjectAdjusterString$(MaxNofObjects,30)

Global HighlightWopAdjusters=True
Global NofWopAdjusters=0
Dim ObjectAdjusterWop$(30)

Dim ObjectPositionMarker(MaxNofObjects)
Dim WorldAdjusterPositionMarker(3)
Global CurrentObjectMoveXYGoalMarker
Global WhereWeEndedUpMarker ; For traveling with G between LevelExits.
Global WhereWeEndedUpAlpha#=0.0

Dim SimulatedObjectXScale#(MaxNofObjects)
Dim SimulatedObjectZScale#(MaxNofObjects)
Dim SimulatedObjectYScale#(MaxNofObjects)
Dim SimulatedObjectXAdjust#(MaxNofObjects)
Dim SimulatedObjectZAdjust#(MaxNofObjects)
Dim SimulatedObjectYAdjust#(MaxNofObjects)
Dim SimulatedObjectPitchAdjust#(MaxNofObjects)
Dim SimulatedObjectYawAdjust#(MaxNofObjects)
Dim SimulatedObjectRollAdjust#(MaxNofObjects)
Dim SimulatedObjectX#(MaxNofObjects),SimulatedObjectY#(MaxNofObjects),SimulatedObjectZ#(MaxNofObjects)
Dim SimulatedObjectPitch#(MaxNofObjects)
Dim SimulatedObjectYaw#(MaxNofObjects)
Dim SimulatedObjectRoll#(MaxNofObjects)
Dim SimulatedObjectPitch2#(MaxNofObjects),SimulatedObjectYaw2#(MaxNofObjects),SimulatedObjectRoll2#(MaxNofObjects)
Dim SimulatedObjectActive(MaxNofObjects),SimulatedObjectLastActive(MaxNofObjects)
Dim SimulatedObjectStatus(MaxNofObjects)
Dim SimulatedObjectTimer(MaxNofObjects)
Dim SimulatedObjectData(MaxNofObjects,10)
Dim SimulatedObjectCurrentAnim(MaxNofObjects)
Dim SimulatedObjectMovementSpeed(MaxNofObjects)
Dim SimulatedObjectMoveXGoal(MaxNofObjects),SimulatedObjectMoveYGoal(MaxNofObjects)
Dim SimulatedObjectData10(MaxNofObjects)
Dim SimulatedObjectSubType(MaxNofObjects)
Dim SimulatedObjectTileTypeCollision(MaxNofObjects)
Dim SimulatedObjectExclamation(MaxNofObjects)
Dim SimulatedObjectFrozen(MaxNofObjects)
;Dim SimulatedObjectScaleAdjust#(MaxNofObjects) ; not useful since ScaleAdjust is set to 1.0 in-game after it is applied to XScale, YScale, and ZScale
Dim SimulatedObjectScaleXAdjust#(MaxNofObjects),SimulatedObjectScaleYAdjust#(MaxNofObjects),SimulatedObjectScaleZAdjust#(MaxNofObjects)

Type GameObject

Field Model.GameObjectModel
Field Attributes.GameObjectAttributes
Field Position.GameObjectPosition

End Type

Type GameObjectModel

Field Entity
Field Texture

Field HatEntity
Field HatTexture

Field AccEntity
Field AccTexture

End Type

Type GameObjectAttributes

Field ModelName$,TexName$ ; Formerly TextureName$, but that's a Blitz3d keyword.
Field XScale#,YScale#,ZScale#
Field XAdjust#,YAdjust#,ZAdjust#
Field PitchAdjust#,YawAdjust#,RollAdjust#
Field DX#,DY#,DZ#
Field Pitch#,Yaw#,Roll#
Field Pitch2#,Yaw2#,Roll2#
Field XGoal#,YGoal#,ZGoal#
Field MovementType,MovementTypeData,Speed#,Radius#,RadiusType
Field Data10
Field PushDX#,PushDY#,AttackPower,DefensePower,DestructionType
Field ID,LogicType,LogicSubType ; Again, Type is a Blitz3d keyword.
Field Active,LastActive,ActivationType,ActivationSpeed
Field Status,Timer,TimerMax1,TimerMax2
Field Teleportable,ButtonPush,WaterReact
Field Telekinesisable,Freezable,Reactive,Child,Parent
Field Data0,Data1,Data2,Data3,Data4,Data5,Data6,Data7,Data8,Data9 ; Oh my god. I guess even the slightest convenience has a price.
Field TextData0$,TextData1$,TextData2$,TextData3$
Field Talkable,CurrentAnim,StandardAnim
Field MovementTimer,MovementSpeed,MoveXGoal,MoveYGoal,TileTypeCollision,ObjectTypeCollision
Field Caged,Dead,DeadTimer,Exclamation,Shadow,Linked,LinkBack,Flying,Frozen,Indigo,FutureInt24,FutureInt25
Field ScaleAdjust#,ScaleXAdjust#,ScaleYAdjust#,ScaleZAdjust#
Field FutureFloat5#,FutureFloat6#,FutureFloat7#,FutureFloat8#,FutureFloat9#,FutureFloat10#,FutureString1$,FutureString2$

End Type

Type GameObjectPosition

Field X#,Y#,Z#
Field OldX#,OldY#,OldZ#
Field TileX,TileY,TileX2,TileY2

End Type

Function NewGameObject.GameObject()

	Result.GameObject=New GameObject
	Result\Model=New GameObjectModel
	Result\Attributes=New GameObjectAttributes
	Result\Position=New GameObjectPosition
	Return Result

End Function

Global CurrentObject.GameObject=NewGameObject()
Global TempObject.GameObject=NewGameObject()

Dim LevelObjects.GameObject(MaxNofObjects)
Dim BrushObjects.GameObject(MaxNofObjects)
;Dim BrushObjectModels.GameObjectModel(MaxNofObjects)
;Dim BrushObjectAttributes.GameObjectAttributes(MaxNofObjects)

Global NofPreviewObjects=0
Dim PreviewObjects(MaxNofObjects)

For i=0 To MaxNofObjects
	LevelObjects.GameObject(i)=NewGameObject()
	BrushObjects.GameObject(i)=NewGameObject()
	;BrushObjectModels.GameObjectModels(i)=New GameObjectModel
	;BrushObjectAttributes.GameObjectAttributes(i)=New GameObjectAttributes
Next

Global NofSelectedObjects=0
Dim SelectedObjects(MaxNofObjects)
Global NofDraggedObjects=0
Dim DraggedObjects(MaxNofObjects)

; Whether the dragged objects or tiles have been moved at all.
Global DragChange=False

Global CurrentGrabbedObjectModified=False
Global PreviousSelectedObject=-1
Global NewSelectedObjectCount=0
Global ReadyToCopyFirstSelected=True

Global SelectionMinTileX=101
Global SelectionMaxTileX=101
Global SelectionMinTileY=-1
Global SelectionMaxTileY=-1

Dim CurrentGrabbedObjectMarkers(MaxNofObjects)

Global DragSpotX=-1
Global DragSpotY=-1
Global DragMinTileX=101
Global DragMaxTileX=101
Global DragMinTileY=-1
Global DragMaxTileY=-1

Global TileDragging=False

Function ResetDragSize()

	DragMinTileX=101
	DragMinTileY=101
	DragMaxTileX=-1
	DragMaxTileY=-1

End Function

Function UpdateDragSize(LevelObjectIndex)

	Pos.GameObjectPosition=LevelObjects(LevelObjectIndex)\Position
	If Pos\TileX<DragMinTileX
		DragMinTileX=Pos\TileX
	EndIf
	If Pos\TileX>DragMaxTileX
		DragMaxTileX=Pos\TileX
	EndIf
	If Pos\TileY<DragMinTileY
		DragMinTileY=Pos\TileY
	EndIf
	If Pos\TileY>DragMaxTileY
		DragMaxTileY=Pos\TileY
	EndIf

End Function

Function RecalculateDragSize()

	ResetDragSize()
	For i=0 To NofDraggedObjects-1
		UpdateDragSize(DraggedObjects(i))
	Next

End Function

Function ResetSelectionSize()

	SelectionMinTileX=101
	SelectionMinTileY=101
	SelectionMaxTileX=-1
	SelectionMaxTileY=-1

End Function

Function UpdateSelectionSize(LevelObjectIndex)

	Pos.GameObjectPosition=LevelObjects(LevelObjectIndex)\Position
	If Pos\TileX<SelectionMinTileX
		SelectionMinTileX=Pos\TileX
	EndIf
	If Pos\TileX>SelectionMaxTileX
		SelectionMaxTileX=Pos\TileX
	EndIf
	If Pos\TileY<SelectionMinTileY
		SelectionMinTileY=Pos\TileY
	EndIf
	If Pos\TileY>SelectionMaxTileY
		SelectionMaxTileY=Pos\TileY
	EndIf

End Function

Function RecalculateSelectionSize()

	ResetSelectionSize()
	For i=0 To NofSelectedObjects-1
		UpdateSelectionSize(SelectedObjects(i))
	Next

End Function

Function RecalculateObjectAdjusterModes()

	ReadyToCopyFirstSelected=True
	CurrentGrabbedObjectModified=False

	For i=0 To NofSelectedObjects-1
		ReadObjectIntoCurrentObject(LevelObjects(SelectedObjects(i)))
	Next

	BuildCurrentObjectModel()

End Function

Function GetSelectedObjectIndexInSelectedObjects(LevelObjectIndex)

	For i=0 To NofSelectedObjects-1
		If SelectedObjects(i)=LevelObjectIndex
			Return i
		EndIf
	Next

	Return -1

End Function

Function GetDraggedObjectIndexInDraggedObjects(LevelObjectIndex)

	For i=0 To NofDraggedObjects-1
		If DraggedObjects(i)=LevelObjectIndex
			Return i
		EndIf
	Next

	Return -1

End Function

Function IsObjectSelected(LevelObjectIndex)

	Return GetSelectedObjectIndexInSelectedObjects(LevelObjectIndex)<>-1

End Function

Function IsObjectDragged(LevelObjectIndex)

	Return GetDraggedObjectIndexInDraggedObjects(LevelObjectIndex)<>-1

End Function

Function IsOnlyObjectSelected(LevelObjectIndex)

	Return NofSelectedObjects=1 And IsObjectSelected(LevelObjectIndex)

End Function

Function StartObjectDrag()

	DragSpotX=BrushCursorX
	DragSpotY=BrushCursorY
	RecalculateDragSize()

End Function

Function ClearObjectDrag()

	NofDraggedObjects=0

End Function

Function ClearObjectSelection()

	NofSelectedObjects=0
	CurrentGrabbedObjectModified=False
	For i=0 To MaxNofObjects-1
		HideSelectedObjectMarker(i)
	Next
	MakeAllObjectAdjustersAbsolute()
	ReadyToCopyFirstSelected=True

End Function

Function AddSelectObject(LevelObjectIndex)

	If Not IsObjectSelected(LevelObjectIndex)
		AddSelectObjectInner(LevelObjectIndex)
	EndIf

End Function

Function AddSelectObjectInner(LevelObjectIndex)

	SelectedObjects(NofSelectedObjects)=LevelObjectIndex
	;ReadObjectIntoCurrentObject(LevelObjects(LevelObjectIndex))
	ShowSelectedObjectMarker(LevelObjectIndex)
	PreviousSelectedObject=LevelObjectIndex
	NofSelectedObjects=NofSelectedObjects+1
	NewSelectedObjectCount=NewSelectedObjectCount+1

	; Doing this discards any non-Updated changes to previously-selected objects.
	RecalculateObjectAdjusterModes()

End Function

Function RemoveSelectObject(LevelObjectIndex)

	Index=GetSelectedObjectIndexInSelectedObjects(LevelObjectIndex)
	If Index<>-1
		RemoveSelectObjectInner(Index)
		HideSelectedObjectMarker(LevelObjectIndex)
	EndIf

End Function

Function RemoveSelectObjectInner(Index)

	For i=Index To NofSelectedObjects-2
		SelectedObjects(i)=SelectedObjects(i+1)
	Next
	NofSelectedObjects=NofSelectedObjects-1

	RecalculateObjectAdjusterModes()

End Function

Function AddDragObject(LevelObjectIndex)

	If Not IsObjectDragged(LevelObjectIndex)
		AddDragObjectInner(LevelObjectIndex)
	EndIf

End Function

Function AddDragObjectInner(LevelObjectIndex)

	DraggedObjects(NofDraggedObjects)=LevelObjectIndex
	NofDraggedObjects=NofDraggedObjects+1

End Function

Function RemoveDraggedObject(LevelObjectIndex)

	Index=GetDraggedObjectIndexInDraggedObjects(LevelObjectIndex)
	If Index<>-1
		RemoveDraggedObjectInner(Index)
	EndIf

End Function

Function RemoveDraggedObjectInner(Index)

	For i=Index To NofDraggedObjects-2
		DraggedObjects(i)=DraggedObjects(i+1)
	Next
	NofDraggedObjects=NofDraggedObjects-1

End Function

Function ToggleSelectObject(LevelObjectIndex)

	Index=GetSelectedObjectIndexInSelectedObjects(LevelObjectIndex)
	If Index=-1
		AddSelectObjectInner(LevelObjectIndex)
	Else
		RemoveSelectObjectInner(Index)
		HideSelectedObjectMarker(LevelObjectIndex)
	EndIf

End Function

Function PrepareObjectSelection()

	If (Not CtrlDown())
		ClearObjectSelection()
	EndIf

	NewSelectedObjectCount=0

End Function

Function FinishObjectSelection()

	If NewSelectedObjectCount<>0
		BuildCurrentObjectModel()
		If AreAllObjectAdjustersAbsolute()
			SetBrushToCurrentObject()
		EndIf
	EndIf

	NofDraggedObjects=NofSelectedObjects
	For i=0 To NofDraggedObjects-1
		DraggedObjects(i)=SelectedObjects(i)
	Next
	StartObjectDrag()

End Function

Global CurrentAdjusterRandomized=False
Global CurrentAdjusterAbsolute=True
Global CurrentAdjusterZero=False
Global LeftAdj$=""
Global RightAdj$=""

Type ObjectAdjusterInt

Field Name$
Field RandomEnabled,RandomMin,RandomMax,RandomMinDefault,RandomMaxDefault
Field Absolute

End Type

Type ObjectAdjusterFloat

Field Name$
Field RandomEnabled,RandomMin#,RandomMax#,RandomMinDefault#,RandomMaxDefault#
Field Absolute

End Type

Type ObjectAdjusterString

Field Name$
Field RandomEnabled
Field Absolute

End Type

Function NewObjectAdjusterInt.ObjectAdjusterInt(Name$,RandomMin,RandomMax)

	Result.ObjectAdjusterInt=New ObjectAdjusterInt
	Result\Name$=Name$
	Result\RandomEnabled=False
	Result\RandomMin=RandomMin
	Result\RandomMax=RandomMax
	Result\RandomMinDefault=RandomMin
	Result\RandomMaxDefault=RandomMax
	Result\Absolute=True
	Return Result

End Function

Function NewObjectAdjusterFloat.ObjectAdjusterFloat(Name$,RandomMin#,RandomMax#)

	Result.ObjectAdjusterFloat=New ObjectAdjusterFloat
	Result\Name$=Name$
	Result\RandomEnabled=False
	Result\RandomMin#=RandomMin
	Result\RandomMax#=RandomMax
	Result\RandomMinDefault#=RandomMin
	Result\RandomMaxDefault#=RandomMax
	Result\Absolute=True
	Return Result

End Function

Function NewObjectAdjusterString.ObjectAdjusterString(Name$)

	Result.ObjectAdjusterString=New ObjectAdjusterString
	Result\Name$=Name$
	Result\RandomEnabled=False
	Result\Absolute=True
	Return Result

End Function

Function AdjustObjectAdjusterInt(ObjectAdjuster.ObjectAdjusterInt,CurrentValue,SlowInt,FastInt,DelayTime)

	If ObjectAdjuster\RandomEnabled
		If OnLeftHalfAdjuster()
			ObjectAdjuster\RandomMin=AdjustInt(ObjectAdjuster\Name$+" Min: ", ObjectAdjuster\RandomMin, SlowInt, FastInt, DelayTime)
		Else
			ObjectAdjuster\RandomMax=AdjustInt(ObjectAdjuster\Name$+" Max: ", ObjectAdjuster\RandomMax, SlowInt, FastInt, DelayTime)
		EndIf
	Else
		CurrentValue=AdjustInt(ObjectAdjuster\Name$+": ", CurrentValue, SlowInt, FastInt, DelayTime)
		If UsedRawInput
			ObjectAdjuster\Absolute=True
		EndIf
	EndIf
	If ReturnPressed()
		ObjectAdjuster\RandomEnabled=Not ObjectAdjuster\RandomEnabled
		ObjectAdjuster\RandomMin=ObjectAdjuster\RandomMinDefault
		ObjectAdjuster\RandomMax=ObjectAdjuster\RandomMaxDefault
	EndIf
	Return CurrentValue

End Function

Function AdjustObjectAdjusterFloat#(ObjectAdjuster.ObjectAdjusterFloat,CurrentValue#,SlowFloat#,FastFloat#,DelayTime)

	If ObjectAdjuster\RandomEnabled
		If OnLeftHalfAdjuster()
			ObjectAdjuster\RandomMin=AdjustFloat#(ObjectAdjuster\Name$+" Min: ", ObjectAdjuster\RandomMin, SlowFloat#, FastFloat#, DelayTime)
		Else
			ObjectAdjuster\RandomMax=AdjustFloat#(ObjectAdjuster\Name$+" Max: ", ObjectAdjuster\RandomMax, SlowFloat#, FastFloat#, DelayTime)
		EndIf
	Else
		CurrentValue#=AdjustFloat#(ObjectAdjuster\Name$+": ", CurrentValue, SlowFloat#, FastFloat#, DelayTime)
		If UsedRawInput
			ObjectAdjuster\Absolute=True
		EndIf
	EndIf
	If ReturnPressed()
		ObjectAdjuster\RandomEnabled=Not ObjectAdjuster\RandomEnabled
		ObjectAdjuster\RandomMin=ObjectAdjuster\RandomMinDefault
		ObjectAdjuster\RandomMax=ObjectAdjuster\RandomMaxDefault
	EndIf
	Return CurrentValue

End Function

Function AdjustObjectAdjusterToggle(ObjectAdjuster.ObjectAdjusterInt,CurrentValue,SlowInt,FastInt,RawInput,ValueLow,ValueHigh,DelayTime)

	If ObjectAdjuster\RandomEnabled
		If OnLeftHalfAdjuster()
			ObjectAdjuster\RandomMin=AdjustInt(ObjectAdjuster\Name$+" Min: ", ObjectAdjuster\RandomMin, SlowInt, FastInt, DelayTime)
		Else
			ObjectAdjuster\RandomMax=AdjustInt(ObjectAdjuster\Name$+" Max: ", ObjectAdjuster\RandomMax, SlowInt, FastInt, DelayTime)
		EndIf
	ElseIf ReturnKey=False And MouseDebounceFinished()
		If RawInput=True
			CurrentValue=InputInt(ObjectAdjuster\Name$+": ")
			ObjectAdjuster\Absolute=True
		Else
			If ObjectAdjuster\Absolute
				If CurrentValue=ValueLow
					CurrentValue=ValueHigh
				Else
					CurrentValue=ValueLow
				EndIf
			Else
				CurrentValue=AdjustInt(ObjectAdjuster\Name$+": ", CurrentValue, SlowInt, FastInt, DelayTime)
			EndIf
		EndIf
		If MouseScroll=0
			MouseDebounceSet(DelayTime)
		EndIf
	EndIf
	If ReturnPressed()
		ObjectAdjuster\RandomEnabled=Not ObjectAdjuster\RandomEnabled
		ObjectAdjuster\RandomMin=ObjectAdjuster\RandomMinDefault
		ObjectAdjuster\RandomMax=ObjectAdjuster\RandomMaxDefault
	EndIf
	Return CurrentValue

End Function

Function AdjustObjectAdjusterBits(ObjectAdjuster.ObjectAdjusterInt,CurrentValue,i,DelayTime)

	If ObjectAdjuster\Absolute And (Not ObjectAdjuster\RandomEnabled) And (LeftMouse Or RightMouse Or MouseScroll<>0) And MouseDebounceFinished()
		StartX=SidebarX+10
		StartY=SidebarY+305
		StartY=StartY+15+(i-ObjectAdjusterStart)*15
		tex2$="TTC"
		tex$="00000 00000 00000"

		HalfNameWidth=4*Len(tex2$+": "+tex$)
		BitStartX=StartX+92-HalfNameWidth+8*Len(tex2$+": ")

		BitPositionIndex=GetBitPositionIndex(BitStartX)
		BitIndex=BitPositionIndexToBitIndex(BitPositionIndex)
		If BitIndexIsValid(BitIndex) And BitPositionIndexIsValid(BitPositionIndex)
			CurrentValue=CurrentValue Xor 2^BitIndex
		EndIf

		If LeftMouse=True Or RightMouse=True
			MouseDebounceSet(DelayTime)
		EndIf
	EndIf
	If CtrlDown()
		CurrentValue=InputInt(ObjectAdjuster\Name$+": ")
		ObjectAdjuster\Absolute=True
	EndIf
	If ReturnPressed()
		ObjectAdjuster\RandomEnabled=Not ObjectAdjuster\RandomEnabled
	EndIf
	Return CurrentValue

End Function

Function RandomObjectAdjusterInt(ObjectAdjuster.ObjectAdjusterInt)

	Return Rand(ObjectAdjuster\RandomMin,ObjectAdjuster\RandomMax)

End Function

Function RandomObjectAdjusterFloat#(ObjectAdjuster.ObjectAdjusterFloat)

	Return Rnd#(ObjectAdjuster\RandomMin,ObjectAdjuster\RandomMax)

End Function

Function SetAdjusterDisplayInt$(ObjectAdjuster.ObjectAdjusterInt,CurrentValue,tex$)

	CurrentAdjusterRandomized=ObjectAdjuster\RandomEnabled
	CurrentAdjusterAbsolute=ObjectAdjuster\Absolute
	CurrentAdjusterZero=(CurrentValue=0)
	LeftAdj$=ObjectAdjuster\RandomMin
	RightAdj$=ObjectAdjuster\RandomMax
	If CurrentAdjusterAbsolute
		Return tex$
	Else
		Return CurrentValue
	EndIf

End Function

Function SetAdjusterDisplayFloat$(ObjectAdjuster.ObjectAdjusterFloat,CurrentValue#,tex$)

	CurrentAdjusterRandomized=ObjectAdjuster\RandomEnabled
	CurrentAdjusterAbsolute=ObjectAdjuster\Absolute
	CurrentAdjusterZero=(CurrentValue=0)
	LeftAdj$=ObjectAdjuster\RandomMin
	RightAdj$=ObjectAdjuster\RandomMax
	If CurrentAdjusterAbsolute
		Return tex$
	Else
		Return CurrentValue
	EndIf

End Function

Function SetAdjusterDisplayString$(ObjectAdjuster.ObjectAdjusterString,CurrentValue$,tex$)

	CurrentAdjusterRandomized=ObjectAdjuster\RandomEnabled
	CurrentAdjusterAbsolute=ObjectAdjuster\Absolute
	CurrentAdjusterZero=True
	LeftAdj$=""
	RightAdj$=""
	If CurrentAdjusterAbsolute
		Return tex$
	Else
		Return "..."
	EndIf

End Function

Global ObjectAdjusterDefensePower.ObjectAdjusterInt=NewObjectAdjusterInt("DefensePower",0,33)
Global ObjectAdjusterAttackPower.ObjectAdjusterInt=NewObjectAdjusterInt("AttackPower",0,33)
Global ObjectAdjusterDestructionType.ObjectAdjusterInt=NewObjectAdjusterInt("DestructionType",0,1)
Global ObjectAdjusterID.ObjectAdjusterInt=NewObjectAdjusterInt("ID",100,200)
Global ObjectAdjusterLogicType.ObjectAdjusterInt=NewObjectAdjusterInt("Type",170,173)
Global ObjectAdjusterLogicSubType.ObjectAdjusterInt=NewObjectAdjusterInt("SubType",0,8)
Global ObjectAdjusterActivationSpeed.ObjectAdjusterInt=NewObjectAdjusterInt("ActivationSpeed",2,40)
Global ObjectAdjusterActivationType.ObjectAdjusterInt=NewObjectAdjusterInt("ActivationType",12,16)
Global ObjectAdjusterTimerMax1.ObjectAdjusterInt=NewObjectAdjusterInt("TimerMax1",1,100)
Global ObjectAdjusterTimerMax2.ObjectAdjusterInt=NewObjectAdjusterInt("TimerMax2",1,100)
Global ObjectAdjusterTimer.ObjectAdjusterInt=NewObjectAdjusterInt("Timer",1,100)
Global ObjectAdjusterWaterReact.ObjectAdjusterInt=NewObjectAdjusterInt("WaterReact",0,10)
Global ObjectAdjusterFreezable.ObjectAdjusterInt=NewObjectAdjusterInt("Freezable",0,1)
Global ObjectAdjusterFrozen.ObjectAdjusterInt=NewObjectAdjusterInt("Frozen",0,100)
Global ObjectAdjusterTalkable.ObjectAdjusterInt=NewObjectAdjusterInt("Talkable",0,100)
Global ObjectAdjusterMovementSpeed.ObjectAdjusterInt=NewObjectAdjusterInt("MovementSpeed",10,40)
Global ObjectAdjusterMovementType.ObjectAdjusterInt=NewObjectAdjusterInt("MovementType",41,48)
Global ObjectAdjusterMovementTypeData.ObjectAdjusterInt=NewObjectAdjusterInt("MovementTypeData",0,30)
Global ObjectAdjusterExclamation.ObjectAdjusterInt=NewObjectAdjusterInt("Exclamation",0,99)
Global ObjectAdjusterLinked.ObjectAdjusterInt=NewObjectAdjusterInt("Linked",0,10)
Global ObjectAdjusterLinkBack.ObjectAdjusterInt=NewObjectAdjusterInt("LinkBack",0,10)
Global ObjectAdjusterShadow.ObjectAdjusterInt=NewObjectAdjusterInt("Shadow",0,10)
Global ObjectAdjusterParent.ObjectAdjusterInt=NewObjectAdjusterInt("Parent",0,10)
Global ObjectAdjusterChild.ObjectAdjusterInt=NewObjectAdjusterInt("Child",0,10)
Global ObjectAdjusterData10.ObjectAdjusterInt=NewObjectAdjusterInt("Data10",0,10)
Global ObjectAdjusterCaged.ObjectAdjusterInt=NewObjectAdjusterInt("Caged",0,1)
Global ObjectAdjusterDead.ObjectAdjusterInt=NewObjectAdjusterInt("Dead",0,3)
Global ObjectAdjusterDeadTimer.ObjectAdjusterInt=NewObjectAdjusterInt("DeadTimer",1,100)
Global ObjectAdjusterMovementTimer.ObjectAdjusterInt=NewObjectAdjusterInt("MovementTimer",0,1000)
Global ObjectAdjusterFlying.ObjectAdjusterInt=NewObjectAdjusterInt("Flying",0,20)
Global ObjectAdjusterIndigo.ObjectAdjusterInt=NewObjectAdjusterInt("Indigo",0,1)
Global ObjectAdjusterStatus.ObjectAdjusterInt=NewObjectAdjusterInt("Status",0,10)
Global ObjectAdjusterButtonPush.ObjectAdjusterInt=NewObjectAdjusterInt("ButtonPush",0,1)
Global ObjectAdjusterTeleportable.ObjectAdjusterInt=NewObjectAdjusterInt("Teleportable",0,1)
Global ObjectAdjusterTileTypeCollision.ObjectAdjusterInt=NewObjectAdjusterInt("TileTypeCollision",0,1)
Global ObjectAdjusterObjectTypeCollision.ObjectAdjusterInt=NewObjectAdjusterInt("ObjectTypeCollision",0,1)
Global ObjectAdjusterActive.ObjectAdjusterInt=NewObjectAdjusterInt("Active",0,1001)
Global ObjectAdjusterMoveXGoal.ObjectAdjusterInt=NewObjectAdjusterInt("MoveXGoal",0,39)
Global ObjectAdjusterMoveYGoal.ObjectAdjusterInt=NewObjectAdjusterInt("MoveYGoal",0,39)
Global ObjectAdjusterData0.ObjectAdjusterInt=NewObjectAdjusterInt("Data0",0,10)
Global ObjectAdjusterData1.ObjectAdjusterInt=NewObjectAdjusterInt("Data1",0,10)
Global ObjectAdjusterData2.ObjectAdjusterInt=NewObjectAdjusterInt("Data2",0,10)
Global ObjectAdjusterData3.ObjectAdjusterInt=NewObjectAdjusterInt("Data3",0,10)
Global ObjectAdjusterData4.ObjectAdjusterInt=NewObjectAdjusterInt("Data4",0,10)
Global ObjectAdjusterData5.ObjectAdjusterInt=NewObjectAdjusterInt("Data5",0,10)
Global ObjectAdjusterData6.ObjectAdjusterInt=NewObjectAdjusterInt("Data6",0,10)
Global ObjectAdjusterData7.ObjectAdjusterInt=NewObjectAdjusterInt("Data7",0,10)
Global ObjectAdjusterData8.ObjectAdjusterInt=NewObjectAdjusterInt("Data8",0,10)
Global ObjectAdjusterData9.ObjectAdjusterInt=NewObjectAdjusterInt("Data9",0,10)
Global ObjectAdjusterCurrentAnim.ObjectAdjusterInt=NewObjectAdjusterInt("CurrentAnim",0,10)
Global ObjectAdjusterStandardAnim.ObjectAdjusterInt=NewObjectAdjusterInt("StandardAnim",0,10)

Global ObjectAdjusterYawAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("YawAdjust",0.0,360.0)
Global ObjectAdjusterRollAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("RollAdjust",0.0,360.0)
Global ObjectAdjusterPitchAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("PitchAdjust",0.0,360.0)
Global ObjectAdjusterXAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("XAdjust",-0.5,0.5)
Global ObjectAdjusterYAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("YAdjust",-0.5,0.5)
Global ObjectAdjusterZAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("ZAdjust",-0.5,0.5)
Global ObjectAdjusterXScale.ObjectAdjusterFloat=NewObjectAdjusterFloat("XScale",0.5,1.5)
Global ObjectAdjusterYScale.ObjectAdjusterFloat=NewObjectAdjusterFloat("YScale",0.5,1.5)
Global ObjectAdjusterZScale.ObjectAdjusterFloat=NewObjectAdjusterFloat("ZScale",0.5,1.5)
Global ObjectAdjusterScaleAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("ScaleAdjust",0.5,1.5)
Global ObjectAdjusterScaleXAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("ScaleXAdjust",0.5,1.5)
Global ObjectAdjusterScaleYAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("ScaleYAdjust",0.5,1.5)
Global ObjectAdjusterScaleZAdjust.ObjectAdjusterFloat=NewObjectAdjusterFloat("ScaleZAdjust",0.5,1.5)
Global ObjectAdjusterX.ObjectAdjusterFloat=NewObjectAdjusterFloat("X",-0.5,0.5)
Global ObjectAdjusterY.ObjectAdjusterFloat=NewObjectAdjusterFloat("Y",-0.5,0.5)
Global ObjectAdjusterZ.ObjectAdjusterFloat=NewObjectAdjusterFloat("Z",-0.5,0.5)
Global ObjectAdjusterDX.ObjectAdjusterFloat=NewObjectAdjusterFloat("DX",-1.0,1.0)
Global ObjectAdjusterDY.ObjectAdjusterFloat=NewObjectAdjusterFloat("DY",-1.0,1.0)
Global ObjectAdjusterDZ.ObjectAdjusterFloat=NewObjectAdjusterFloat("DZ",-1.0,1.0)
Global ObjectAdjusterSpeed.ObjectAdjusterFloat=NewObjectAdjusterFloat("Speed",-0.5,0.5)
Global ObjectAdjusterRadius.ObjectAdjusterFloat=NewObjectAdjusterFloat("Radius",-0.5,0.5)

Global ObjectAdjusterTextureName.ObjectAdjusterString=NewObjectAdjusterString("TextureName")
Global ObjectAdjusterModelName.ObjectAdjusterString=NewObjectAdjusterString("ModelName")
Global ObjectAdjusterTextData0.ObjectAdjusterString=NewObjectAdjusterString("TextData0")
Global ObjectAdjusterTextData1.ObjectAdjusterString=NewObjectAdjusterString("TextData1")

Const CurrentObjectTargetIDCount=4
Dim CurrentObjectTargetID(CurrentObjectTargetIDCount-1)
Dim CurrentObjectTargetIDEnabled(CurrentObjectTargetIDCount-1)

Const PlayerActivateId=-2
Const CurrentObjectActivateIdCount=3
Dim CurrentObjectActivateId(CurrentObjectActivateIdCount-1)
Dim CurrentObjectActivateIdEnabled(CurrentObjectActivateIdCount-1)

Global IDFilterEnabled=False
Global IDFilterInverted=False
Global IDFilterAllow=-1

;Global TexturePrefix$=""

Global SimulationLevel=1
Const SimulationLevelMax=4
Const SimulationLevelAnimation=1
Const SimulationLevelMusic=4

;Dim BrushObjectXOffset#(1000),BrushObjectYOffset#(1000)
Dim BrushObjectTileXOffset(1000),BrushObjectTileYOffset(1000)

Global NofBrushObjects=0

Global BrushSpaceOriginX
Global BrushSpaceOriginY
Global BrushSpaceWidth=1
Global BrushSpaceHeight=1

; Object PRESETS
; ========================
Global CurrentObjectPresetCategory, NofObjectPresetCategories
Global CurrentObjectPresetObject, NofObjectPresetObjects
Dim ObjectPresetCategoryName$(1000)
Dim ObjectPresetObjectName$(1000)
Dir=ReadDir("Data\Editor\ObjectPresets")
file$=NextFile$(Dir)
While file$<>""
	If file$<>"." And file$<>".." And FileType("Data\Editor\ObjectPresets\"+file$)=2
		ObjectPresetCategoryName$(NofObjectPresetCategories)=file$
		NofObjectPresetCategories=NofObjectPresetCategories+1
	EndIf
	file$=NextFile$(Dir)
Wend

CloseDir dir
i=0
Repeat
	NofObjectPresetObjects=0
	Dir=ReadDir("Data\Editor\ObjectPresets\"+ObjectPresetCategoryName$(i))
	file$=NextFile$(Dir)
	While file$<>""
		If file$<>"." And file$<>".." And FileType("Data\Editor\ObjectPresets\"+ObjectPresetCategoryName$(i)+"\"+file$)=1 And Lower$(Right$(file$,4))=".wop"
			ObjectPresetObjectName$(NofObjectPresetObjects)=file$
			NofObjectPresetObjects=NofObjectPresetObjects+1
		EndIf
		file$=NextFile$(Dir)
	Wend
	CloseDir dir
	CurrentObjectPresetCategory=i
	i=i+1
Until NofObjectPresetObjects>0

;Dim ObjectAdjuster$(30)
Dim ObjectAdjuster$(60)
Global NofObjectAdjusters,ObjectAdjusterStart

; GLOBAL LEVELSETTINGS
; ========================
Global WaterFlow=1
Global WaterTransparent=True
Global WaterGlow=False

Global LightRed=255
Global LightGreen=255
Global LightBlue=255
Global AmbientRed=100
Global AmbientGreen=100
Global AmbientBlue=100

Global SimulatedLightRed,SimulatedLightGreen,SimulatedLightBlue,SimulatedLightRedGoal,SimulatedLightGreenGoal,SimulatedLightBlueGoal,SimulatedLightChangeSpeed
Global SimulatedAmbientRed,SimulatedAmbientGreen,SimulatedAmbientBlue,SimulatedAmbientRedGoal,SimulatedAmbientGreenGoal,SimulatedAmbientBlueGoal,SimulatedAmbientChangeSpeed

; ******************************************************

; Setup Graphics, Lights, Camera
; ================================
;If displayfullscreen=True
;	Graphics3D 800,600,16,1
;	SetBuffer BackBuffer()
;Else
;	Graphics3D 800,600,16,2
;	SetBuffer BackBuffer()
;	Graphics3D 800,600,16,3
;EndIf

Global NofMyGfxModes, GfxMode
Dim MyGfxModeWidth(1000),MyGfxModeHeight(1000),MyGfxModeDepth(1000)
Global GfxWidth,GfxHeight,GfxDepth,GfxWindowed
Global GfxZoomScaling#
Global TilePickerZoomScaling#

Const EditorDisplayFile$="displaymnikeditor.wdf"

Function ReadDisplayFile()

	file=ReadFile (globaldirname$+"\"+EditorDisplayFile$)
	If file>0

		NofMyGfxModes=ReadInt(file)
		For i=0 To NofMyGfxModes-1
			MyGfxModeWidth(i)=ReadInt(file)
			MyGfxModeHeight(i)=ReadInt(file)
			MyGfxModeDepth(i)=ReadInt(file)
		Next
		GfxMode=ReadInt(file)
		GfxWindowed=ReadInt(file)
		GfxWidth=MyGfxModeWidth(gfxmode)
		GfxHeight=MyGfxModeHeight(gfxmode)
		GfxDepth=MyGfxModeDepth(gfxmode)
	Else
		PopulateGfxModes()
		GfxWidth=800
		GfxHeight=600
		GfxWindowed=1
		GfxDepth=16
	EndIf

End Function

Function WriteDisplayFile()

	file=WriteFile(globaldirname$+"\"+EditorDisplayFile$)

	WriteInt file,NofMyGfxModes

	For i=0 To NofMyGfxModes-1
		WriteInt file,MyGfxModeWidth(i)
		WriteInt file,MyGfxModeHeight(i)
		WriteInt file,MyGfxModeDepth(i)

	Next

	WriteInt file,GfxMode
	WriteInt file,GfxWindowed

	CloseFile file

End Function

Function PopulateGfxModes()

	j=0
	For i=1 To CountGfxModes3D()
		ratio#=Float(GfxModeWidth(i))/Float(GfxModeHeight(i))
		If ratio#>1.33 And ratio#<1.34 And GfxModeWidth(i)>=640
			; list all 4:3 modes above 640x480
			MyGfxModeWidth(j)=GfxModeWidth(i)
			MyGfxModeHeight(j)=GfxModeHeight(i)
			MyGfxModeDepth(j)=GfxModeDepth(i)
			;IsWideScreen(j)=False
			j=j+1
		ElseIf ratio#>1.77 And ratio#<1.78 And GfxModeWidth(i)>=640 ;And widescreen=True
			MyGfxModeWidth(j)=GfxModeWidth(i)
			MyGfxModeHeight(j)=GfxModeHeight(i)
			MyGfxModeDepth(j)=GfxModeDepth(i)
			;IsWideScreen(j)=True
			j=j+1
		EndIf
	Next
	NofMyGfxModes=j

	GfxMode=-1

	If GfxMode=-1
		For j=0 To NofMyGfxModes-1
			If MyGfxModeWidth(j)=800 And MyGfxModeheight(j)=600 And MyGfxModeDepth(j)=32
				GfxMode=j
			EndIf
		Next
	EndIf
	If GfxMode=-1
		For j=0 To NofMyGfxModes-1
			If MyGfxModeWidth(j)=800 And MyGfxModeHeight(j)=600 And MyGfxModeDepth(j)=16
				GfxMode=j
			EndIf
		Next
	EndIf

End Function

ReadDisplayFile()

; something is wrong with the graphics mode: try different versions
If GfxMode3DExists (GfxWidth,GfxHeight,GfxDepth)=False
	GfxWidth=800
	GfxHeight=600
	GfxDepth=16
EndIf
If GfxMode3DExists (GfxWidth,GfxHeight,GfxDepth)=False
	GfxWidth=800
	GfxHeight=600
	GfxDepth=32
EndIf
If GfxMode3DExists (GfxWidth,GfxHeight,GfxDepth)=False
	GfxWidth=640
	GfxHeight=480
	GfxDepth=16
EndIf
If GfxMode3DExists (GfxWidth,GfxHeight,GfxDepth)=False
	GfxWidth=640
	GfxHeight=480
	GfxDepth=32
EndIf
If GfxMode3DExists (GfxWidth,GfxHeight,GfxDepth)=False
	Print "Unable to set graphics mode!"
	Print ""
	Print "Please ensure that your video card drivers"
	Print "are up-to-date, or use the graphic options"
	Print "to select a different display mode."
	Print ""
	Print "Exiting... press any key."
	WaitKey()
	End
EndIf

;widescreen
Global widescreen=False
Global wideicons=True
Global FitForWidescreenGlobal ;read from master.dat
Global FitForWidescreenGlobalHub ;reserved when starting an adventure in a custom hub
Global FitForWidescreen ;read from .wlv
ratio#=Float(GfxWidth)/Float(GfxHeight)

If ratio#>1.77 And ratio#<1.78 ;aspect ratio must be 16:9
	widescreen=True
EndIf

Const OriginalRatio#=800.0/600.0
Global GfxAspectRatio#

GfxWindowed=2 ; Force windowed mode

Graphics3D GfxWidth,GfxHeight,GfxDepth,GfxWindowed
SetBuffer BackBuffer()

If GfxWindowed=1
	displayfullscreen=True
Else
	displayfullscreen=False
EndIf

;ShowMessage("Graphics3D: "+GfxWidth+" x "+GfxHeight+" with depth "+GfxDepth,1000)

Const LettersCountX=44
Const LettersCountY=30

Const ToolbarHeight=100
Const SidebarWidth=300

Global ToolbarBrushModeX
Global ToolbarBrushModeY

Global ToolbarBrushSizeX
Global ToolbarBrushSizeY

Global ToolbarTexPrefixX
Global ToolbarTexPrefixY

Global ToolbarDensityX
Global ToolbarDensityY

Global ToolbarElevateX
Global ToolbarElevateY

Global ToolbarBrushWrapX
Global ToolbarBrushWrapY

Global ToolbarStepPerX
Global ToolbarStepPerY

Global ToolbarShowMarkersX
Global ToolbarShowMarkersY

Global ToolbarShowObjectsX
Global ToolbarShowObjectsY

Global ToolbarShowLogicX
Global ToolbarShowLogicY

Global ToolbarShowLevelX
Global ToolbarShowLevelY

Global ToolbarIDFilterX
Global ToolbarIDFilterY

Global ToolbarSimulationLevelX
Global ToolbarSimulationLevelY

Global ToolbarExitX
Global ToolbarExitY

Global ToolbarSaveX
Global ToolbarSaveY

Global LetterWidth#
Global LetterHeight#

Const CharWidth#=0.045
Const CharHeight#=0.05

Global LevelViewportWidth
Global LevelViewportHeight

Global SidebarX
Global SidebarY

Global FlStartX
Global FlStartY

Global LowerButtonsCutoff

;ahaha goodness...

Function LetterX(x#)

	Return GfxWidth/2+LetterWidth*(x#-LettersCountX/2)

End Function

Function LetterY(y#)

	Return LetterHeight*y#

End Function

Function LevelViewportX(x#)

	Return -(LevelViewportWidth-LevelViewportHeight)/2+x#

End Function

Global Light
Global SpotLight
AmbientLight 155,155,155

Global Camera1 ; level camera
Global Camera2
Global camera3
Global camera4 ; object camera
Global camera ; text screen camera
Global CameraParticle ; particle camera, duh

; the current projection mode for each camera
Global Camera1Proj=0
Global Camera2Proj=0
Global Camera3Proj=0
Global Camera4Proj=0
Global CameraProj=0
Global CameraParticleProj=0

Global CameraPanning=False
Global GameCamera=False ; whether "game camera mode" is active (simulates the in-game camera)

Global Camera1PerspectiveZoom#
Global Camera1OrthographicZoom#
Global Camera1Zoom#
Global Camera4Zoom#

Global Camera1StartY=6 ;/GfxZoomScaling# ;*GfxAspectRatio#
; saved when entering orthographic mode since orthographic mode mouse wheel scrolling does not change the height, unlike perspective mode mouse wheel scrolling
Global Camera1PerspectiveY#=Camera1StartY
Global Camera1SavedProjMode=1 ; the projection mode to return to after being in projection mode 0 (which means the camera is disabled)

s=CreateMesh()
su=CreateSurface(s)
AddVertex (su,-1.5,300,1.5)
AddVertex (su,1.5,300,1.5)
AddVertex (su,-1.5,300,-1.5)
AddVertex (su,1.5,300,-1.5)
AddTriangle (su,0,1,2)
AddTriangle (su,2,1,3)
EntityColor s,255,255,255
EntityAlpha s,0.5

; Create Meshes
; =================
; Cursor
Dim CursorMeshPillar(3)
Dim CursorMeshOpaque(3)
Global CursorMeshTexturePicker

Global BrushMesh
Global BrushSurface
Global BrushSurfaceVertexCount=0

Global BrushTextureMesh
Global BrushTextureSurface

Const BrushMeshAlpha#=0.3
Const BrushMeshObjectAlpha#=0.5
Const BrushMeshOffsetY#=0.01

Function ClearBrushSurface()

	ClearSurface BrushSurface

	ClearBrushPreviewSurface()

End Function

Function ClearBrushPreviewSurface()

	For i=0 To NofPreviewObjects-1
		FreeEntity PreviewObjects(i)
	Next
	NofPreviewObjects=0

	ClearSurface BrushTextureSurface
	BrushSurfaceVertexCount=0

End Function

Function ShowBrushSurface()

	ShowEntity BrushMesh
	ShowEntity BrushTextureMesh

End Function

Function HideBrushSurface()

	HideEntity BrushMesh
	HideEntity BrushTextureMesh

End Function

Function FinishBrushSurface()

	UpdateNormals BrushMesh
	UpdateNormals BrushTextureMesh

End Function

Function AddSquareToBrushSurface(TheSurface,i,j,y#,SetTexCoords)

	; Stupid hack to prevent MAVs from too many vertices at immense brush sizes.
	; It's meant to be a temporary solution but I might also just keep this forever. That's the nature of software engineering.
	If BrushSurfaceVertexCount=32000
		Return
	EndIf

	StartingVertex=BrushSurfaceVertexCount

	AddVertex TheSurface,i,y#+BrushMeshOffsetY#,-j
	AddVertex TheSurface,i+1,y#+BrushMeshOffsetY#,-j
	AddVertex TheSurface,i,y#+BrushMeshOffsetY#,-j-1
	AddVertex TheSurface,i+1,y#+BrushMeshOffsetY#,-j-1

	AddTriangle TheSurface,StartingVertex+0,StartingVertex+1,StartingVertex+2
	AddTriangle TheSurface,StartingVertex+1,StartingVertex+3,StartingVertex+2

	If SetTexCoords
		TheTile.Tile=BrushTiles(LevelSpaceToBrushSpaceX(i,BrushWrap),LevelSpaceToBrushSpaceY(j,BrushWrap))
		CalculateUV(TheTile\Terrain\Texture,0,0,TheTile\Terrain\Rotation,8,1)
		VertexTexCoords(TheSurface,StartingVertex+0,ChunkTileU#,ChunkTileV#)
		CalculateUV(TheTile\Terrain\Texture,1,0,TheTile\Terrain\Rotation,8,1)
		VertexTexCoords(TheSurface,StartingVertex+1,ChunkTileU#,ChunkTileV#)
		CalculateUV(TheTile\Terrain\Texture,0,1,TheTile\Terrain\Rotation,8,1)
		VertexTexCoords(TheSurface,StartingVertex+2,ChunkTileU#,ChunkTileV#)
		CalculateUV(TheTile\Terrain\Texture,1,1,TheTile\Terrain\Rotation,8,1)
		VertexTexCoords(TheSurface,StartingVertex+3,ChunkTileU#,ChunkTileV#)
	EndIf

	BrushSurfaceVertexCount=BrushSurfaceVertexCount+4

End Function

Function AddTileToBrushSurfaceActual(TheSurface,x,y,BrushSpaceX,BrushSpaceY,SetTexCoords)

	AddSquareToBrushSurface(TheSurface,x,y,0.0,SetTexCoords)

	If IsPositionInLevel(x,y)
		SquareHeight#=GetTileTotalHeight(LevelTiles(x,y))
		If SquareHeight#<>0.0
			AddSquareToBrushSurface(TheSurface,x,y,SquareHeight#,SetTexCoords)
		EndIf
	EndIf

End Function

Function AddTileToBrushSurface(TheSurface,x,y,BrushSpaceX,BrushSpaceY,SetTexCoords)

	If BrushMode=BrushModeSetMirror
		Return
	EndIf

	BrushSpaceX=LevelSpaceToBrushSpaceX(x,BrushWrap)
	BrushSpaceY=LevelSpaceToBrushSpaceY(y,BrushWrap)

	AddTileToBrushSurfaceActual(TheSurface,x,y,BrushSpaceX,BrushSpaceY,SetTexCoords)

	If DupeMode=DupeModeX
		TargetX=MirrorAcrossInt(x,MirrorPositionX)
		AddTileToBrushSurfaceActual(TheSurface,TargetX,y,BrushSpaceX,BrushSpaceY,SetTexCoords)
	ElseIf DupeMode=DupeModeY
		TargetY=MirrorAcrossInt(y,MirrorPositionY)
		AddTileToBrushSurfaceActual(TheSurface,x,TargetY,BrushSpaceX,BrushSpaceY,SetTexCoords)
	ElseIf DupeMode=DupeModeXPlusY
		TargetX=MirrorAcrossInt(x,MirrorPositionX)
		TargetY=MirrorAcrossInt(y,MirrorPositionY)
		AddTileToBrushSurfaceActual(TheSurface,TargetX,y,BrushSpaceX,BrushSpaceY,SetTexCoords)
		AddTileToBrushSurfaceActual(TheSurface,x,TargetY,BrushSpaceX,BrushSpaceY,SetTexCoords)
		AddTileToBrushSurfaceActual(TheSurface,TargetX,TargetY,BrushSpaceX,BrushSpaceY,SetTexCoords)
	EndIf

End Function

Function AddTileToBrushPreview(x,y)

	BrushSpaceX=LevelSpaceToBrushSpaceX(x,BrushWrap)
	BrushSpaceY=LevelSpaceToBrushSpaceY(y,BrushWrap)

	If EditorMode=0
		AddTileToBrushSurface(BrushTextureSurface,x,y,BrushSpaceX,BrushSpaceY,True)
	ElseIf EditorMode=3
		For k=0 To NofBrushObjects-1
			If BrushObjectTileXOffset(k)=BrushSpaceX And BrushObjectTileYOffset(k)=BrushSpaceY
				If NofPreviewObjects<MaxNofObjects-NofObjects
					Preview=CopyEntity(BrushObjects(k)\Model\Entity)
					SetEntityAlphaWithModelName(Preview,BrushMeshObjectAlpha#,BrushObjects(k)\Attributes\ModelName$)
					PositionEntityWithXYZAdjust(Preview,x+0.5,y+0.5,BrushObjects(k)\Position\Z,BrushObjects(k)\Attributes)
					PreviewObjects(NofPreviewObjects)=Preview
					NofPreviewObjects=NofPreviewObjects+1
				EndIf
			EndIf
		Next
	EndIf

End Function

; MousePlane
;Const MouseSurfaceMinX=0
;Const MouseSurfaceMinY=0
;Const MouseSurfaceMaxX=100
;Const MouseSurfaceMaxY=100
Const MouseSurfaceMinX=-100
Const MouseSurfaceMinY=-100
Const MouseSurfaceMaxX=200
Const MouseSurfaceMaxY=200

Global MousePlane=CreateMesh()
MouseSurface=CreateSurface(MousePlane)
AddVertex MouseSurface,MouseSurfaceMinX,0,-MouseSurfaceMinY ;0,0,0
AddVertex MouseSurface,MouseSurfaceMaxX,0,-MouseSurfaceMinY ;100,0,0
AddVertex MouseSurface,MouseSurfaceMinX,0,-MouseSurfaceMaxY ;0,0,-100
AddVertex MouseSurface,MouseSurfaceMaxX,0,-MouseSurfaceMaxY ;100,0,-100
AddTriangle MouseSurface,0,1,2
AddTriangle MouseSurface,1,3,2
EntityPickMode MousePlane,2
EntityAlpha MousePlane,0
; TexturePlane
Global TexturePlane
; CurrentTile
CurrentMesh=CreateMesh()
CurrentSurface=CreateSurface(CurrentMesh)
; CurrentWaterTile
Global CurrentWaterTile
Global CurrentWaterTileSurface

; CurrentObjectMarker
Global CurrentObjectMarkerMesh

; ObjectPositionMarker
Global ObjectPositionMarkerMesh=CreateCube()
ScaleMesh ObjectPositionMarkerMesh,0.08,90,0.08
;EntityAlpha ObjectPositionMarkerMesh,.3
;EntityColor ObjectPositionMarkerMesh,100,255,100
HideEntity ObjectPositionMarkerMesh

PopulateGlobalGraphics(0)

Global hubmode
Global HubFileName$, HubTitle$, HubDescription$, HubTotalAdventures, HubAdvStart, HubSelectedAdventure
Const HubAdvMax=MaxCompilerFile ;500
Dim HubAdventuresFilenames$(HubAdvMax)
Dim HubAdventuresMissing(HubAdvMax)
Dim HubAdventuresIncludeInTotals(HubAdvMax)

Global NoOfShards=7
Global CustomShardEnabled
Dim CustomShardCMD(NoOfShards,5)
;Dim CustomShardData1(NoOfShards)
;Dim CustomShardData2(NoOfShards)
;Dim CustomShardData3(NoOfShards)
;Dim CustomShardData4(NoOfShards)

Global NoOfGlyphs=5
Global CustomGlyphEnabled
Dim CustomGlyphCMD(NoOfGlyphs,5)
;Dim CustomGlyphData1(NoOfGlyphs)
;Dim CustomGlyphData2(NoOfGlyphs)
;Dim CustomGlyphData3(NoOfGlyphs)
;Dim CustomGlyphData4(NoOfGlyphs)

Global SelectedShard
Global SelectedGlyph

Global MaxParticleWarningTimer=0 ; number of frames remaining before the "too many particles" warning message disappears