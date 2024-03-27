Function SetAdventureFileNamesListedStart(Target)

	AdventureFileNamesListedStart=Target

	If AdventureFileNamesListedStart>NofAdventureFileNames-19 Then AdventureFileNamesListedStart=NofAdventureFileNames-19
	If AdventureFileNamesListedStart<0 Then AdventureFileNamesListedStart=0

End Function

Function AdventureFileNamesListPageUp()
	AdventureFileNamesListedStart=AdventureFileNamesListedStart-19
	If AdventureFileNamesListedStart<0 Then AdventureFileNamesListedStart=0
End Function

Function AdventureFileNamesListPageDown()
	AdventureFileNamesListedStart=AdventureFileNamesListedStart+19
	If AdventureFileNamesListedStart>NofAdventureFileNames-19 Then AdventureFileNamesListedStart=NofAdventureFileNames-19
	If AdventureFileNamesListedStart<0 Then AdventureFileNamesListedStart=0
End Function

Function StartAdventureSelectScreen()

	SetEditorMode(5)
	Camera1Proj=0
	Camera2Proj=0
	Camera3Proj=0
	Camera4Proj=0
	CameraProj=1
	UpdateCameraProj()

	GetAdventures()

	AdventureNameEntered$=""

End Function

Function AdventureSelectScreen()

	leveltimer=leveltimer+1

	mx=MouseX()
	my=MouseY()
	If my>=LetterHeight*8 And my<LetterHeight*27 And mx>LetterX(2.5) And mx<LetterX(41.5)
		AdventureNameSelected=(my-LetterHeight*8.5)/LetterHeight
	Else
		AdventureNameSelected=-1
	EndIf

	If EditorMode=5
		DisplayText2(Versiontext$,0,0,TextMenusR,TextMenusG,TextMenusB)

		;DisplayText2("================================",0,1,TextMenusR,TextMenusG,TextMenusB)
		;DisplayText2("            ====================",0,1,TextMenusR,TextMenusG,TextMenusB)
		;DisplayText2("            ================================",0,1,TextMenusR,TextMenusG,TextMenusB)

		DisplayText2("            ======================",0,1,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("                                  (Settings)",0,1,255,255,255)

	;	If displayfullscreen=True
	;		DisplayText2("                                (FullScreen)",0,1,255,255,255)
	;	Else
	;		DisplayText2("                                ( Windowed )",0,1,255,255,255)
	;	EndIf
		;hubmode=True
		If hubmode=True
			DisplayText2("(   Hubs   )",0,1,255,255,255)
			DisplayText2("Enter New Hub Filename (e.g. 'TestHub12345')",0,3,TextMenusR,TextMenusG,TextMenusB)
		Else
			DisplayText2("(Adventures)",0,1,255,255,255)
			DisplayText2("Enter New Adventure Filename (e.g. 'Test34')",0,3,TextMenusR,TextMenusG,TextMenusB)
		EndIf

		DisplayText2("Or Select Existing To Edit:                 ",0,6,TextMenusR,TextMenusG,TextMenusB)

		DisplayText2("User:",0,28,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2(EditorUserName$,6,28,255,255,255)
		DisplayText2("(CHANGE)",36,28,TextMenusR,TextMenusG,TextMenusB)
	ElseIf EditorMode=12
		DisplayText2("Hub File Name: ",0,0,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2(HubFileName$,0,1,255,255,255)
		DisplayText2("--------------------------------------------",0,2,TextMenusR,TextMenusG,TextMenusB)
		If HubSelectedAdventure<10
			x=2
		ElseIf HubSelectedAdventure<100
			x=1
		Else
			x=0
		EndIf
		If HubSelectedAdventure=0
			DisplayText2("Hub",41,0,TextMenusR,TextMenusG,TextMenusB)
		Else
			DisplayText2("Adventure"+HubSelectedAdventure,32+x,0,TextMenusR,TextMenusG,TextMenusB)
		EndIf

		DisplayText2("Enter New Adventure Filename (e.g. 'Test34')",0,3,TextMenusR,TextMenusG,TextMenusB)

		DisplayText2("Or Select Existing To Add:                 ",0,6,TextMenusR,TextMenusG,TextMenusB)

		DisplayText2("(BACK)",0,28,TextMenusR,TextMenusG,TextMenusB)
	EndIf

	DisplayText2("============================================",0,27,TextMenusR,TextMenusG,TextMenusB)

	If leveltimer Mod 100 <50
		DisplayText2(":",Len(AdventureNameEntered$),4,255,255,255)
	EndIf
	DisplayText2(AdventureNameEntered$,0,4,255,255,255)

	DisplayText2("============================================",0,7,TextMenusR,TextMenusG,TextMenusB)

	If CanChangeAdventureCurrentArchive()
;		If AdventureCurrentArchive=0
;			DisplayText2("Current",28,6,255,255,255)
;			DisplayText2("Archive",37,6,155,155,155)
;		Else
;			DisplayText2("Current",28,6,155,155,155)
;			DisplayText2("Archive",37,6,255,255,255)
;		EndIf
;		DisplayText2("/",35.5,5.9,TextMenusR,TextMenusG,TextMenusB)

		Select AdventureCurrentArchive
		Case AdventureCurrentArchiveArchive
			TheString$="Archive"
		Case AdventureCurrentArchivePlayer
			TheString$="Player"
		Case AdventureCurrentArchiveDataAdventures
			TheString$="Data/Adventures"
		Default
			TheString$="Current"
		End Select
		DisplayText2(TheString$,28,6,255,255,255)
	EndIf
	If NofAdventureFileNames>19
		For i=0 To 18
			displaytext2(":",2,8+i,TextMenusR,TextMenusG,TextMenusB)
			displaytext2(":",41,8+i,TextMenusR,TextMenusG,TextMenusB)
		Next
		DisplayText2("--",0,8,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Pg",0,9,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Up",0,10,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("--",0,11,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("--",0,23,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Pg",0,24,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Dn",0,25,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("--",0,26,TextMenusR,TextMenusG,TextMenusB)

		DisplayText2("--",42,8,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Pg",42,9,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Up",42,10,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("--",42,11,TextMenusR,TextMenusG,TextMenusB)

		DisplayText2("--",42,23,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Pg",42,24,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("Dn",42,25,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("--",42,26,TextMenusR,TextMenusG,TextMenusB)
	EndIf

	If AdventureNameSelected>=AdventureFileNamesListedStart+NofAdventureFileNames
		AdventureNameSelected=-1
	EndIf

	For i=0 To 18
		If AdventureFileNamesListedStart+i<NofAdventureFileNames
			AdventureFileName$=AdventureFileNamesListed$(AdventureFileNamesListedStart+i)
			If i=AdventureNameSelected
				DisplayText2(AdventureFileName$,22-Len(AdventureFileName$)/2,8+i,255,255,255)

				;ShowTooltipCenterAligned(LetterX(LettersCountX/2),LetterY(8.5+i),AdventureTitlesListed$(AdventureFileNamesListedStart+i))

				ShowTooltipCenterAligned(LetterX(LettersCountX/2+0.5),LetterY(8.5+i),GetAdventureTitle$(AdventureFileName$))
			Else
				DisplayText2(AdventureFileName$,22-Len(AdventureFileName$)/2,8+i,155,155,155)
			EndIf
		EndIf
	Next

	; Entering New Name
	let=GetKey()
	If let>=32 And let<=122 And Len(AdventureNameEntered$)<38
		AdventureNameEntered$=AdventureNameEntered$+Chr$(let)
	EndIf
	If KeyDown(14)
		; backspace key
		If Len(AdventureNameEntered$)>0
			AdventureNameEntered$=Left$(AdventureNameEntered$,Len(AdventureNameEntered$)-1)
			Delay CharacterDeleteDelay
		EndIf
	EndIf
	If KeyDown(211)
		; delete key
		AdventureNameEntered$=""
		Delay CharacterDeleteDelay
	EndIf
	If (KeyPressed(28) Or KeyPressed(156)) And ReturnKeyReleased=True
		; Enter key
		If hubmode And EditorMode=5
			If AdventureNameEntered$=""
				DisplayText2(" INVALID HUB NAME - Empty Name!",0,5,TextMenusR,TextMenusG,TextMenusB)
			Else If FileType(GlobalDirName$+"\Custom\Editing\Hubs\"+AdventureNameEntered$)=2
				DisplayText2(" INVALID HUB NAME - Already Exists!",0,5,TextMenusR,TextMenusG,TextMenusB)
			Else
				DisplayText2("--> STARTING HUB EDITOR - Please Wait!",0,5,TextMenusR,TextMenusG,TextMenusB)
				CreateDir GlobalDirName$+"\Custom\Editing\Hubs\"+AdventureNameEntered$

				HubFileName$=AdventureNameEntered$

				GetHubs()

				For i=0 To NofAdventureFileNames-1
					If HubFileName$=AdventureFileNamesListed$(i)
						AdventureNameSelected=i
						Repeat
						Until LeftMouseDown()=0
						StartHub()
					EndIf
				Next

			EndIf
		Else
			If AdventureNameEntered$=""
				DisplayText2(" INVALID ADVENTURE NAME - Empty Name!",0,5,TextMenusR,TextMenusG,TextMenusB)
			Else If FileType(GetAdventuresDir$(AdventureCurrentArchiveCurrent)+AdventureNameEntered$)=2
				DisplayText2(" INVALID ADVENTURE NAME - Already in Current!",0,5,TextMenusR,TextMenusG,TextMenusB)
			Else If FileType(GetAdventuresDir$(AdventureCurrentArchiveArchive)+AdventureNameEntered$)=2
				DisplayText2(" INVALID ADVENTURE NAME - Already in Archive!",0,5,TextMenusR,TextMenusG,TextMenusB)
			Else If FileType(GetAdventuresDir$(AdventureCurrentArchivePlayer)+EditorUserName$+"#"+AdventureNameEntered$)=2
				DisplayText2(" INVALID ADVENTURE NAME - Already in Player!",0,5,TextMenusR,TextMenusG,TextMenusB)
			Else If FileType(GetAdventuresDir$(AdventureCurrentArchiveDataAdventures)+AdventureNameEntered$)=2
				DisplayText2(" INVALID ADVENTURE NAME - Already in Data\Adventures!",0,5,TextMenusR,TextMenusG,TextMenusB)
			Else
				DisplayText2("--> STARTING MAIN EDITOR - Please Wait!",0,5,TextMenusR,TextMenusG,TextMenusB)

				AdventureCurrentArchive=AdventureCurrentArchiveCurrent ; Set to current.

				CreateDir GetAdventuresDir$(AdventureCurrentArchiveCurrent)+AdventureNameEntered$

				AdventureFileName$=AdventureNameEntered$

				GetAdventures()

				For i=0 To NofAdventureFileNames-1
					If AdventureFileName$=AdventureFileNamesListed$(i)
						ThisEditorMode=EditorMode

						AdventureNameSelected=i
						Repeat
						Until LeftMouseDown()=0
						StartMaster()

						If ThisEditorMode=12
							HubAdventuresFilenames$(HubSelectedAdventure)=AdventureCurrentArchiveToHubPrefix$()+AdventureFileNamesListed$(AdventureNameSelected)
							HubAdventuresMissing(HubSelectedAdventure)=False
							If HubSelectedAdventure>HubTotalAdventures
								HubTotalAdventures=HubSelectedAdventure
							EndIf
						EndIf
					EndIf
				Next
			EndIf
		EndIf
		waitflag=True
	EndIf

	If CanChangeAdventureCurrentArchive()
		If my>LetterHeight*6 And my<LetterHeight*7 And mx>LetterX(28) And mx<LetterX(44) And MouseDebounceFinished()
			If LeftMouse Or MouseScroll>0
				SetAdventureCurrentArchive(AdventureCurrentArchive+1)
				GetAdventures()
				If MouseScroll=0 Then MouseDebounceSet(10)
			EndIf
			If RightMouse Or MouseScroll<0
				SetAdventureCurrentArchive(AdventureCurrentArchive-1)
				GetAdventures()
				If MouseScroll=0 Then MouseDebounceSet(10)
			EndIf
		EndIf
		;If my>LetterHeight*6 And my<LetterHeight*7 And mx>LetterX(36) And AdventureCurrentArchive=0
		;	AdventureCurrentArchive=1
		;	GetAdventures()
		;EndIf
	EndIf

	If LeftMouse Or RightMouse Or MouseScroll<>0
		If MouseDebounceFinished()
			If mx<LetterX(12) And my>LetterHeight And my<LetterHeight*2 ;hubmode
				hubmode=Not hubmode
				If hubmode
					GetHubs()
				Else
					GetAdventures()
				EndIf

				If MouseScroll=0 Then MouseDebounceSet(10)
			EndIf
		EndIf
	EndIf

	If LeftMouse
		If EditorMode=5
			If mx>LetterX(36) And my>LetterHeight*28
				; change user
				StartUserSelectScreen()
				Repeat
				Until LeftMouseDown()=0
			EndIf

			If mx>LetterX(34) And my<LetterHeight*2
				; switch window/fullscreen
	;			DisplayFullScreen = Not DisplayFullScreen
	;			filed=WriteFile (globaldirname$+"\display-ed.wdf")
	;			If filed>0
	;
	;				WriteInt filed,DisplayFullScreen
	;				CloseFile filed
	;			EndIf
	;
	;			; and restart
	;			Cls
	;			Flip
	;			Print "Note: Screenmode will be switched upon next restart."
	;			Delay 4000

				;ShowMessage("Here we go!",1000)
				SetEditorMode(13)
				Repeat
				Until LeftMouseDown()=0
				;ShowMessage("We're here!",1000)

			EndIf

			If AdventureNameSelected>=0
				Repeat
				Until LeftMouseDown()=0
				SetEditorMode(6)
			EndIf
		ElseIf EditorMode=12
			If mx<LetterX(7) And my>LetterHeight*28
				; go back to hub menu
				SetEditorMode(11)
				If HubAdventuresFilenames$(HubSelectedAdventure)=""
					HubSelectedAdventure=-1
				EndIf
				Repeat
				Until LeftMouseDown()=0
			EndIf

			If AdventureNameSelected>=0
				Repeat
				Until LeftMouseDown()=0
				SetEditorMode(11)
				HubAdventuresFilenames$(HubSelectedAdventure)=AdventureCurrentArchiveToHubPrefix$()+AdventureFileNamesListed$(AdventureNameSelected+AdventureFileNamesListedStart)
				HubAdventuresMissing(HubSelectedAdventure)=False
				If HubSelectedAdventure>HubTotalAdventures
					HubTotalAdventures=HubSelectedAdventure
				EndIf
			EndIf
		EndIf

	EndIf

	If my>LetterHeight*8 And my<LetterHeight*28
		If MouseScroll<>0
			Speed=1
			If ShiftDown()
				Speed=10
			EndIf
			SetAdventureFileNamesListedStart(AdventureFileNamesListedStart-MouseScroll*Speed)
		EndIf
		If (mx<LetterX(2.5) Or mx>LetterX(41.5)) And LeftMouse
			If my>LetterHeight*8 And my<LetterHeight*12
				; Page Up
				AdventureFileNamesListPageUp()
				Repeat
				Until LeftMouseDown()=0
			ElseIf my>LetterHeight*24 And my<LetterHeight*28
				; Page Down
				AdventureFileNamesListPageDown()
				Repeat
				Until LeftMouseDown()=0
			EndIf
		EndIf
	EndIf

	If KeyPressed(201) ; page up
		AdventureFileNamesListPageUp()
	EndIf
	If KeyPressed(209) ; page down
		AdventureFileNamesListPageDown()
	EndIf

	RenderLetters()
	UpdateWorld
	RenderWorld

	FinishDrawing()
	If waitflag=True Delay 2000

End Function
Function AdventureSelectScreen2()

	mx=MouseX()
	my=MouseY()
	StartY=LetterHeight*9
	If mx>LetterX(15) And mx<LetterX(29) And my>StartY And my<StartY+LetterHeight*8
		Selected=(my-StartY-LetterHeight*0.5)/(LetterHeight*2)
	Else
		Selected=-1
	EndIf

	DisplayText2(Versiontext$,0,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("============================================",0,1,TextMenusR,TextMenusG,TextMenusB)

	If hubmode
		DisplayText2("               Hub Selected:",0.5,3,TextMenusR,TextMenusG,TextMenusB)
	Else
		DisplayText2("             Adventure Selected:",0,3,TextMenusR,TextMenusG,TextMenusB)
	EndIf

	AdventureFileName$=AdventureFileNamesListed$(AdventureNameSelected+AdventureFileNamesListedStart)
	DisplayText2(AdventureFileName$,22-Len(AdventureFileName$)/2,4,255,255,255)

	If Not hubmode
		If MasterFileExists()
			LoadMasterFile()
			DisplayText2(AdventureTitle$,22-Len(AdventureTitle$)/2,5,255,255,255)
		EndIf
	EndIf

	DisplayText2("               Choose Option:",0,6,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("============================================",0,7,TextMenusR,TextMenusG,TextMenusB)

	If AdventureCurrentArchive=AdventureCurrentArchiveCurrent Or AdventureCurrentArchive=AdventureCurrentArchiveDataAdventures
		If Selected=0
			DisplayText2("EDIT",20,9,255,255,255)
		Else
			DisplayText2("EDIT",20,9,155,155,155)
		EndIf
	EndIf
	If hubmode=False
		If AdventureCurrentArchive=AdventureCurrentArchiveCurrent
			If Selected=1
				DisplayText2("MOVE TO ARCHIVE",14.5,11,255,255,255)
			Else
				DisplayText2("MOVE TO ARCHIVE",14.5,11,155,155,155)
			EndIf
		ElseIf AdventureCurrentArchive=AdventureCurrentArchiveArchive
			If Selected=1
				DisplayText2("MOVE TO CURRENT",14.5,11,255,255,255)
			Else
				DisplayText2("MOVE TO CURRENT",14.5,11,155,155,155)
			EndIf
		Else
			If Selected=1
				DisplayText2("COPY TO CURRENT",14.5,11,255,255,255)
			Else
				DisplayText2("COPY TO CURRENT",14.5,11,155,155,155)
			EndIf
		EndIf
	EndIf

	If AdventureCurrentArchive=AdventureCurrentArchiveCurrent
		If Selected=2
			DisplayText2("DELETE",19,13,255,255,255)
		Else
			DisplayText2("DELETE",19,13,155,155,155)
		EndIf
	EndIf
	If Selected=3
		DisplayText2("CANCEL",19,15,255,255,255)
	Else
		DisplayText2("CANCEL",19,15,155,155,155)
	EndIf

	If LeftMouse And Selected<>-1
		Repeat
		Until LeftMouseDown()=0

		; Check again to make sure the mouse is still on the button.
		OldSelected=Selected

		mx=MouseX()
		my=MouseY()

		If mx>LetterX(15) And mx<LetterX(29) And my>StartY And my<StartY+LetterHeight*8
			Selected=(my-StartY-LetterHeight*0.5)/(LetterHeight*2)
		Else
			Selected=-1
		EndIf

		If Selected=OldSelected
			If selected=0 And AdventureCurrentArchive=AdventureCurrentArchiveCurrent Or AdventureCurrentArchive=AdventureCurrentArchiveDataAdventures
				If hubmode
					HubFileName$=AdventureFileNamesListed$(AdventureNameSelected+AdventureFileNamesListedStart)
					StartHub()
				Else
					AdventureFileName$=AdventureFileNamesListed$(AdventureNameSelected+AdventureFileNamesListedStart)
					MasterDialogListStart=0
					MasterLevelListStart=0
					StartMaster()
				EndIf
				Repeat
				Until LeftMouseDown()=0
			EndIf

			If selected=1
				ex$=AdventureFileNamesListed$(AdventureNameSelected+AdventureFileNamesListedStart)

				FromDir$=GetAdventureDir$()
				If AdventureCurrentArchive=AdventureCurrentArchiveCurrent
					ToDir$=GetAdventuresDir$(AdventureCurrentArchiveArchive)+ex$
				Else
					ToDir$=GetAdventuresDir$(AdventureCurrentArchiveCurrent)+ex$
				EndIf

				CreateDir ToDir$
				dirfile=ReadDir(FromDir$)
				Repeat
					ex2$=NextFile$(dirfile)
					If ex2$<>"" And ex2$<>"." And ex2$<>".."
						CopyFile FromDir$+ex2$,ToDir$+"\"+ex2$

						If AdventureCurrentArchive=AdventureCurrentArchiveCurrent Or AdventureCurrentArchive=AdventureCurrentArchiveArchive
							DeleteFile FromDir$+"\"+ex2$
						EndIf
					EndIf
				Until ex2$=""
				CloseDir dirfile

				If AdventureCurrentArchive=AdventureCurrentArchiveCurrent Or AdventureCurrentArchive=AdventureCurrentArchiveArchive
					DeleteDir FromDir$
				EndIf

				GetAdventures()

				SetEditorMode(5)
			EndIf
			If selected=2 And AdventureCurrentArchive=AdventureCurrentArchiveCurrent
				SetEditorMode(7)
			EndIf

			If selected=3
				SetEditorMode(5)
			EndIf
		EndIf
	EndIf

	RenderLetters()
	UpdateWorld
	RenderWorld

	FinishDrawing()
	If waitflag=True Delay 2000

End Function

Function AdventureSelectScreen3()

	MX=MouseX()
	my=MouseY()
	StartY=LetterHeight*9
	If mx>LetterX(15) And mx<LetterX(29) And my>StartY And my<StartY+LetterHeight*8
		Selected=(my-StartY-LetterHeight*0.5)/(LetterHeight*2)
	Else
		Selected=-1
	EndIf

	DisplayText2(Versiontext$,0,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("============================================",0,1,TextMenusR,TextMenusG,TextMenusB)

	If hubmode
		DisplayText2("               Hub Selected:",0.5,3,TextMenusR,TextMenusG,TextMenusB)
	Else
		DisplayText2("             Adventure Selected:",0,3,TextMenusR,TextMenusG,TextMenusB)
	EndIf
	DisplayText2(AdventureFileNamesListed$(AdventureNameSelected+AdventureFileNamesListedStart),22-Len(AdventureFileNamesListed$(AdventureNameSelected+AdventureFileNamesListedStart))/2,4,255,255,255)

	If hubmode
		DisplayText2("         DELETE HUB - ARE YOU SURE?",0,6,TextMenusR,TextMenusG,TextMenusB)
	Else
		DisplayText2("      DELETE ADVENTURE - ARE YOU SURE?",0,6,TextMenusR,TextMenusG,TextMenusB)
	EndIf
	DisplayText2("============================================",0,7,TextMenusR,TextMenusG,TextMenusB)

	If Selected=0
		DisplayText2("YES!",20,9,255,255,255)
	Else
		DisplayText2("YES!",20,9,155,155,155)
	EndIf
	If Selected=1
		DisplayText2("NO!!",20,11,255,255,255)
	Else
		DisplayText2("NO!!",20,11,155,155,155)
	EndIf

	If LeftMouse
		If selected=0
			ex$=AdventureFileNamesListed$(AdventureNameSelected+AdventureFileNamesListedStart)
			If hubmode
				dirfile=ReadDir(GlobalDirName$+"\Custom\Editing\Hubs\"+ex$)
				Repeat
					ex2$=NextFile$(dirfile)
					If ex2$<>"" And ex2$<>"." And ex2$<>".."
						DeleteFile GlobalDirName$+"\Custom\Editing\Hubs\"+ex$+"\"+ex2$
					EndIf
				Until ex2$=""
				CloseDir dirfile
				DeleteDir GlobalDirName$+"\Custom\Editing\Hubs\"+ex$
				GetHubs()
			Else
				TheDir$=GetAdventuresDir$(AdventureCurrentArchive)+ex$
				dirfile=ReadDir(TheDir$)
				Repeat
					ex2$=NextFile$(dirfile)
					If ex2$<>"" And ex2$<>"." And ex2$<>".."
						DeleteFile TheDir$+"\"+ex2$
					EndIf
				Until ex2$=""
				CloseDir dirfile
				DeleteDir TheDir$
				GetAdventures()
			EndIf

			Repeat
			Until LeftMouseDown()=0
			SetEditorMode(5)
		EndIf
		If selected=1
			SetEditorMode(5)
			Repeat
			Until LeftMouseDown()=0
		EndIf
	EndIf

	RenderLetters()
	UpdateWorld
	RenderWorld

	FinishDrawing()
	If waitflag=True Delay 2000

End Function

Function SetAdventureCurrentArchive(NewValue)

	AdventureCurrentArchive=NewValue
	If AdventureCurrentArchive<0
		AdventureCurrentArchive=MaxAdventureCurrentArchive
	ElseIf AdventureCurrentArchive>MaxAdventureCurrentArchive
		AdventureCurrentArchive=0
	EndIf

End Function

Function GetAdventureTitle$(ex$)

	AdventureFileName$=ex$
	If MasterFileExists()
		Return LoadAdventureTitle$()
	Else
		Return ""
	EndIf

End Function

Function GetAdventures()

	NofAdventureFileNames=0
	AdventureFileNamesListedStart=0

	TheDir$=GetAdventuresDir$(AdventureCurrentArchive)
	dirfile=ReadDir(TheDir$)

	Repeat
		ex$=NextFile$(dirfile)
		If ex$<>"." And ex$<>".." And ex$<>"" And FileType(TheDir$+ex$)=2
			; check if there's a hash or name is too long
			flag=True

			;For i=1 To Len(ex$)
			;	If Mid$(ex$,i,1)="#" Then flag=False
			;Next
			;If Len(ex$)>38 Then flag=False

			If flag=True
				; good file name - add to list
				AdventureFileNamesListed$(NofAdventureFileNames)=ex$

				; VERY slow to do this all at once.
				;AdventureTitlesListed$(NofAdventureFileNames)=GetAdventureTitle$(ex$)

				NofAdventureFileNames=NofAdventureFileNames+1
			EndIf
		EndIf
	Until ex$=""

	CloseDir dirfile

End Function

Function GetHubs()

	NofAdventureFileNames=0
	AdventureFileNamesListedStart=0
	AdventureCurrentArchive=AdventureCurrentArchiveCurrent

	dirfile=ReadDir(GlobalDirName$+"\Custom\Editing\Hubs")

	Repeat
		ex$=NextFile$(dirfile)
		If ex$<>"." And ex$<>".." And ex$<>"" And FileType(GlobalDirName$+"\Custom\Editing\Hubs\"+ex$)=2
			; check if there's a hash or name is too long
			flag=True
			For i=1 To Len(ex$)
				If Mid$(ex$,i,1)="#" Then flag=False
			Next
			If Len(ex$)>38 Then flag=False
			If flag=True
				; good file name - add to list
				AdventureFileNamesListed$(NofAdventureFileNames)=ex$
				NofAdventureFileNames=NofAdventureFileNames+1
			EndIf
		EndIf
	Until ex$=""

	CloseDir dirfile

End Function

Function LevelExists(levelnumber)

	If FileType(GetAdventureDir$()+Str$(levelnumber)+".wlv")=1
		Return True
	Else
		Return False
	EndIf

End Function

Function DialogExists(dialognumber)

	If FileType(GetAdventureDir$()+Str$(dialognumber)+".dia")=1
		Return True
	Else
		Return False
	EndIf

End Function

Function AdventureTitleWithoutAuthor$(ex$)

	exWithoutUsername$=""
	FoundHash=False
	For i=1 To Len(ex$)
		TheChar$=Mid$(ex$,i,1)
		If FoundHash
			exWithoutUsername$=exWithoutUsername$+TheChar$
		ElseIf TheChar$="#"
			FoundHash=True
		EndIf
	Next

	If FoundHash=False
		Return ex$
	Else
		Return exWithoutUsername$
	EndIf

End Function

Function GetAuthorFromAdventureTitle$(ex$)

	TheUsername$=""
	FoundHash=False
	For i=1 To Len(ex$)
		TheChar$=Mid$(ex$,i,1)
		If TheChar$="#"
			FoundHash=True
		ElseIf Not FoundHash
			TheUsername$=TheUsername$+TheChar$
		EndIf
	Next

	If FoundHash=False
		Return EditorUserName$
	Else
		Return TheUsername$
	EndIf

End Function

Function AdventureCurrentArchiveToHubPrefix$()

	Select AdventureCurrentArchive
	Case AdventureCurrentArchiveArchive
		Return ":Archive:"
	Case AdventureCurrentArchivePlayer
		Return ":Player:"
	Case AdventureCurrentArchiveDataAdventures
		Return ":Data:"
	Default
		Return ""
	End Select

End Function

Function GetHubAdventurePath$(Filename$)

	If Left$(Filename$,9)=":Archive:"
		Return GetAdventuresDir$(AdventureCurrentArchiveArchive)+RemoveLeft$(Filename$,9)
	ElseIf Left$(Filename$,8)=":Player:"
		Return GetAdventuresDir$(AdventureCurrentArchivePlayer)+RemoveLeft$(Filename$,8)
	ElseIf Left$(Filename$,6)=":Data:"
		Return GetAdventuresDir$(AdventureCurrentArchiveDataAdventures)+RemoveLeft$(Filename$,6)
	Else
		Return GetAdventuresDir$(AdventureCurrentArchiveCurrent)+Filename$
	EndIf

End Function

Function TrimHubAdventureName$(Filename$)

	If Left$(Filename$,9)=":Archive:"
		AdventureCurrentArchive=AdventureCurrentArchiveArchive
		Return RemoveLeft$(Filename$,9)
	ElseIf Left$(Filename$,8)=":Player:"
		AdventureCurrentArchive=AdventureCurrentArchivePlayer
		Return RemoveLeft$(Filename$,8)
	ElseIf Left$(Filename$,6)=":Data:"
		AdventureCurrentArchive=AdventureCurrentArchiveDataAdventures
		Return RemoveLeft$(Filename$,6)
	Else
		AdventureCurrentArchive=AdventureCurrentArchiveCurrent
		Return Filename$
	EndIf

End Function

Function IsHubMissingAdventures()

	For i=0 To HubTotalAdventures
		If HubAdventuresMissing(i)=True
			Return True
		EndIf
	Next

	Return False

End Function