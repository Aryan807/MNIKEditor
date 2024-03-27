Function LoadLevel(levelnumber)

	SetCurrentLevelNumber(levelnumber)

	resetlevel()

	; clear current objects first
	;ShowMessage("Freeing " + NofObjects + " objects...", 1000)
	For i=0 To NofObjects-1
		;DeleteObject(i)
		FreeObject(i)
	Next

	file=ReadFile (GetAdventureDir$()+levelnumber+".wlv")

	LevelWidth=-999
	; This loop will bypass the protection on MOFI and WA3 Beta1 level files.
	; MOFI levels have only one extra -999 integer.
	While LevelWidth=-999
		LevelWidth=ReadInt(File)
	Wend

	If LevelWidth>121
		; WA3 VAULTS
		LevelWidth=LevelWidth-121
	EndIf

	LevelHeight=ReadInt(File)
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			LevelTiles(i,j)\Terrain\Texture=ReadInt(file) ; corresponding to squares in LevelTexture
			LevelTiles(i,j)\Terrain\Rotation=ReadInt(file) ; 0-3 , and 4-7 for "flipped"
			LevelTiles(i,j)\Terrain\SideTexture=ReadInt(file) ; texture for extrusion walls
			LevelTiles(i,j)\Terrain\SideRotation=ReadInt(file) ; 0-3 , and 4-7 for "flipped"
			LevelTiles(i,j)\Terrain\Random=ReadFloat(file) ; random height pertubation of tile
			LevelTiles(i,j)\Terrain\Height=ReadFloat(file) ; height of "center" - e.g. to make ditches and hills
			LevelTiles(i,j)\Terrain\Extrusion=ReadFloat(file); extrusion with walls around it
			LevelTiles(i,j)\Terrain\Rounding=ReadInt(file); 0-no, 1-yes: are floors rounded if on a drop-off corner
			LevelTiles(i,j)\Terrain\EdgeRandom=ReadInt(file); 0-no, 1-yes: are edges rippled
			LevelTiles(i,j)\Terrain\Logic=ReadInt(file)

			LevelTileObjectCount(i,j)=0

		Next
	Next
	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			LevelTiles(i,j)\Water\Texture=ReadInt(file)
			LevelTiles(i,j)\Water\Rotation=ReadInt(file)
			LevelTiles(i,j)\Water\Height=ReadFloat(file)
			LevelTiles(i,j)\Water\Turbulence=ReadFloat(file)
		Next
	Next
	WaterFlow=ReadInt(file)
	WaterTransparent=ReadInt(File)
	WaterGlow=ReadInt(File)

	currentleveltexture=-1
	currentwatertexture=-1
	a$=ReadString$(file)
	For i=0 To nofleveltextures-1
		If a$=levelTextureName$(i) Then CurrentLevelTexture=i
	Next
	If currentleveltexture=-1
		LevelTextureCustomName$=a$
	EndIf

	a$=ReadString$(file)
	For i=0 To nofwatertextures-1
		If a$=waterTextureName$(i) Then CurrentwaterTexture=i
	Next
	If currentwatertexture=-1
		WaterTextureCustomName$=a$
	EndIf

	FreeTexture leveltexture
	FreeTexture watertexture
	leveltexture=0
	If currentleveltexture=-1
		LevelTexture=LoadTexture(globaldirname$+"\custom\leveltextures\leveltex "+LevelTextureCustomName$+".bmp",1)
		If leveltexture=0
			Locate 0,0
			Color 0,0,0
			Rect 0,0,500,40,True
			Color 255,255,0
			Print "CUSTOM TEXTURE NOT FOUND... REVERTING."
			Delay 2000
			currentleveltexture=1
			LevelTexture=myLoadTexture("data\Leveltextures\"+LevelTexturename$(CurrentLevelTexture),1)
		EndIf
	Else
		LevelTexture=myLoadTexture("data\Leveltextures\"+LevelTexturename$(CurrentLevelTexture),1)
	EndIf

	watertexture=0
	If currentwatertexture=-1
		WaterTexture=LoadTexture(globaldirname$+"\custom\leveltextures\watertex "+WaterTextureCustomName$+".jpg",2)
		If Watertexture=0
			Locate 0,0
			Color 0,0,0
			Rect 0,0,500,40,True
			Color 255,255,0
			Print "CUSTOM WATERTEXTURE NOT FOUND... REVERTING."
			Delay 2000
			currentwatertexture=0
			WaterTexture=myLoadTexture("data\Leveltextures\"+WaterTexturename$(CurrentWaterTexture),1)
		EndIf
	Else
		waterTexture=myLoadTexture("data\Leveltextures\"+waterTexturename$(CurrentWaterTexture),1)
	EndIf

	leftmousereleased=False

	SetNofObjects(0)
	ReadObjectCount=ReadInt(file)

	For i=0 To ReadObjectCount-1
		LoadObjectAttributes(file,i)

		If WA1Format=False
			For k=0 To 30
				;ObjectAdjusterString$(Dest,k)=ReadString(file)
				ReadString(file)
			Next
		EndIf

		BuildLevelObjectModel(i)

		SetNofObjects(NofObjects+1)

		CreateObjectPositionMarker(i)
	Next

	; finalize object data
	PlayerIndex=NofObjects
	For j=0 To NofObjects-1
		LevelObject.GameObject=LevelObjects(j)
		LevelObject\Attributes\Linked=ObjectIndexGameToEditor(LevelObject\Attributes\Linked, PlayerIndex)
		LevelObject\Attributes\LinkBack=ObjectIndexGameToEditor(LevelObject\Attributes\LinkBack, PlayerIndex)
		LevelObject\Attributes\Parent=ObjectIndexGameToEditor(LevelObject\Attributes\Parent, PlayerIndex)
		LevelObject\Attributes\Child=ObjectIndexGameToEditor(LevelObject\Attributes\Child, PlayerIndex)
	Next

	ObjectsWereChanged()

	LevelEdgeStyle=ReadInt(file)

	LightRed=ReadInt(file)
	LightGreen=ReadInt(file)
	LightBlue=ReadInt(file)

	AmbientRed=ReadInt(file)
	AmbientGreen=ReadInt(file)
	AmbientBlue=ReadInt(file)

	LevelMusic=ReadInt(file)
	LevelWeather=ReadInt(file)

	LightingWasChanged()

	ReBuildLevelModel()

	UpdateLevelTexture()
	UpdateWaterTexture()

	BuildCurrentTileModel()

	For j=0 To LevelHeight-1
		For i=0 To LevelWidth-1
			UpdateTile(i,j)
		Next
	Next

	OpenedLevel()

	If Not Eof(file)
		ReadString(file)
	EndIf

	If Not Eof(file)
		ReadInt(file)
	EndIf

	If Not Eof(file)
		WidescreenRangeLevel=ReadInt(file)
		AddUnsavedChange()
	EndIf

	CloseFile file

End Function