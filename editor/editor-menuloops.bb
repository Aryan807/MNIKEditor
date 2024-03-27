Function MasterMainLoop()

	If CtrlDown() And KeyDown(20) ; Ctrl+T
		StartTestMode()
	EndIf

	If HotkeySave()
		SaveMasterFile()
	EndIf

	dialogtimer=dialogtimer+1
	adj=1
	If KeyDown(42) Or KeyDown(54) Then adj=10

	DisplayText2("Adventure File Name: ",0,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2(AdventureFileName$,0,1,255,255,255)
	If MouseY()<LetterHeight And MouseX()>LetterX(24) And MouseX()<LetterX(38) ; x: 430 to 700
		DisplayText2("                        (Adv. Options)",0,0,255,255,255)
	Else
		DisplayText2("                        (Adv. Options)",0,0,TextMenusR,TextMenusG,TextMenusB)
	EndIf

	WlvColumnLeft=LetterX(38.5)

	If MOdified>=0

		For i=0 To 37

			AddLetter(Asc("X")-32,-.97+i*.045,.5-0*.05,1,0,.04,0,0,0,0,0,0,0,0,0,TextMenuXR,TextMenuXG,TextMenuXB)
			For j=0 To 4
				AddLetter(Asc("X")-32,-.97+i*.045,.5-(3+j)*.05,1,0,.04,0,0,0,0,0,0,0,0,0,TextMenuXR,TextMenuXG,TextMenuXB)
			Next

		Next

		For i=0 To 25
			DisplayText2(":",38,i,TextMenusR,TextMenusG,TextMenusB)
		Next
		DisplayText2("EDIT",39.5,0,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("LV DG",39,1,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("-----",39,2,TextMenusR,TextMenusG,TextMenusB)

		If MouseX()>WlvColumnLeft And MouseX()<LetterX(41.5) And MouseY()>LetterHeight*3 And MouseY()<LetterHeight*4 ; X: 700 to 750
			DisplayText2("-",39.5,3,TextMenusR,TextMenusG,TextMenusB)
		Else
			DisplayText2("-",39.5,3,TextMenuXR,TextMenuXG,TextMenuXB)
		EndIf
		If MouseX()>LetterX(41.5) And MouseX()<GfxWidth And MouseY()>LetterHeight*3 And MouseY()<LetterHeight*4
			DisplayText2("-",42.5,3,TextMenusR,TextMenusG,TextMenusB)
		Else
			DisplayText2("-",42.5,3,TextMenuXR,TextMenuXG,TextMenuXB)
		EndIf

		DigitSpaceMult#=0.8
		For i=1 To 20
			flag=False
			If MouseX()>WlvColumnLeft And MouseX()<LetterX(41.5) And MouseY()>LetterHeight*3+i*LetterHeight And MouseY()<=LetterHeight*4+i*LetterHeight
				flag=True
			EndIf

			If i+MasterLevelListStart<10
				ex$="00"+Str$(i+MasterLevelListStart)
			Else If i+MasterLevelListStart<100
				ex$="0"+Str$(i+MasterLevelListStart)
			Else
				ex$=Str$(i+MasterLevelListStart)
			EndIf

			If flag=True ; mouse over
				r=255
				g=255
				b=100
			Else If MasterLevelList(i+MasterLevelListStart)=0
				r=100
				g=100
				b=100
			Else If CopyingLevel=StateCopying And CopiedLevel=i+MasterLevelListStart
				r=0
				g=255
				b=255
			Else If CopyingLevel=StateSwapping And CopiedLevel=i+MasterLevelListStart
				r=255
				g=0
				b=255
			Else
				r=210
				g=210
				b=210
			EndIf
			DisplayText2(ex$,38.7,3+i,r,g,b,DigitSpaceMult) ; previously, x=39

			flag=False
			If MouseX()>LetterX(41.5) And MouseX()<GfxWidth And MouseY()>LetterHeight*3+i*LetterHeight And MouseY()<=LetterHeight*4+i*LetterHeight
				flag=True
			EndIf

			If i+MasterDialogListStart<10
				ex$="00"+Str$(i+MasterDialogListStart)
			Else If i+MasterDialogListStart<100
				ex$="0"+Str$(i+MasterDialogListStart)
			Else
				ex$=Str$(i+MasterDialogListStart)
			EndIf

			If flag=True
				r=255
				g=255
				b=100
			Else If MasterDialogList(i+MasterDialogListStart)=0
				r=100
				g=100
				b=100
			Else If CopyingDialog=StateCopying And CopiedDialog=i+MasterDialogListStart
				r=0
				g=255
				b=255
			Else If CopyingDialog=StateSwapping And CopiedDialog=i+MasterDialogListStart
				r=255
				g=0
				b=255
			Else
				r=210
				g=210
				b=210
			EndIf
			DisplayText2(ex$,41.8,3+i,r,g,b,DigitSpaceMult) ; previously, x=41.5

		Next

		If MouseX()>WlvColumnLeft And MouseX()<LetterX(41.5) And MouseY()>LetterHeight*24 And MouseY()<LetterHeight*25
			DisplayText2("+",39.5,24,TextMenusR,TextMenusG,TextMenusB)
		Else
			DisplayText2("+",39.5,24,TextMenuXR,TextMenuXG,TextMenuXB)
		EndIf
		If MouseX()>LetterX(41.5) And MouseX()<GfxWidth And MouseY()>LetterHeight*24 And MouseY()<LetterHeight*25
			DisplayText2("+",42.5,24,TextMenusR,TextMenusG,TextMenusB)
		Else
			DisplayText2("+",42.5,24,TextMenuXR,TextMenuXG,TextMenuXB)
		EndIf

		displayText2("-----",39,25,TextMenusR,TextMenusG,TextMenusB)

		DisplayText2("--------------------------------------",0,2,TextMenusR,TextMenusG,TextMenusB)

		DisplayText2("Adventure Title:",0,3,TextMenusR,TextMenusG,TextMenusB)

		DisplayText2(AdventureTitle$,0,4,255,255,255)
		DisplayText2("--------------------------------------",0,5,TextMenusR,TextMenusG,TextMenusB)

		DisplayText2("Introductory Text:",0,6,TextMenusR,TextMenusG,TextMenusB)
		For i=0 To 4
			DisplayText2(AdventureTextline$(i),0,7+i,255,255,255)
		Next
		DisplayText2("--------------------------------------",0,12,TextMenusR,TextMenusG,TextMenusB)

		DisplayText2("Starting Coord. (Lv 01)",0,13,TextMenusR,TextMenusG,TextMenusB)
		displaytext2("X:      Y:      Dir:",0,14,TextMenusR,TextMenusG,TextMenusB)
		displaytext2(Str$(adventurestartx),2,14,255,255,255)
		displaytext2(Str$(adventurestarty),10,14,255,255,255)
		;displaytext2(Str$((adventurestartdir+180) Mod 360),20,14,255,255,255)
		displaytext2(Str$(adventurestartdir),20,14,255,255,255)

		DisplayText2("--------------------------------------",0,15,TextMenusR,TextMenusG,TextMenusB)

		DisplayText2("Winning Condition:",0,16,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2(WinningCondition$(AdventureGoal),0,17,255,255,255)
		DisplayText2("--------------------------------------",0,18,TextMenusR,TextMenusG,TextMenusB)

		DisplayText2(":Gate/Keys",25,13,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2(":Version #",25,14,255,255,255)
		DisplayText2(Str$(GateKeyVersion),35,14,255,255,255)

		DisplayText2(":Custom Icons",25,16,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2(":",25,17,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2(Left$(CustomIconName$,12),26,17,255,255,255)
	; PUT BACK IN FOR ME

		If MASTERUSER=True
			DisplayText2("Hub Commands:",0,19,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2("WonExit Lv    X    Y",0,20,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(Str$(adventureexitwonlevel),10,20,255,255,255)
			DisplayText2(Str$(adventureexitwonx),15,20,255,255,255)
			DisplayText2(Str$(adventureexitwony),20,20,255,255,255)
			DisplayText2("LostExt Lv    X    Y",0,21,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(Str$(adventureexitlostlevel),10,21,255,255,255)
			DisplayText2(Str$(adventureexitlostx),15,21,255,255,255)
			DisplayText2(Str$(adventureexitlosty),20,21,255,255,255)
			For i=0 To 2
				Displaytext2("WonCMD: Lv    C    D    D    D    D",0,22+i,TextMenusR,TextMenusG,TextMenusB)
				For j=0 To 5
					Displaytext2(Str$(adventurewoncommand(i,j)),10+j*5,22+i,255,255,255)
				Next
			Next
		EndIf
		DisplayText2("--------------------------------------",0,25,TextMenusR,TextMenusG,TextMenusB)

		DisplayText2("========== ========== ==========",0.5,26,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2(":        : :        : :        :",0.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2(":        : :        : :        :",0.5,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("========== ========== ==========",0.5,29,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

		If hubmode
			DisplayText2("                                 ==========",0.5,26,50,50,0)
			DisplayText2("                                 :        :",0.5,27,50,50,0)
			DisplayText2("                                 :        :",0.5,28,50,50,0)
			DisplayText2("                                 ==========",0.5,29,50,50,0)
		Else
			DisplayText2("                                 ==========",0.5,26,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
			DisplayText2("                                 :        :",0.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
			DisplayText2("                                 :        :",0.5,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
			DisplayText2("                                 ==========",0.5,29,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		EndIf

	If MouseY()>LowerButtonsCutoff And MouseX()<LetterX(11)
		DisplayText2("CANCEL",2.5,27,255,255,255)
		DisplayText2("+EXIT",3,28,255,255,255)
	Else
		DisplayText2("CANCEL",2.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",3,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	EndIf

	If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(11) And MouseX()<LetterX(22)
		DisplayText2(" SAVE",13.5,27,255,255,255)
		DisplayText2("+EXIT",14,28,255,255,255)
	Else
		DisplayText2(" SAVE",13.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",14,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

	EndIf

	If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(22) And MouseX()<LetterX(33)
		DisplayText2(" SAVE",24.5,27,255,255,255)
		DisplayText2("+TEST",25,28,255,255,255)
	Else
		DisplayText2(" SAVE",24.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+TEST",25,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

	EndIf
	If hubmode
		DisplayText2("COMPILE",35,27,50,50,0)
		DisplayText2("+EXIT",36,28,50,50,0)
	Else
		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(33)
			DisplayText2("COMPILE",35,27,255,255,255)
			DisplayText2("+EXIT",36,28,255,255,255)
		Else
			DisplayText2("COMPILE",35,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
			DisplayText2("+EXIT",36,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		EndIf
	EndIf

	AdventureExitButtonsMinX=LetterX(24)
	AdventureExitButtonsMaxX=LetterX(38)
	AdventureExitButtonsStartY=LetterHeight*20
	AdventureExitButtonsGapY=LetterHeight ;20
	If MouseX()>AdventureExitButtonsMinX And MouseX()<AdventureExitButtonsMaxX And MouseY()>AdventureExitButtonsStartY And MouseY()<AdventureExitButtonsStartY+AdventureExitButtonsGapY
		r=255
		g=255
		b=255

		If LeftMouse
			adventureexitwonlevel=adventureexitlostlevel
			adventureexitwonx=adventureexitlostx
			adventureexitwony=adventureexitlosty
		EndIf
	Else
		r=TextMenuButtonR
		g=TextMenuButtonG
		b=TextMenuButtonB
	EndIf
	DisplayText2("Set To LostExt",24,20,r,g,b)

	If MouseX()>AdventureExitButtonsMinX And MouseX()<AdventureExitButtonsMaxX And MouseY()>AdventureExitButtonsStartY+AdventureExitButtonsGapY And MouseY()<AdventureExitButtonsStartY+AdventureExitButtonsGapY*2
		r=255
		g=255
		b=255

		If LeftMouse
			adventureexitlostlevel=adventureexitwonlevel
			adventureexitlostx=adventureexitwonx
			adventureexitlosty=adventureexitwony
		EndIf
	Else
		r=TextMenuButtonR
		g=TextMenuButtonG
		b=TextMenuButtonB
	EndIf
	DisplayText2("Set To WonExit",24,21,r,g,b)

		; Mouse
		MouseTextEntryTrackMouseMovement()
		; Mouse Pos
		Entering=0

		x=GetMouseLetterX()
		y=(MouseY()-LetterHeight*5)/LetterHeight

		debug1=MouseY()
		debug2=y

		; cursor
		If x<38 And MouseY()>=LetterHeight*4 And y>2 And y<8
			Entering=1
			If x>Len(AdventureTextLine$(y-3)) Then x=Len(AdventureTextLine$(y-3))
			If DialogTimer Mod 50 <25 Or OldX<>x Or OldY<>y
				AddLetter(Asc("_")-32,-.97+x*.045,.5-y*.05,1,0,.05,0,0,0,0,0,0,0,0,0,255,255,255)
			EndIf
		EndIf
		If x<38 And y=0
			Entering=2
			If x>Len(AdventureTitle$) Then x=Len(AdventureTitle$)
			If DialogTimer Mod 50 <25 Or OldX<>x Or OldY<>y
				AddLetter(Asc("_")-32,-.97+x*.045,.5-y*.05,1,0,.05,0,0,0,0,0,0,0,0,0,255,255,255)
			EndIf
		EndIf
	; PUT BACK IN FOR ME - (HELP LINES _ DON"T NEED)
	;	If x<10 And y>9 And y<13
	;		Entering=3
	;		If x>Len(AdventureHelpLine$(y-10)) Then x=Len(AdventureHelpLine$(y-10))
	;		If DialogTimer Mod 50 <25 Or OldX<>x Or OldY<>y
	;			AddLetter(Asc("_")-32,-.97+x*.045,.5-y*.05,1,0,.05,0,0,0,0,0,0,0,0,0,255,255,255)
	;		EndIf
	;	EndIf

		OldX=x
		OldY=y
		; entering text
		let=GetKey()
		If Entering>0

			Select entering
			Case 1
				tex$=AdventureTextline$(y-3)
			Case 2
				tex$=AdventureTitle$
	; PUT BACK IN FOR ME - HELP LINE (DON"T NEED)
	;		Case 3
	;			tex$=AdventureHelpLine$(y-10)

			End Select

			tex$=MouseTextEntry$(tex$,let,x,y,0,0)

			Select entering
			Case 1
				AdventureTextline$(y-3)=tex$
			Case 2
				AdventureTitle$=tex$
		;	Case 3
		;		AdventureHelpLine$(y-10)=tex$

			End Select

		EndIf

	EndIf

	mb=0
	If LeftMouse mb=1
	If RightMouse mb=2
	If MouseDown(3) mb=3

	DelayTime=10

	; level list start
	If MouseX()>WlvColumnLeft And MouseX()<LetterX(41.5)
		If MouseDebounceFinished()
			If (MouseY()>LetterHeight*24 And MouseY()<LetterHeight*25 And mb>0) Or MouseScroll<0
				MasterLevelListStart=MasterLevelListStart+adj
				If MasterLevelListStart>MaxLevel-20 Then MasterLevelListStart=MaxLevel-20
				If MouseScroll=0 Then MouseDebounceSet(DelayTime)
			Else If (MouseY()>LetterHeight*3 And MouseY()<LetterHeight*4 And mb>0) Or MouseScroll>0
				MasterLevelListStart=MasterLevelListStart-adj
				If MasterLevelListStart<0 Then MasterLevelListStart=0
				If MouseScroll=0 Then MouseDebounceSet(DelayTime)
			EndIf
		EndIf
		If KeyPressed(201) ; page up
			MasterLevelListStart=MasterLevelListStart-20
			If MasterLevelListStart<0 Then MasterLevelListStart=0
		EndIf
		If KeyPressed(209) ; page down
			MasterLevelListStart=MasterLevelListStart+20
			If MasterLevelListStart>MaxLevel-20 Then MasterLevelListStart=MaxLevel-20
		EndIf
	EndIf

	; dialog list start
	If MouseX()>LetterX(41.5) And MouseX()<GfxWidth
		If MouseDebounceFinished()
			If (MouseY()>LetterHeight*24 And MouseY()<LetterHeight*25 And mb>0) Or MouseScroll<0
				MasterDialogListStart=MasterDialogListStart+adj
				If MasterDialogListStart>MaxDialog-20 Then MasterDialogListStart=MaxDialog-20
				If MouseScroll=0 Then MouseDebounceSet(DelayTime)
			Else If (MouseY()>LetterHeight*3 And MouseY()<LetterHeight*4 And mb>0) Or MouseScroll>0
				MasterDialogListStart=MasterDialogListStart-adj
				If MasterDialogListStart<0 Then MasterDialogListStart=0
				If MouseScroll=0 Then MouseDebounceSet(DelayTime)
			EndIf
		EndIf
		If KeyPressed(201) ; page up
			MasterDialogListStart=MasterDialogListStart-20
			If MasterDialogListStart<0 Then MasterDialogListStart=0
		EndIf
		If KeyPressed(209) ; page down
			MasterDialogListStart=MasterDialogListStart+20
			If MasterDialogListStart>MaxDialog-20 Then MasterDialogListStart=MaxDialog-20
		EndIf
	EndIf

	If mb>0

		;new advanced mode 2019
		If MouseY()<LetterHeight And  MouseX()>LetterX(24) And MouseX()<WlvColumnLeft
			SetEditorMode(10)
			Repeat
			Until LeftMouseDown()=0 And RightMouseDown()=0
		EndIf

	EndIf

	; Change Adventure

	; change textures

	; startpos
	If MouseY()>LetterHeight*14 And MouseY()<LetterHeight*15
		If MouseX()>LetterX(0) And MouseX()<LetterX(7)
			adventurestartx=AdjustInt("Adventure start X: ", adventurestartx, 1, 10, DelayTime)
		EndIf
		If MouseX()>LetterX(8) And MouseX()<LetterX(15)
			adventurestarty=AdjustInt("Adventure start Y: ", adventurestarty, 1, 10, DelayTime)
		EndIf
		If MouseX()>LetterX(16) And MouseX()<LetterX(23)
			adventurestartdir=AdjustInt("Adventure start direction: ", adventurestartdir, 45, 45, DelayTime)
			adventurestartdir=adventurestartdir Mod 360
			; This If block is necessary because Blitz3D's Mod can return negative numbers.
			If adventurestartdir<0
				adventurestartdir=adventurestartdir+360
			EndIf

			ShowTooltipCenterAligned(LetterX(19.5),LetterHeight*14,GetDirectionString$(adventurestartdir))
		EndIf
	EndIf

	; PUT BACK IN FOR ME
	If MASTERUSER=True
		; change hub data
		For i=0 To 5
			For j=0 To 4
				XMin=LetterX(7.5+i*5)
				XMax=LetterX(12.5+i*5)
				YMin=LetterHeight*18+(j+2)*LetterHeight
				YMax=LetterHeight*19+(j+2)*LetterHeight
				TooltipX=XMin+LetterWidth*2.5
				TooltipY=YMin
				If MouseX()>XMin And MouseX()<XMax And MouseY()>YMin And MouseY()<YMax
					If j=0 And i=0
						Adventureexitwonlevel=AdjustInt("Adventure exit won level: ", Adventureexitwonlevel, 1, 10, DelayTime)
					EndIf
					If j=0 And i=1
						Adventureexitwonx=AdjustInt("Adventure exit won X: ", Adventureexitwonx, 1, 10, DelayTime)
					EndIf
					If j=0 And i=2
						Adventureexitwony=AdjustInt("Adventure exit won Y: ", Adventureexitwony, 1, 10, DelayTime)
					EndIf
					If j=1 And i=0
						Adventureexitlostlevel=AdjustInt("Adventure exit lost level: ", Adventureexitlostlevel, 1, 10, DelayTime)
					EndIf
					If j=1 And i=1
						Adventureexitlostx=AdjustInt("Adventure exit lost X: ", Adventureexitlostx, 1, 10, DelayTime)
					EndIf
					If j=1 And i=2
						Adventureexitlosty=AdjustInt("Adventure exit lost Y: ", Adventureexitlosty, 1, 10, DelayTime)
					EndIf
					If j=2 Or j=3 Or j=4
						Select i
						Case 0
							cmdbit$="level"
						Case 1
							cmdbit$="command"
							ShowTooltipCenterAligned(TooltipX,TooltipY,GetCommandName$(AdventureWonCommand(j-2,1)))
						Case 2
							cmdbit$="Data1"
							ShowTooltipCenterAligned(TooltipX,TooltipY,GetCMDData1NameAndValue$(AdventureWonCommand(j-2,1),AdventureWonCommand(j-2,2),": "))
						Case 3
							cmdbit$="Data2"
							ShowTooltipCenterAligned(TooltipX,TooltipY,GetCMDData2NameAndValue$(AdventureWonCommand(j-2,1),AdventureWonCommand(j-2,3),": "))
						Case 4
							cmdbit$="Data3"
							ShowTooltipCenterAligned(TooltipX,TooltipY,GetCMDData3NameAndValue$(AdventureWonCommand(j-2,1),AdventureWonCommand(j-2,3),AdventureWonCommand(j-2,4),": "))
						Case 5
							cmdbit$="Data4"
							ShowTooltipCenterAligned(TooltipX,TooltipY,GetCMDData4NameAndValue$(AdventureWonCommand(j-2,1),AdventureWonCommand(j-2,5),": "))
						End Select
						Adventurewoncommand(j-2,i)=AdjustInt("Adventure won command "+cmdbit$+": ", Adventurewoncommand(j-2,i), 1, 10, DelayTime)
					EndIf
				EndIf

			Next
		Next

	EndIf

	; adventure goal
	If MouseY()>LetterHeight*17 And MouseY()<LetterHeight*18 And MouseX()<LetterX(25)
		AdventureGoal=AdjustInt("Adventure goal: ", AdventureGoal, 1, 10, DelayTime)
		If AdventureGoal<=-1 Then AdventureGoal=nofwinningconditions-1
		If AdventureGoal>=Nofwinningconditions Then adventuregoal=0
	EndIf

	; GateKeyVersion
	If MouseY()>LetterHeight*14 And MouseY()<LetterHeight*15 And MouseX()>LetterX(26) And MouseX()<LetterX(38)
		GateKeyVersion=AdjustInt("Gate/key version: ", GateKeyVersion, 1, 10, DelayTime)
		If GateKeyVersion<=0 Then GateKeyVersion=3
		If GateKeyVersion>=4 Then GateKeyVersion=1
		FreeTexture buttontexture
		FreeTexture gatetexture
		UpdateButtonGateTexture()
	EndIf

	; custom icon
	If mb>0 And MouseY()>LetterHeight*17 And MouseY()<LetterHeight*18 And MouseX()>LetterX(26) And MouseX()<LetterX(38)

		FreeTexture IconTextureCustom
		IconTextureCustom=0

		FlushKeys
		Locate 0,0
		Color 0,0,0
		Rect 0,0,500,40,True
		Color 255,255,255
		CustomIconName$=Input$( "Enter Custom Icon Texture Name (e.g. 'standard'):")

		If CustomIconName$="" Or CustomIconName$="Standard"
			CustomIconName$="Standard"
		Else
			If FileType(globaldirname$+"\custom\icons\icons "+CustomIconName$+".bmp")<>1
				Locate 0,0
				Color 0,0,0
				Rect 0,0,500,60,True
				Color 255,255,0
				Print "Error: Custom Icon File '"+customiconname$+"' not found."
				Print "Reverting to 'Standard' Custom Icon Texture."
				Delay 2000

				CustomIconName$="Standard"
			EndIf

		EndIf

		IconTextureCustom=myLoadTexture(globaldirname$+"\Custom\Icons\icons "+customiconname$+".bmp",4)

	EndIf

	If MouseX()>WlvColumnLeft And MouseX()<LetterX(41.5) And MouseY()<LowerButtonsCutoff
		If CtrlDown() And mb>0
			If OpenTypedLevel()
				StartEditorMainLoop()
			EndIf
		Else
			For i=1 To 20
				If MouseY()>LetterHeight*3+i*LetterHeight And MouseY()<=LetterHeight*4+i*LetterHeight
					SelectedLevel=i+MasterLevelListStart
					If mb=1
						If CopyingLevel=StateCopying And LevelExists(SelectedLevel)=False
							; copy from CopiedLevel
							dirbase$=GetAdventureDir$()
							CopyFile(dirbase$+CopiedLevel+".wlv",dirbase$+SelectedLevel+".wlv")
							MasterLevelList(SelectedLevel)=1

							CopyingLevel=StateNotSpecial
						ElseIf CopyingLevel=StateSwapping
							SwapLevel(CopiedLevel,SelectedLevel)
							CopyingLevel=StateNotSpecial
						Else
							AccessLevelAtCenter(SelectedLevel)
							StartEditorMainLoop()
						EndIf

						Repeat
						Until LeftMouseDown()=0

						mb=0
						Exit
					ElseIf mb=2 And LevelExists(SelectedLevel)=True
						If CopyingLevel=StateCopying And SelectedLevel=CopiedLevel
							CopyingLevel=StateNotSpecial
						Else
							CopyingLevel=StateCopying
							CopiedLevel=SelectedLevel
						EndIf

						Repeat
						Until RightMouseDown()=0

						mb=0
						Exit
					ElseIf mb=3 And LevelExists(SelectedLevel)=True
						If CopyingLevel=StateSwapping And SelectedLevel=CopiedLevel
							CopyingLevel=StateNotSpecial
						Else
							CopyingLevel=StateSwapping
							CopiedLevel=SelectedLevel
						EndIf

						Repeat
						Until MouseDown(3)=0

						mb=0
						Exit
					EndIf
				EndIf
			Next
		EndIf
	EndIf

	; load dialog
	StartX=LetterX(41.5)
	If MouseX()>StartX And MouseX()<GfxWidth And MouseY()<LowerButtonsCutoff
		If (CtrlDown() And mb>0) Or HotkeyOpen()
			If OpenTypedDialog()
				StartDialog()
			EndIf
		Else
			For i=1 To 20
				StartY=LetterHeight*3+i*LetterHeight
				If MouseY()>StartY And MouseY()<=StartY+LetterHeight
					SelectedDialog = i+MasterDialogListStart
					ShowTooltipRightAligned(StartX,StartY+LetterHeight*2,PreviewDialog$(SelectedDialog,0))

					If mb=1
						If CopyingDialog=StateCopying And DialogExists(SelectedDialog)=False
							; copy from CopiedDialog
							dirbase$=GetAdventureDir$()
							CopyFile(dirbase$+CopiedDialog+".dia",dirbase$+SelectedDialog+".dia")
							MasterDialogList(SelectedDialog)=1

							CopyingDialog=StateNotSpecial
						ElseIf CopyingDialog=StateSwapping
							SwapDialog(CopiedDialog,SelectedDialog)
							CopyingDialog=StateNotSpecial
						Else
							AccessDialog(SelectedDialog)
							StartDialog()
						EndIf

						Repeat
						Until LeftMouseDown()=0

						mb=0
						Exit
					ElseIf mb=2 And DialogExists(SelectedDialog)=True
						If CopyingDialog=StateCopying And SelectedDialog=CopiedDialog
							CopyingDialog=StateNotSpecial
						Else
							CopyingDialog=StateCopying
							CopiedDialog=SelectedDialog
						EndIf

						Repeat
						Until RightMouseDown()=0

						mb=0
						Exit
					ElseIf mb=3 And DialogExists(SelectedDialog)=True
						If CopyingDialog=StateSwapping And SelectedDialog=CopiedDialog
							CopyingDialog=StateNotSpecial
						Else
							CopyingDialog=StateSwapping
							CopiedDialog=SelectedDialog
						EndIf

						Repeat
						Until MouseDown(3)=0

						mb=0
						Exit
					EndIf
				EndIf
			Next
		EndIf
	Else
		; load level
		If HotkeyOpen()
			If OpenTypedLevel()
				StartEditorMainLoop()
			EndIf
		EndIf
	EndIf

	If mb>0

		If MouseY()>LowerButtonsCutoff And MouseX()<LetterX(11)
			DisplayText2(">       <",1,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",1,28,TextMenusR,TextMenusG,TextMenusB)
			WaitFlag=True
			If hubmode
				SetEditorMode(11)
			Else
				StartAdventureSelectScreen()
			EndIf
		EndIf

		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(11) And MouseX()<LetterX(22)
			DisplayText2(">       <",12,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",12,28,TextMenusR,TextMenusG,TextMenusB)
			WaitFlag=True
			SaveMasterFile()
			If hubmode
				SetEditorMode(11)
			Else
				StartAdventureSelectScreen()
			EndIf

		EndIf

		; SAVE+TEST ; SAVE AND TEST
		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(22) And MouseX()<LetterX(33)
			DisplayText2(">       <",23,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",23,28,TextMenusR,TextMenusG,TextMenusB)
			StartTestMode()
		EndIf
		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(33) And hubmode=False
			If CtrlDown()
				PackContent=True
			Else
				PackContent=False
			EndIf
			DisplayText2(">       <",34,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",34,28,TextMenusR,TextMenusG,TextMenusB)
			SaveMasterFile()
			If CompileAdventure(PackContent)=True
				StartAdventureSelectScreen()
			EndIf
			If hubmode
				hubmode=False
				SaveHubFile()
			EndIf
			Repeat
			Until LeftMouseDown()=False

		EndIf

	EndIf

	RenderLetters()
	RenderWorld()

	FinishDrawing()

	If waitflag=True Delay 1000

End Function

Function MasterAdvancedLoop()

	If CtrlDown() And KeyDown(20) ; Ctrl+T
		StartTestMode()
	EndIf

	adj=1
	If KeyDown(42) Or KeyDown(54) Then adj=10

	DisplayText2("Adventure File Name: ",0,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2(AdventureFileName$,0,1,255,255,255)
	DisplayText2("--------------------------------------------",0,2,TextMenusR,TextMenusG,TextMenusB)

	If MouseY()<LetterHeight And  MouseX()>LetterX(30)
		DisplayText2("                              (Main Options)",0,0,255,255,255)
	Else
		DisplayText2("                              (Main Options)",0,0,TextMenusR,TextMenusG,TextMenusB)
	EndIf

	DisplayText2("Adventure Title:",0,3,TextMenusR,TextMenusG,TextMenusB)

	DisplayText2(AdventureTitle$,0,4,255,255,255)
	DisplayText2("--------------------------------------------",0,5,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("Starting items:",0,6,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("Gloves:         GlowGem:         Spy-eye:",0,7,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("Gloves:Yes                      Spy-eye:Yes",0,7,255,255,255)
	;DisplayText2("               GlowGem:Yes",0.5,7,255,255,255)
	;If MouseY()<22 And  MouseX()>580
	If StarterItems And 1
		DisplayText2("       Yes",0,7,255,255,255)
	Else
		DisplayText2("       No",0,7,255,255,255)
	EndIf

	If StarterItems And 2
		DisplayText2("       Yes",17,7,255,255,255)
	Else
		DisplayText2("       No",17,7,255,255,255)
	EndIf

	If StarterItems And 4
		DisplayText2("       Yes",34,7,255,255,255)
	Else
		DisplayText2("       No",34,7,255,255,255)
	EndIf

	DisplayText2("--------------------------------------------",0,8,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("Widescreen Spell Range:",0,9,TextMenusR,TextMenusG,TextMenusB)

	If WidescreenRange
		DisplayText2("                       On ",0,9,255,255,255)
	Else
		DisplayText2("                       Off ",0,9,255,255,255)
	EndIf
	DisplayText2("--------------------------------------------",0,10,TextMenusR,TextMenusG,TextMenusB)
	Displaytext2("ShardCMD:  #    C    D    D    D    D",0,11,TextMenusR,TextMenusG,TextMenusB)
	;Displaytext2(Str$(adventurewoncommand(i,j)),12+j*5,11,255,255,255)
	Displaytext2(SelectedShard,12,11,255,255,255)
	For j=0 To 4
		Displaytext2(Str$(CustomShardCMD(SelectedShard,j)),17+j*5,11,255,255,255)
	Next
	DisplayText2("--------------------------------------------",0,12,TextMenusR,TextMenusG,TextMenusB)
	Displaytext2("GlyphCMD:  #    C    D    D    D    D",0,13,TextMenusR,TextMenusG,TextMenusB)
	Displaytext2(SelectedGlyph,12,13,255,255,255)
	For j=0 To 4
		Displaytext2(Str$(CustomGlyphCMD(SelectedGlyph,j)),17+j*5,13,255,255,255)
	Next
	DisplayText2("--------------------------------------------",0,14,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("Custom Map Name:",0,15,TextMenusR,TextMenusG,TextMenusB)
	If CustomMapName$=""
		DisplayText2("None",16,15,150,150,150)
	Else
		DisplayText2(CustomMapName$,16,15,255,255,255)
	EndIf
	DisplayText2("--------------------------------------------",0,16,TextMenusR,TextMenusG,TextMenusB)

	DisplayText2("--------------------------------------------",0,25,TextMenusR,TextMenusG,TextMenusB)

	DisplayText2("========== ========== ==========",0.5,26,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	DisplayText2(":        : :        : :        :",0.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	DisplayText2(":        : :        : :        :",0.5,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	DisplayText2("========== ========== ==========",0.5,29,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

	If hubmode
		DisplayText2("                                 ==========",0.5,26,50,50,0)
		DisplayText2("                                 :        :",0.5,27,50,50,0)
		DisplayText2("                                 :        :",0.5,28,50,50,0)
		DisplayText2("                                 ==========",0.5,29,50,50,0)
	Else
		DisplayText2("                                 ==========",0.5,26,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("                                 :        :",0.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("                                 :        :",0.5,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("                                 ==========",0.5,29,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	EndIf

	If MouseY()>LowerButtonsCutoff And MouseX()<LetterX(11)
		DisplayText2("CANCEL",2.5,27,255,255,255)
		DisplayText2("+EXIT",3,28,255,255,255)
	Else
		DisplayText2("CANCEL",2.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",3,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	EndIf

	If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(11) And MouseX()<LetterX(22)
		DisplayText2(" SAVE",13.5,27,255,255,255)
		DisplayText2("+EXIT",14,28,255,255,255)
	Else
		DisplayText2(" SAVE",13.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",14,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

	EndIf

	If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(22) And MouseX()<LetterX(33)
		DisplayText2(" SAVE",24.5,27,255,255,255)
		DisplayText2("+TEST",25,28,255,255,255)
	Else
		DisplayText2(" SAVE",24.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+TEST",25,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

	EndIf
	If hubmode
		DisplayText2("COMPILE",35,27,50,50,0)
		DisplayText2("+EXIT",36,28,50,50,0)
	Else
		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(33)
			DisplayText2("COMPILE",35,27,255,255,255)
			DisplayText2("+EXIT",36,28,255,255,255)
		Else
			DisplayText2("COMPILE",35,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
			DisplayText2("+EXIT",36,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		EndIf
	EndIf

	mb=0
	If LeftMouse mb=1
	If RightMouse mb=2
	If mb>0
		If MouseY()<LetterHeight And MouseX()>LetterX(30)
			SetEditorMode(8)
			Repeat
			Until LeftMouseDown()=0 And RightMouseDown()=0
		EndIf

		If MouseY()>LetterHeight*7 And MouseY()<LetterHeight*8
			If MouseX()<LetterX(14.6)
				StarterItems=StarterItems Xor 1
				Repeat
				Until LeftMouseDown()=0 And RightMouseDown()=0
			EndIf

			If MouseX()>LetterX(14.6) And MouseX()<LetterX(29.2)
				StarterItems=StarterItems Xor 2
				Repeat
				Until LeftMouseDown()=0 And RightMouseDown()=0
			EndIf

			If MouseX()>LetterX(29.2)
				StarterItems=StarterItems Xor 4
				Repeat
				Until LeftMouseDown()=0 And RightMouseDown()=0
			EndIf

		EndIf

		If MouseY()>LetterHeight*9 And MouseY()<LetterHeight*10 And MouseX()<LetterX(24)
			WidescreenRange=Not WidescreenRange
			Repeat
			Until LeftMouseDown()=0 And RightMouseDown()=0
		EndIf

		If MouseY()>LowerButtonsCutoff And MouseX()<LetterX(11)
			DisplayText2(">       <",1,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",1,28,TextMenusR,TextMenusG,TextMenusB)
			WaitFlag=True
			If hubmode
				SetEditorMode(11)
			Else
				StartAdventureSelectScreen()
			EndIf
		EndIf

		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(11) And MouseX()<LetterX(22)
			DisplayText2(">       <",12,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",12,28,TextMenusR,TextMenusG,TextMenusB)
			WaitFlag=True
			SaveMasterFile()
			If hubmode
				SetEditorMode(11)
			Else
				StartAdventureSelectScreen()
			EndIf

		EndIf

		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(22) And MouseX()<LetterX(33)
			DisplayText2(">       <",23,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",23,28,TextMenusR,TextMenusG,TextMenusB)
			StartTestMode()
		EndIf

		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(33) And hubmode=False ; Compile
			If CtrlDown()
				PackContent=True
			Else
				PackContent=False
			EndIf
			DisplayText2(">       <",34,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",34,28,TextMenusR,TextMenusG,TextMenusB)
			SaveMasterFile()
			If CompileAdventure(PackContent)=True
				StartAdventureSelectScreen()
			EndIf
			If hubmode
				hubmode=False
				SaveHubFile()
			EndIf
			Repeat
			Until LeftMouseDown()=0

		EndIf

		For i=0 To 5
			For j=0 To 2
				If MouseX()>LetterX(10+i*5) And MouseX()<LetterX(14+i*5) And MouseY()>LetterHeight*9+(j+2)*LetterHeight And MouseY()<LetterHeight*10+(j+2)*LetterHeight And MouseDebounceFinished()
					;Locate 0,0
					;Print "j="+j+" i="+i
					If j=0 And i=0
						;Print "here "+SelectedShard
						If mb=1 SelectedShard=SelectedShard+adj
						If mb=2 SelectedShard=SelectedShard-adj
						If SelectedShard<0 Then SelectedShard=NoOfShards-1
						If SelectedShard>=NoOfShards Then SelectedShard=0
					EndIf
					If j=2 And i=0
						If mb=1 SelectedGlyph=SelectedGlyph+adj
						If mb=2 SelectedGlyph=SelectedGlyph-adj
						If SelectedGlyph<0 Then SelectedGlyph=NoOfGlyphs-1
						If SelectedGlyph>=NoOfGlyphs Then SelectedGlyph=0
					EndIf
					If j=0 And i>0
						If mb=1 CustomShardCMD(SelectedShard,i-1)=CustomShardCMD(SelectedShard,i-1)+adj
						If mb=2 CustomShardCMD(SelectedShard,i-1)=CustomShardCMD(SelectedShard,i-1)-adj
					EndIf
					If j=2 And i>0
						If mb=1 CustomGlyphCMD(SelectedGlyph,i-1)=CustomGlyphCMD(SelectedGlyph,i-1)+adj
						If mb=2 CustomGlyphCMD(SelectedGlyph,i-1)=CustomGlyphCMD(SelectedGlyph,i-1)-adj
					EndIf
					MouseDebounceSet(10)
				EndIf
			Next
		Next

		If MouseY()>LetterHeight*15 And MouseY()<LetterHeight*16 And MouseX()>LetterX(16)
			FlushKeys
			Locate 0,0
			Color 0,0,0
			Rect 0,0,500,40,True
			Color 255,255,255
			CustomMapName$=Input$( "Enter Custom Map Name:")
			If CustomMapName$<>""
				For i=0 To 8
					If FileType(GlobalDirName$+"\Custom\Maps\"+CustomMapName$+"\mappiece"+Str$(i)+".bmp")<>1
						;NoMapFlag=True
						Cls
						Print "Error: Custom Map piece '"+CustomMapName$+"\mappiece"+Str$(i)+".bmp' not found."
						Print "Reverting to no map."
						Delay 2000
						CustomMapName$=""
						Exit
					EndIf
				Next
			EndIf
		EndIf
	EndIf

	RenderLetters()
	RenderWorld()

	FinishDrawing()

	If waitflag=True Delay 1000
End Function

Function HubMainLoop()

	;If KeyDown(157) And KeyDown(20)
	;	WaitFlag=True
	;	SaveMasterFile()
	;	file=WriteFile("test.dat")
	;	WriteString file,AdventureFileName$
	;	CloseFile file
	;	ExecFile ("wg.exe")
	;	End
	;EndIf
	dialogtimer=dialogtimer+1
	DisplayText2("Hub File Name: ",0,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2(HubFileName$,0,1,255,255,255)
	DisplayText2("--------------------------------------------",0,2,TextMenusR,TextMenusG,TextMenusB)

	adj=1
	If ShiftDown() Then adj=10

	For i=0 To 43
		AddLetter(Asc("X")-32,-.97+i*.045,.5-0*.05,1,0,.04,0,0,0,0,0,0,0,0,0,TextMenuXR,TextMenuXG,TextMenuXB)
		For j=0 To 0
			AddLetter(Asc("X")-32,-.97+i*.045,.5-(3+j)*.05,1,0,.04,0,0,0,0,0,0,0,0,0,TextMenuXR,TextMenuXG,TextMenuXB)
		Next
	Next

	DisplayText2("Hub Title:",0,3,TextMenusR,TextMenusG,TextMenusB)

	DisplayText2(HubTitle$,0,4,255,255,255)
	DisplayText2("--------------------------------------------",0,5,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("Hub Description:",0,6,TextMenusR,TextMenusG,TextMenusB)

	DisplayText2(HubDescription$,0,7,255,255,255)
	DisplayText2("--------------------------------------------",0,8,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("Starting items:",0,6,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("Gloves:         GlowGem:         Spy-eye:",0,7,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("Gloves:Yes                      Spy-eye:Yes",0,7,255,255,255)
	;DisplayText2("               GlowGem:Yes",0.5,7,255,255,255)
	;If MouseY()<22 And  MouseX()>580

	For i=9 To 25
		DisplayText2("    :                         :",0,i,TextMenusR,TextMenusG,TextMenusB)

	Next

	;HubAdvStart=0
	For i=0 To 12
		c=HubAdvStart+i
		flag=False
		If MouseX()<LetterX(4) And MouseY()>11*LetterHeight+i*LetterHeight And MouseY()<=12*LetterHeight+i*LetterHeight
			flag=True
		EndIf

		If c=0
			s$="Hub"
		Else
			If c<10
				s$="00"+Str(c)
			ElseIf c<100
				s$="0"+Str(c)
			Else
				s$=Str(c)
			EndIf
		EndIf
		If flag ; mouse over
			ColorR=255
			ColorG=255
			ColorB=100
		ElseIf CopyingLevel=StateCopying And CopiedLevel=c
			ColorR=0
			ColorG=255
			ColorB=255
		ElseIf CopyingLevel=StateSwapping And CopiedLevel=c
			ColorR=255
			ColorG=0
			ColorB=255
		ElseIf HubAdventuresFilenames$(c)<>"" And c<=HubTotalAdventures
			ColorR=210
			ColorG=210
			ColorB=210
		Else
			ColorR=100
			ColorG=100
			ColorB=100
		EndIf
		DisplayText2(s$,0.5,11+i,ColorR,ColorG,ColorB)

		If c<=HubTotalAdventures
			If HubAdventuresMissing(c)
				ColorR=255
				ColorG=0
				ColorB=0
			ElseIf Not HubAdventuresIncludeInTotals(c)
				ColorR=255
				ColorG=155
				ColorB=0
			Else
				ColorR=210
				ColorG=210
				ColorB=210
			EndIf
			DisplayText2(HubAdventuresFilenames$(c),5,11+i,ColorR,ColorG,ColorB)
		EndIf
	Next

	If HubSelectedAdventure=-1
		HubSelectedAdventureText$="---"
	ElseIf HubSelectedAdventure=0
		HubSelectedAdventureText$="Hub"
	ElseIf HubSelectedAdventure<10
		HubSelectedAdventureText$="00"+HubSelectedAdventure
	ElseIf HubSelectedAdventure<100
		HubSelectedAdventureText$="0"+HubSelectedAdventure
	Else
		HubSelectedAdventureText$=HubSelectedAdventure
	EndIf
	DisplayText2("Adv# FileName                  Selected: "+HubSelectedAdventureText,0,9,TextMenusR,TextMenusG,TextMenusB)
	flag2=False
	If HubSelectedAdventure>=0
		If HubAdventuresFilenames$(HubSelectedAdventure)<>""
			flag2=True
		EndIf
	EndIf
	If flag2
		If MouseX()>LetterX(33) And MouseX()<LetterX(41) And MouseY()>LetterHeight*13 And MouseY()<LetterHeight*14
			DisplayText2("                                   EDIT",0.5,13,255,255,255)
		Else
			DisplayText2("                                   EDIT",0.5,13,180,180,180)
		EndIf
		If MouseX()>LetterX(33) And MouseX()<LetterX(41) And MouseY()>LetterHeight*17 And MouseY()<LetterHeight*18
			DisplayText2("                                  REPLACE",0,17,255,255,255)
		Else
			DisplayText2("                                  REPLACE",0,17,180,180,180)
		EndIf
		If MouseX()>LetterX(33) And MouseX()<LetterX(41) And MouseY()>LetterHeight*21 And MouseY()<LetterHeight*22
			DisplayText2("                                  REMOVE",0.5,21,255,255,255)
		Else
			DisplayText2("                                  REMOVE",0.5,21,180,180,180)
		EndIf
	Else
	DisplayText2("                                   EDIT",0.5,13,100,100,100)
	DisplayText2("                                  REPLACE",0,17,100,100,100)
	DisplayText2("                                  REMOVE",0.5,21,100,100,100)
	EndIf
	If MouseX()<LetterX(4) And MouseY()>LetterHeight*10 And MouseY()<LetterHeight*11
		DisplayText2(" -",0.5,10,TextMenusR,TextMenusG,TextMenusB)
	Else
		DisplayText2(" -",0.5,10,TextMenuXR,TextMenuXG,TextMenuXB)
	EndIf
	If MouseX()<LetterX(4) And MouseY()>LetterHeight*24 And MouseY()<LetterHeight*25
		DisplayText2(" +",0.5,24,TextMenusR,TextMenusG,TextMenusB)
	Else
		DisplayText2(" +",0.5,24,TextMenuXR,TextMenuXG,TextMenuXB)
	EndIf
	;DisplayText2("Adv#:FileName                   :EDIT:REMOVE",0,9,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("----:---------------------------: -  :  -",0,10,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("Hub :myHub                      :Edit:Remove",0,11,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("001 :(Click to add)             :Edit:Remove",0,12,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("002 :(Click to add)             :Edit:Remove",0,13,TextMenusR,TextMenusG,TextMenusB)

	;DisplayText2("Adv#:FileName                 :Selected: 001",0,9,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("     ------------------------- -------------",0,10,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("Hub :myHub                    : ",0,11,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("001 :(Click to add)           :Edit:Remove",0,12,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("002 :(Click to add)           :Edit:Remove",0,13,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2("--------------------------------------------",0,10,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("   ",0,24,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("---- ------------------------- -------------",0,25,TextMenusR,TextMenusG,TextMenusB)

	DisplayText2("========== ========== ========== ==========",0.5,26,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	DisplayText2(":        : :        : :        : :        :",0.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	DisplayText2(":        : :        : :        : :        :",0.5,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	DisplayText2("========== ========== ========== ==========",0.5,29,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

	If MouseY()>LowerButtonsCutoff And MouseX()<LetterX(11)
		DisplayText2("CANCEL",2.5,27,255,255,255)
		DisplayText2("+EXIT",3,28,255,255,255)
	Else
		DisplayText2("CANCEL",2.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",3,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	EndIf

	If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(11) And MouseX()<LetterX(22)
		DisplayText2(" SAVE",13.5,27,255,255,255)
		DisplayText2("+EXIT",14,28,255,255,255)
	Else
		DisplayText2(" SAVE",13.5,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",14,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

	EndIf

	If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(22) And MouseX()<LetterX(33)
		DisplayText2("BUILD",25,27,255,255,255)
		DisplayText2("+EXIT",25,28,255,255,255)
	Else
		DisplayText2("BUILD",25,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",25,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)

	EndIf

	If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(33)
		DisplayText2("COMPILE",35,27,255,255,255)
		DisplayText2("+EXIT",36,28,255,255,255)
	Else
		DisplayText2("COMPILE",35,27,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
		DisplayText2("+EXIT",36,28,TextMenuButtonR,TextMenuButtonG,TextMenuButtonB)
	EndIf

	mb=0
	If LeftMouse mb=1
	If RightMouse mb=2
	If MouseDown(3) mb=3

	;If MouseY()<22 And  MouseX()>540
	;	SetEditorMode(8)
	;	Repeat
	;	Until LeftMouseDown()=0 And RightMouseDown()=0
	;EndIf
	If MouseX()<LetterX(4)
		If MouseScroll<>0
			adj=-MouseScroll
			If ShiftDown()
				adj=adj*10
			EndIf
			HubAdvStart=HubAdvStart+adj
		ElseIf mb>0 And MouseDebounceFinished()
			If MouseY()>LetterHeight*10 And MouseY()<LetterHeight*11
				HubAdvStart=HubAdvStart-adj
				MouseDebounceSet(10)
			EndIf

			If MouseY()>LetterHeight*24 And MouseY()<LetterHeight*25
				HubAdvStart=HubAdvStart+adj
				MouseDebounceSet(10)
			EndIf

			;If CtrlDown()
			;	HubSelectedAdventure=InputInt("Enter adventure number to select: ")
			;	LeftMouseReleased=False
			;EndIf
		EndIf

		If HubAdvStart<0
			HubAdvStart=0
		ElseIf HubAdvStart+12>HubAdvMax
			HubAdvStart=HubAdvMax-12
		EndIf
	EndIf

	If mb>0 And LeftMouseReleased=True
		For i=0 To 12
			If MouseX()<LetterX(4) And MouseY()>LetterHeight*11+i*LetterHeight And MouseY()<=LetterHeight*12+i*LetterHeight
				HubSelectedAdventure=HubAdvStart+i
				If mb=1 And LeftMouseReleased=True
					If CopyingLevel=StateCopying And HubAdventuresFilenames$(HubSelectedAdventure)=""
						HubAdventuresFilenames$(HubSelectedAdventure)=HubAdventuresFilenames$(CopiedLevel)
						HubAdventuresMissing(HubSelectedAdventure)=HubAdventuresMissing(CopiedLevel)

						CopyingLevel=StateNotSpecial

						If HubSelectedAdventure>HubTotalAdventures
							HubTotalAdventures=HubSelectedAdventure
						EndIf
					ElseIf CopyingLevel=StateSwapping
						TempFilename$=HubAdventuresFilenames$(HubSelectedAdventure)
						TempMissing=HubAdventuresMissing(HubSelectedAdventure)
						HubAdventuresFilenames$(HubSelectedAdventure)=HubAdventuresFilenames$(CopiedLevel)
						HubAdventuresMissing(HubSelectedAdventure)=HubAdventuresMissing(CopiedLevel)
						HubAdventuresFilenames$(CopiedLevel)=TempFilename$
						HubAdventuresMissing(CopiedLevel)=TempMissing

						CopyingLevel=StateNotSpecial

						If HubSelectedAdventure>HubTotalAdventures
							HubTotalAdventures=HubSelectedAdventure
						EndIf
					ElseIf HubAdventuresFilenames$(HubSelectedAdventure)="" Or HubSelectedAdventure>HubTotalAdventures
						CopyingLevel=StateNotSpecial
						SetAdventureCurrentArchive(0)
						GetAdventures()
						AdventureNameEntered$=""
						SetEditorMode(12)
					Else
						CopyingLevel=StateNotSpecial

						If KeyDown(45) ; x key
							HubAdventuresIncludeInTotals(HubSelectedAdventure)=Not HubAdventuresIncludeInTotals(HubSelectedAdventure)
						EndIf
					EndIf

					Repeat
					Until LeftMouseDown()=0

					mb=0
					Exit
				ElseIf HubAdventuresFilenames$(HubSelectedAdventure)="" Or HubSelectedAdventure>HubTotalAdventures
					CopyingLevel=StateNotSpecial
					SetAdventureCurrentArchive(0)
					GetAdventures()
					AdventureNameEntered$=""
					SetEditorMode(12)
				ElseIf mb=2
					If CopyingLevel=StateCopying And HubSelectedAdventure=CopiedLevel
						CopyingLevel=StateNotSpecial
					Else
						CopyingLevel=StateCopying
						CopiedLevel=HubSelectedAdventure
					EndIf

					Repeat
					Until RightMouseDown()=0

					mb=0
					Exit
				ElseIf mb=3
					If CopyingLevel=StateSwapping And HubSelectedAdventure=CopiedLevel
						CopyingLevel=StateNotSpecial
					Else
						CopyingLevel=StateSwapping
						CopiedLevel=HubSelectedAdventure
					EndIf

					Repeat
					Until MouseDown(3)=0

					mb=0
					Exit
				EndIf
			EndIf
		Next

		; edit
		If MouseX()>LetterX(33) And MouseX()<LetterX(41) And MouseY()>LetterHeight*13 And MouseY()<LetterHeight*14 And HubSelectedAdventure>=0
			AdventureFileName$=TrimHubAdventureName$(HubAdventuresFilenames$(HubSelectedAdventure))
			MasterDialogListStart=0
			MasterLevelListStart=0
			StartMaster()
			Repeat
			Until LeftMouseDown()=0
		EndIf

		; replace
		If MouseX()>LetterX(33) And MouseX()<LetterX(41) And MouseY()>LetterHeight*17 And MouseY()<LetterHeight*18 And HubSelectedAdventure>=0
			SetAdventureCurrentArchive(0)
			GetAdventures()
			SetEditorMode(12)
			Repeat
			Until LeftMouseDown()=0
		EndIf

		; remove
		If MouseX()>LetterX(33) And MouseX()<LetterX(41) And MouseY()>LetterHeight*21 And MouseY()<LetterHeight*22 And HubSelectedAdventure>=0
			HubAdventuresFilenames$(HubSelectedAdventure)=""
			;also check if this is the bigest number and update HubTotalAdventures
			If HubTotalAdventures=HubSelectedAdventure
				;find the new HubTotalAdventures
				For i=HubTotalAdventures To 1 Step-1
					If HubAdventuresFilenames$(i)<>""
						Exit
					EndIf
					HubTotalAdventures=HubTotalAdventures-1
				Next
			EndIf
			HubSelectedAdventure=-1
			Repeat
			Until LeftMouseDown()=0
		EndIf
		If MouseY()>LowerButtonsCutoff And MouseX()<LetterX(11)
			DisplayText2(">       <",1,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",1,28,TextMenusR,TextMenusG,TextMenusB)
			WaitFlag=True
			hubmode=False
			StartAdventureSelectScreen()
		EndIf

		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(11) And MouseX()<LetterX(22) ; Save+Exit
			DisplayText2(">       <",12,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",12,28,TextMenusR,TextMenusG,TextMenusB)
			WaitFlag=True
			SaveHubFile()
			hubmode=False
			StartAdventureSelectScreen()

		EndIf

		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(22) And MouseX()<LetterX(33)
			DisplayText2(">       <",23,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",23,28,TextMenusR,TextMenusG,TextMenusB)
			;WaitFlag=True
			If BuildHub()=True
				SaveHubFile()
				hubmode=False
				StartAdventureSelectScreen()
			EndIf
			Repeat
			Until LeftMouseDown()=False
		EndIf

		If MouseY()>LowerButtonsCutoff And MouseX()>LetterX(33)
			DisplayText2(">       <",34,27,TextMenusR,TextMenusG,TextMenusB)
			DisplayText2(">       <",34,28,TextMenusR,TextMenusG,TextMenusB)
			If CtrlDown()
				PackContent=True
			Else
				PackContent=False
			EndIf
			;WaitFlag=True
			;SaveMasterFile()
			If CompileHub(PackContent)=True
				SaveHubFile()
				hubmode=False
				StartAdventureSelectScreen()
			EndIf
			Repeat
			Until LeftMouseDown()=False

		EndIf
	EndIf

	If HotkeySave()
		SaveHubFile()
	EndIf

	MouseTextEntryTrackMouseMovement()

	Entering=0
	x=GetMouseLetterX()
	y=(MouseY()-LetterHeight*5)/LetterHeight
	If x<38 And MouseY()>=84 And y=3
		Entering=1
		If x>Len(HubDescription$) Then x=Len(HubDescription$)
		If DialogTimer Mod 50 <25 Or OldX<>x Or OldY<>y
			AddLetter(Asc("_")-32,-.97+x*.045,.5-y*.05,1,0,.05,0,0,0,0,0,0,0,0,0,255,255,255)
		EndIf
	EndIf
	If x<38 And y=0
		Entering=2
		If x>Len(HubTitle$) Then x=Len(HubTitle$)
		If DialogTimer Mod 50 <25 Or OldX<>x Or OldY<>y
			AddLetter(Asc("_")-32,-.97+x*.045,.5-y*.05,1,0,.05,0,0,0,0,0,0,0,0,0,255,255,255)
		EndIf
	EndIf

	OldX=x
	OldY=y
	; entering text
	let=GetKey()
	If Entering>0

		Select entering
		Case 1
			tex$=HubDescription$
		Case 2
			tex$=HubTitle$
; PUT BACK IN FOR ME - HELP LINE (DON"T NEED)
;		Case 3
;			tex$=AdventureHelpLine$(y-10)

		End Select

		tex$=MouseTextEntry$(tex$,let,x,y,0,2)

		Select entering
		Case 1
			HubDescription$=tex$
		Case 2
			HubTitle$=tex$
	;	Case 3
	;		AdventureHelpLine$(y-10)=tex$

		End Select

	EndIf

	RenderLetters()
	RenderWorld()

	FinishDrawing()

	If waitflag=True Delay 1000
End Function