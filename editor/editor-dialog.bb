Function SetInterChange(i)

	If i<0
		i=0
	ElseIf i>MaxInterChanges
		i=MaxInterChanges
	EndIf

	If i<>WhichInterChange
		SetAnswer(0)
		ColEffect=-1
		TxtEffect=-1
	EndIf

	WhichInterChange=i

	If WhichInterChange>=NofInterChanges
		NofInterChanges=WhichInterChange+1
	EndIf

	; for old dialogs
	RemoveDanglingDialogTextCommands()
	DeduplicateDialogTextCommands()

End Function

Function SetAnswer(i)

	If i<0
		i=0
	ElseIf i>MaxReply
		i=MaxReply
	EndIf

	If i<>WhichAnswer
		ColEffect=-1
		TxtEffect=-1
	EndIf

	WhichAnswer=i

End Function

Function SetAskabout(i)

	If i<0
		i=0
	ElseIf i>100
		i=100
	EndIf

	If i<>WhichAskabout
		ColEffect=-1
		TxtEffect=-1
	EndIf

	WhichAskabout=i

End Function

Function StartDialog()

	SetEditorMode(9)

	Camera1Proj=0
	Camera2Proj=0
	Camera3Proj=0
	Camera4Proj=0
	CameraProj=1
	UpdateCameraProj()

End Function

Function AccessDialog(TargetDialog)

	CurrentDialog=TargetDialog

	SetInterChange(0)
	WhichAnswer=0
	WhichAskAbout=0

	ColEffect=-1
	TxtEffect=-1

	; check existence of this dialog file
	If DialogExists(CurrentDialog)
		LoadDialogFile()
	Else
		NewDialog()
	EndIf

End Function

Function NewDialog()

	ClearDialogFile()

	UnsavedChanges=0

End Function

Function TryPreviewDialog(TargetDialog)

	If DialogExists(TargetDialog)
		If PreviewCurrentDialog<>TargetDialog
			PreviewCurrentDialog=TargetDialog
			PreviewDialogFile()
		EndIf
		Return True
	Else
		Return False
	EndIf

End Function

Function DialogGetFirstLine$(Interchange)

	For j=0 To 6
		tex$=PreviewInterChangeTextLine$(Interchange,j)
		If tex$<>""
			Return tex$
		EndIf
	Next

	Return "< EMPTY >"

End Function

Function DialogGetFirstTwoLines$(Interchange)

	Result$=""
	ResultCount=0
	For j=0 To 6
		tex$=PreviewInterChangeTextLine$(Interchange,j)
		If tex$<>""
			If ResultCount=0
				Result$=tex$
			Else
				Result$=Result$+" "+tex$
				Return Result$
			EndIf
			ResultCount=ResultCount+1
		EndIf
	Next

	If Result$=""
		Return "< EMPTY >"
	Else
		Return Result$
	EndIf

End Function

Function CurrentDialogGetFirstTwoLines$(Interchange)

	Result$=""
	ResultCount=0
	For j=0 To 6
		tex$=InterChangeTextLine$(Interchange,j)
		If tex$<>""
			If ResultCount=0
				Result$=tex$
			Else
				Result$=Result$+" "+tex$
				Return Result$
			EndIf
			ResultCount=ResultCount+1
		EndIf
	Next

	If Result$=""
		Return "< EMPTY >"
	Else
		Return Result$
	EndIf

End Function

Function PreviewDialog$(DialogNumber,InterchangeNumber)

	If TryPreviewDialog(DialogNumber)
		If InterchangeNumber>=0 And InterchangeNumber<=100
			Return DialogGetFirstTwoLines$(InterchangeNumber)
		Else
			Return "< INVALID INTERCHANGE >"
		EndIf
	Else
		Return "< DIALOG DOES NOT EXIST >"
	EndIf

End Function

Function PreviewCurrentDialog$(InterchangeNumber)

	If InterchangeNumber>=0 And InterchangeNumber<=100
		Return CurrentDialogGetFirstTwoLines$(InterchangeNumber)
	Else
		Return "< INVALID INTERCHANGE >"
	EndIf

End Function

Function PreviewAskAbout$(DialogNumber,AskAboutNumber)

	If TryPreviewDialog(DialogNumber)
		If AskAboutNumber>-1 And AskAboutNumber<101
			tex$=PreviewAskAboutText$(AskAboutNumber)
			If tex$=""
				Return "< EMPTY >"
			Else
				Return tex$
			EndIf
		Else
			Return "< INVALID ASKABOUT >"
		EndIf
	Else
		Return "< DIALOG DOES NOT EXIST >"
	EndIf

End Function

Function DialogTextCommandIsColor(k)

	Return Left$(DialogTextCommand$(WhichInterChange,k),1)="C"

End Function

Function DialogTextCommandIsEffect(k)

	Return Left$(DialogTextCommand$(WhichInterChange,k),1)="E"

End Function

Function CopyDialogTextCommand(Source,Dest)

	DialogTextCommandPos(WhichInterChange,Dest)=DialogTextCommandPos(WhichInterChange,Source)
	DialogTextCommand$(WhichInterChange,Dest)=DialogTextCommand$(WhichInterChange,Source)

End Function

Function SwapDialogTextCommand(Source,Dest)

	temp=DialogTextCommandPos(WhichInterChange,Dest)
	DialogTextCommandPos(WhichInterChange,Dest)=DialogTextCommandPos(WhichInterChange,Source)
	DialogTextCommandPos(WhichInterChange,Source)=temp

	temp2$=DialogTextCommand$(WhichInterChange,Dest)
	DialogTextCommand$(WhichInterChange,Dest)=DialogTextCommand$(WhichInterChange,Source)
	DialogTextCommand$(WhichInterChange,Source)=temp2$

End Function

Function DeleteDialogTextCommand(k)

	For j=k+1 To NofTextCommands(WhichInterChange)-1
		CopyDialogTextCommand(j,j-1)
	Next

	NofTextCommands(WhichInterChange)=NofTextCommands(WhichInterChange)-1

End Function

Function AddDialogTextCommand(x,y,command$)

	DialogTextCommandPos(WhichInterChange,NofTextCommands(WhichInterChange))=x+(y*CharactersPerLine)
	DialogTextCommand$(WhichInterChange,NofTextCommands(WhichInterChange))=command$
	NofTextCommands(WhichInterChange)=NofTextCommands(WhichInterChange)+1

	AddUnsavedChange()

End Function

Function ReplaceDialogTextCommand(k,NewEffect$)

	DialogTextCommand$(WhichInterChange,k)=NewEffect$

	AddUnsavedChange()

End Function

Function SortDialogTextCommands()

	; insertion sort
	i=1
	While i<NofTextCommands(WhichInterChange)
		j=i
		While DialogTextCommandPos(WhichInterChange,j-1)>DialogTextCommandPos(WhichInterChange,j)
			SwapDialogTextCommand(j,j-1)
			j=j-1
			If j=0
				Exit ; Needed instead of a conditional because Blitz3D doesn't short-circuit And...
			EndIf
		Wend
		i=i+1
	Wend

End Function

Function DeduplicateDialogTextCommands()

	SortDialogTextCommands()

	; remove duplicates (except for rainbow)
	; start with initial states for a dialog
	LatestColor$="CWHI" ; white color
	LatestEffect$="ENON" ; no effect
	For k=0 To NofTextCommands(WhichInterChange)-1
		If DialogTextCommandIsColor(k)
			CurrentColor$=DialogTextCommand$(WhichInterChange,k)
			If CurrentColor$=LatestColor$ And DialogTextCommand$(WhichInterChange,k)<>"CRAI" ; rainbow
				DeleteDialogTextCommand(k)
				k=k-1
			Else
				LatestColor$=CurrentColor$
			EndIf
		ElseIf DialogTextCommandIsEffect(k)
			CurrentEffect$=DialogTextCommand$(WhichInterChange,k)
			If CurrentEffect$=LatestEffect$
				DeleteDialogTextCommand(k)
				k=k-1
			Else
				LatestEffect$=CurrentEffect$
			EndIf
		EndIf
	Next

End Function

Function RemoveDanglingDialogTextCommands()

	For k=0 To NofTextCommands(WhichInterChange)-1
		thePos=DialogTextCommandPos(WhichInterChange,k)
		x=thePos Mod CharactersPerLine
		y=thePos/CharactersPerLine
		If Len(InterChangeTextLine$(WhichInterChange,y))<=x
			DeleteDialogTextCommand(k)
			k=k-1
		EndIf
	Next

End Function

Function DialogMainLoop()

	DialogTimer=DialogTimer+1

	adj=1
	If ShiftDown() Then adj=10

	If HotkeySave()
		SaveDialogFile()
;	ElseIf HotKeyOpen()
;		If AskToSaveDialogAndExit()
;			OpenTypedDialog()
;		EndIf
	EndIf

	If KeyPressed(1) ; Esc key
		If AskToSaveDialogAndExit()
			ResumeMaster()
		EndIf
	EndIf

	If CtrlDown()
		If KeyPressed(209) ; Ctrl+PageDown
			If AskToSaveDialogAndExit()
				AccessDialog(CurrentDialog+1)
			EndIf
		ElseIf KeyPressed(201) ; Ctrl+PageUp
			If AskToSaveDialogAndExit()
				AccessDialog(CurrentDialog-1)
			EndIf
		EndIf
	EndIf

	DisplayText2("Adventure: "+Left$(AdventureFileName$,20),0,0,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("Dialog #"+Str$(CurrentDialog),36-Len(Str$(CurrentDialog)),0,TextMenusR,TextMenusG,TextMenusB)

	DisplayText2("W",39,3,255,255,255)
	DisplayText2("G",41,3,195,195,195)
	DisplayText2("R",43,3,255,100,100)
	DisplayText2("O",39,4,255,155,0)
	DisplayText2("Y",41,4,255,255,0)
	DisplayText2("G",43,4,0,255,0)
	DisplayText2("B",39,5,0,255,255)
	DisplayText2("I",41,5,130,130,255)
	DisplayText2("V",43,5,255,100,255)
	DisplayText2("R",39,6,Rand(0,255),Rand(0,255),Rand(0,255))
	DisplayText2("B",41,6,GetAnimatedFlashing(DialogTimer),GetAnimatedFlashing(DialogTimer),GetAnimatedFlashing(DialogTimer))
	DisplayText2("W",43,6,GetAnimatedFlashing(DialogTimer),60,60)

	DisplayText2("NO SH",39,7,255,255,255)
	DisplayText2("JI WA",39,8,255,255,255)
	DisplayText2("BO ZO",39,9,255,255,255)
	DisplayText2("ZS CR",39,10,255,255,255)
	DisplayText2("EI UD",39,11,255,255,255)
	DisplayText2("LR RT",39,12,255,255,255)

	DisplayText2("CLEAR",39,14,255,255,0)
	DisplayText2("COPY",39.5,16,255,255,0)
	DisplayText2("PASTE",39,18,255,255,0)
	DisplayText2("ERASE",39,20,255,255,0)

	If KeyDown(62) ; F4 Key
		; LOL
		DisplayText2("ALT+F4",-7,8,255,255,0)
	EndIf

	AutofillLabel$="AUTOFILL EVERY MISSING FIRST ANSWER"
	DisplayText2(AutofillLabel$,0,19,255,255,0)

	If MouseX()>LetterX(38) And MouseY()>23*LetterHeight And MouseY()<25*LetterHeight
		DisplayText2("CANCEL",38.2,23,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("+EXIT",38.7,24,TextMenusR,TextMenusG,TextMenusB)
	Else
		DisplayText2("CANCEL",38.2,23,255,255,255)
		DisplayText2("+EXIT",38.7,24,255,255,255)

	EndIf

	If MouseX()>LetterX(38) And MouseY()>27*LetterHeight
		DisplayText2("SAVE",39.2,27,TextMenusR,TextMenusG,TextMenusB)
		DisplayText2("+EXIT",38.7,28,TextMenusR,TextMenusG,TextMenusB)
	Else
		DisplayText2("SAVE",39.2,27,255,255,255)
		DisplayText2("+EXIT",38.7,28,255,255,255)

	EndIf

	If ColEffect>-1
		DisplayText2("_",39+2*(ColEffect Mod 3),3+(ColEffect/3),TextMenusR,TextMenusG,TextMenusB)
	EndIf
	If TxtEffect>-1
		DisplayText2("__",39+3*(TxtEffect Mod 2),7+(TxtEffect/2),TextMenusR,TextMenusG,TextMenusB)
	EndIf

	DisplayText2("--------------------------------------------",0,1,TextMenusR,TextMenusG,TextMenusB)

	;DisplayText2("InterChange #"+Str$(WhichInterChange),20,0,TextMenusR,TextMenusG,TextMenusB)
	;DisplayText2(" COPY                PASTE",0,2,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("----- INTERCHANGE #"+Str$(WhichInterChange)+" -----",0,3,TextMenusR,TextMenusG,TextMenusB)

	DisplayText2("--------------------------------------",0,12,TextMenusR,TextMenusG,TextMenusB) ; Formerly 0,11

	DisplayText2("----- ANSWER #"+Str$(WhichAnswer)+" -----",0,13,TextMenusR,TextMenusG,TextMenusB)

	DisplayText2("FNC  Data  CMD  Dat1  Dat2  Dat3  Dat4",0,15,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2(Str$(InterChangeReplyFunction(WhichInterChange,WhichAnswer)),0,16,255,255,255)		;**
	DisplayText2(Str$(InterChangeReplyData(WhichInterChange,WhichAnswer)),5,16,255,255,255)
	DisplayText2(Str$(InterChangeReplyCommand(WhichInterChange,WhichAnswer)),11,16,255,255,255)
	DisplayText2(Str$(InterChangeReplyCommandData(WhichInterChange,WhichAnswer,0)),17,16,255,255,255)
	DisplayText2(Str$(InterChangeReplyCommandData(WhichInterChange,WhichAnswer,1)),23,16,255,255,255)
	DisplayText2(Str$(InterChangeReplyCommandData(WhichInterChange,WhichAnswer,2)),29,16,255,255,255)
	DisplayText2(Str$(InterChangeReplyCommandData(WhichInterChange,WhichAnswer,3)),35,16,255,255,255)
	DisplayText2("--------------------------------------",0,17,TextMenusR,TextMenusG,TextMenusB)

	DisplayText2("----- ASKABOUT #"+Str$(WhichAskAbout)+" -----",0,22,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2("Active    InterChange   Repeat",0,24,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2(Str$(AskAboutActive(WhichAskAbout)),0,25,255,255,255)

	DisplayText2(Str$(AskAboutInterChange(WhichAskAbout)),12,25,255,255,255)
	DisplayText2(Str$(AskAboutRepeat(WhichAskAbout)),24,25,255,255,255)
	DisplayText2("AskAbout Title Line:",0,27,TextMenusR,TextMenusG,TextMenusB)
	DisplayText2(AskAboutTopText$,0,28,255,255,255)
;	DisplayText2("--------------------------------------",0,28,TextMenusR,TextMenusG,TextMenusB)

	For i=0 To 37

		For j=0 To MaxInterChangeTextLine
			AddLetter(Asc("X")-32,-.97+i*.045,.5-j*.05,1,0,.04,0,0,0,0,0,0,0,0,0,TextMenuXR,TextMenuXG,TextMenuXB)
		Next
		AddLetter(Asc("X")-32,-.97+i*.045,.5-10*.05,1,0,.04,0,0,0,0,0,0,0,0,0,TextMenuXR,TextMenuXG,TextMenuXB)
		AddLetter(Asc("X")-32,-.97+i*.045,.5-19*.05,1,0,.04,0,0,0,0,0,0,0,0,0,TextMenuXR,TextMenuXG,TextMenuXB)
		AddLetter(Asc("X")-32,-.97+i*.045,.5-24*.05,1,0,.04,0,0,0,0,0,0,0,0,0,TextMenuXR,TextMenuXG,TextMenuXB)

	Next

	; Display
	DialogCurrentRed=255
	DialogCurrentGreen=255
	DialogCurrentBlue=255
	DialogCurrentEffect=0
	totalletters=0
	For j=0 To MaxInterChangeTextLine
		For i=0 To Len(InterChangeTextLine$(WhichInterChange,j))
			; check special commands
			For k=0 To NofTextCommands(WhichInterChange)-1
				If DialogTextCommandPos(WhichInterChange,k)=j*38+i
					; yup - enact
					Select DialogTextCommand$(WhichInterChange,k)
					Case "CWHI"
						DialogCurrentRed=255
						DialogCurrentGreen=255
						DialogCurrentBlue=255
					Case "CGRY"
						DialogCurrentRed=195
						DialogCurrentGreen=195
						DialogCurrentBlue=195
					Case "CRED"
						DialogCurrentRed=255
						DialogCurrentGreen=100
						DialogCurrentBlue=100
					Case "CORA"
						DialogCurrentRed=255
						DialogCurrentGreen=155
						DialogCurrentBlue=000
					Case "CYEL"
						DialogCurrentRed=255
						DialogCurrentGreen=255
						DialogCurrentBlue=000
					Case "CGRE"
						DialogCurrentRed=0
						DialogCurrentGreen=255
						DialogCurrentBlue=0
					Case "CCYA"
						DialogCurrentRed=0
						DialogCurrentGreen=255
						DialogCurrentBlue=255
					Case "CBLU"
						DialogCurrentRed=130
						DialogCurrentGreen=130
						DialogCurrentBlue=255
					Case "CPUR"
						DialogCurrentRed=255
						DialogCurrentGreen=100
						DialogCurrentBlue=255
					Case "CRAI"
						DialogCurrentRed=Rand(0,255)
						DialogCurrentGreen=Rand(0,255)
						DialogCurrentBlue=Rand(0,255)
					Case "CBLI"
						DialogCurrentRed=GetAnimatedFlashing(DialogTimer)
						DialogCurrentGreen=GetAnimatedFlashing(DialogTimer)
						DialogCurrentBlue=GetAnimatedFlashing(DialogTimer)
					Case "CWAR"
						DialogCurrentRed=GetAnimatedFlashing(DialogTimer)
						DialogCurrentGreen=60
						DialogCurrentBlue=60
					Case "ENON"
						DialogCurrentEffect=0
					Case "ESHI"
						DialogCurrentEffect=1
					Case "EJIT"
						DialogCurrentEffect=2
					Case "EWAV"
						DialogCurrentEffect=3
					Case "EBOU"
						DialogCurrentEffect=4
					Case "EZOO"
						DialogCurrentEffect=5
					Case "EZSH"
						DialogCurrentEffect=6
					Case "ECIR"
						DialogCurrentEffect=7
					Case "EEIG"
						DialogCurrentEffect=8
					Case "EUPD"
						DialogCurrentEffect=9
					Case "ELER"
						DialogCurrentEffect=10
					Case "EROT"
						DialogCurrentEffect=11

					End Select
				EndIf
			Next

			size#=1.0
			spacing#=1.0
			angle#=0.0
			xoff#=0.0
			yoff#=0.0
			rot#=0.0
			Select DialogCurrentEffect
			Case 1
				xoff#=Rnd(-.1,.1)
			Case 2
				xoff#=Rnd(-.15,.15)
				yoff#=Rnd(-.1,.1)
			Case 3
				yoff#=0.2*Sin((totalletters+dialogtimer)*10)
			Case 4
				size=1.0+0.3*Sin((totalletters+dialogtimer)*10)
				spacing=1.0/size
			Case 5
				size=1.4
				spacing=1.0/size

			Case 6
				size=1.4
				spacing=1.0/size

				xoff#=Rnd(-.15,.15)
				yoff#=Rnd(-.1,.1)
			Case 7
				xoff#=Cos(dialogtimer*4)
				yoff#=Sin(dialogtimer*4)
			Case 8
				xoff#=Cos(dialogtimer*2)
				yoff#=Sin(dialogtimer*4)
			Case 9
				yoff#=Sin(dialogtimer*8)
			Case 10
				xoff#=Cos(dialogtimer*8)
			Case 11
				If Abs((-dialogtimer*8+(i+j*75)*10)) Mod 3600 <3400
					size=1.0

				Else
					size=1.3
				EndIf
				spacing=1.0/size
			;	yoff#=0.2*Sin(dialogtimer*8+i*180)

			End Select

			charx#=(i)+xoff
			chary#=YOffset/2.0+j+yoff

			AddLetter(Asc(Mid$(InterChangeTextLine$(WhichInterChange,j),i+1,1))-32,(-.97+charx*.045*size*spacing)/1.0,(.5-chary*.05*size*spacing)/1.0,1.0,rot,.04*size/1.0,0,0,0,0,0,0,0,0,0,dialogcurrentred,dialogcurrentgreen,dialogcurrentblue)

 				totalletters=totalletters+1
		;	AddLetter(Asc(Mid$(InterChangeTextLine$(WhichInterChange,j),i+1,1))-32,-.97+i*.045,.5-j*.05,1,0,.04,0,0,0,0,0,0,0,0,0,DialogCurrentRed,DialogCurrentGreen,DialogCurrentBlue)
		Next
	Next

	For i=0 To Len(InterChangeReplyText$(WhichInterChange,WhichAnswer))
		AddLetter(Asc(Mid$(InterChangeReplyText$(WhichInterChange,WhichAnswer),i+1,1))-32,-.97+i*.045,.5-(10)*.05,1,0,.04,0,0,0,0,0,0,0,0,0,255,255,255)
	Next

	For i=0 To Len(AskAboutText$(WhichAskAbout))
		AddLetter(Asc(Mid$(AskAboutText$(WhichAskAbout),i+1,1))-32,-.97+i*.045,.5-(19)*.05,1,0,.04,0,0,0,0,0,0,0,0,0,255,255,255)
	Next
	For i=0 To Len(AskAboutTopText$)
		AddLetter(Asc(Mid$(AskAboutTopText$,i+1,1))-32,-.97+i*.045,.5-(24)*.05,1,0,.04,0,0,0,0,0,0,0,0,0,255,255,255)
	Next

	; Mouse
	MouseTextEntryTrackMouseMovement()
	; Mouse Pos
	Entering=0

	x=GetMouseLetterX()
	If MouseY()<LetterHeight*14
		y=(MouseY()-LetterHeight*5)/LetterHeight
	Else If MouseY()<LetterHeight*15
		y=(MouseY()-LetterHeight*4.8)/LetterHeight ; 4.5
	Else
		y=(MouseY()-LetterHeight*4.6)/LetterHeight ; 4
	EndIf

	debug1=MouseY()
	debug2=y

	; cursor
	If x<CharactersPerLine And MouseY()>=LetterHeight*4 And y<8 And y>-1
		Entering=1
		If x>Len(InterChangeTextLine$(WhichInterChange,y)) Then x=Len(InterChangeTextLine$(WhichInterChange,y))
		If DialogTimer Mod 50 <25 Or OldX<>x Or OldY<>y
			AddLetter(Asc("_")-32,-.97+x*.045,.5-y*.05,1,0,.05,0,0,0,0,0,0,0,0,0,255,255,255)
		EndIf
		; Effects and Colours
		MouseButtonUsed=0
		If MouseDown(1)
			MouseButtonUsed=1
		EndIf
		If MouseDown(2)
			MouseButtonUsed=2
		EndIf
		If MouseButtonUsed<>0 And x<Len(InterChangeTextLine$(WhichInterChange,y))
			If ColEffect>=0 Or MouseButtonUsed=2
				If MouseButtonUsed=1
					Effect$=CCommands(ColEffect)
				Else
					Effect$="CWHI"
				EndIf
				; check if already one there
				flag7=False
				For k=0 To NofTextCommands(WhichInterChange)-1
					If DialogTextCommandPos(WhichInterChange,k)=x+(y*CharactersPerLine) And DialogTextCommandIsColor(k)
						; yes, replace
						flag7=True
						ReplaceDialogTextCommand(k,Effect$)
					EndIf
				Next
				If flag7=False
					; add new
					AddDialogTextCommand(x,y,Effect$)
				EndIf
			EndIf
			If TxtEffect>=0 And MouseButtonUsed=1
				; check if already one there
				flag7=False
				For k=0 To NofTextCommands(WhichInterChange)-1
					If DialogTextCommandPos(WhichInterChange,k)=x+(y*CharactersPerLine) And DialogTextCommandIsEffect(k)
						; yes, replace
						flag7=True
						ReplaceDialogTextCommand(k,TCommands(TxtEffect))
					EndIf
				Next
				If flag7=False
					; add new
					AddDialogTextCommand(x,y,TCommands(TxtEffect))
				EndIf

			EndIf
			;ColEffect=-1
			;TxtEffect=-1

			DeduplicateDialogTextCommands()

		EndIf

	EndIf
	If x<CharactersPerLine And y=10
		Entering=2
		If x>Len(InterChangeReplyText$(WhichInterChange,WhichAnswer)) Then x=Len(InterChangeReplyText$(WhichInterChange,WhichAnswer))
		If DialogTimer Mod 50 <25 Or OldX<>x Or OldY<>y
			AddLetter(Asc("_")-32,-.97+x*.045,.5-y*.05,1,0,.05,0,0,0,0,0,0,0,0,0,255,255,255)
		EndIf
	EndIf

	If x<CharactersPerLine And y=19
		Entering=3
		If x>Len(AskAboutText$(WhichAskAbout)) Then x=Len(AskAboutText$(WhichAskAbout))
		If DialogTimer Mod 50 <25 Or OldX<>x Or OldY<>y
			AddLetter(Asc("_")-32,-.97+x*.045,.5-y*.05,1,0,.05,0,0,0,0,0,0,0,0,0,255,255,255)
		EndIf
	EndIf

	If x<CharactersPerLine And y=24
		Entering=4
		If x>Len(AskAboutTopText$) Then x=Len(AskAboutTopText$)
		If DialogTimer Mod 50 <25 Or OldX<>x Or OldY<>y
			AddLetter(Asc("_")-32,-.97+x*.045,.5-y*.05,1,0,.05,0,0,0,0,0,0,0,0,0,255,255,255)
		EndIf
	EndIf

	OldX=x
	OldY=y
	; entering text
	let=GetKey()
	If Entering=1

		InterChangeTextLine$(WhichInterChange,y)=MouseTextEntry$(InterChangeTextLine$(WhichInterChange,y),let,x,y,0,1)

		If MouseTextEntryDelete=True
			RemoveDanglingDialogTextCommands()
			DeduplicateDialogTextCommands()
		EndIf

	EndIf
	If Entering=2

		InterChangeReplyText$(WhichInterChange,WhichAnswer)=MouseTextEntry$(InterChangeReplyText$(WhichInterChange,WhichAnswer),let,x,y,0,1)

	EndIf
	If Entering=3

		AskaboutText$(WhichAskAbout)=MouseTextEntry$(AskaboutText$(WhichAskAbout),let,x,y,-8,1)

	EndIf
	If Entering=4

		AskaboutTopText$=MouseTextEntry$(AskaboutTopText$,let,x,y,-8,1)

	EndIf

	mb=0
	If LeftMouse mb=1
	If RightMouse mb=2
	Modified=mb<>0 Or MouseScroll<>0
	If mb>0
		; Change Adventure
		; Load/Save

		If MouseX()>LetterX(38) And MouseY()>LetterHeight*23 And MouseY()<LetterHeight*25
			If AskToSaveDialogAndExit()
				ClearDialogFile()
				ResumeMaster()
			EndIf
			Repeat
			Until LeftMouseDown()=0
		EndIf

		If MouseX()>LetterX(38) And MouseY()>LetterHeight*27
			SaveDialogAndExit()
			Repeat
			Until LeftMouseDown()=0

		EndIf

	EndIf

	RawInput=CtrlDown()
	DelayTime=10

	; Change InterChange
	If MouseY()>LetterHeight*3 And MouseY()<LetterHeight*4 And MouseX()>LetterX(5) And MouseX()<LetterX(21)
		target=AdjustInt("Interchange: ", WhichInterChange, 1, 10, DelayTime)
		SetInterChange(target)
	EndIf

	; Change Answer
	If MouseY()>LetterHeight*13 And MouseY()<LetterHeight*14 And MouseX()>LetterX(5) And MouseX()<LetterX(21)
		target=AdjustInt("Answer: ", WhichAnswer, 1, 10, DelayTime)
		SetAnswer(target)
	EndIf
	; Change AnswerData
	; thanks to tooltips this is now awesome
	If MouseY()>LetterHeight*15 And MouseY()<LetterHeight*17
		MouseXToUse=(MouseX()-LetterX(0))/(LetterWidth*6)
		TooltipX=MouseXToUse*LetterWidth*6+LetterX(0)
		TooltipY=LetterHeight*19.5
		Select MouseXToUse
		Case 0
			OldValue=InterChangeReplyFunction(WhichInterChange,WhichAnswer)
			InterChangeReplyFunction(WhichInterChange,WhichAnswer)=AdjustInt("FNC: ", InterChangeReplyFunction(WhichInterChange,WhichAnswer), 1, 10, DelayTime)
			If OldValue<>InterChangeReplyFunction(WhichInterChange,WhichAnswer)
				AddUnsavedChange()
			EndIf

			ShowTooltipCenterAligned(TooltipX, TooltipY, ReplyFunctionToName$(InterChangeReplyFunction(WhichInterChange,WhichAnswer)))
		Case 1
			OldValue=InterChangeReplyData(WhichInterChange,WhichAnswer)
			InterChangeReplyData(WhichInterChange,WhichAnswer)=AdjustInt("Data: ", InterChangeReplyData(WhichInterChange,WhichAnswer), 1, 10, DelayTime)
			If OldValue<>InterChangeReplyData(WhichInterChange,WhichAnswer)
				AddUnsavedChange()
			EndIf

			Fnc=InterChangeReplyFunction(WhichInterChange,WhichAnswer)
			tex$=ReplyFunctionToDataName$(Fnc)
			If Fnc=1 Or Fnc=2
				tex$=tex$+": "+PreviewCurrentDialog$(InterChangeReplyData(WhichInterchange,WhichAnswer))
			EndIf
			ShowTooltipCenterAligned(TooltipX, TooltipY, tex$)
		Case 2
			OldValue=InterChangeReplyCommand(WhichInterChange,WhichAnswer)
			InterChangeReplyCommand(WhichInterChange,WhichAnswer)=AdjustInt("CMD: ", InterChangeReplyCommand(WhichInterChange,WhichAnswer), 1, 10, DelayTime)
			If OldValue<>InterChangeReplyCommand(WhichInterChange,WhichAnswer)
				AddUnsavedChange()
			EndIf

			ShowTooltipCenterAligned(TooltipX, TooltipY, GetCommandName$(InterChangeReplyCommand(WhichInterChange,WhichAnswer)))
		Case 3
			OldValue=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,0)
			InterChangeReplyCommandData(WhichInterChange,WhichAnswer,0)=AdjustInt("Data1: ", InterChangeReplyCommandData(WhichInterChange,WhichAnswer,0), 1, 10, DelayTime)
			If OldValue<>InterChangeReplyCommandData(WhichInterChange,WhichAnswer,0)
				AddUnsavedChange()
			EndIf

			Cmd=InterChangeReplyCommand(WhichInterChange,WhichAnswer)
			Data1=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,0)

			tex$=GetCMDData1NameAndValue(Cmd, Data1, ": ")+WithJoinerIfNotEmpty$(GetCmdData1ExtraInfo$(Cmd,Data1),": ")
			ShowTooltipCenterAligned(TooltipX, TooltipY, tex$)
		Case 4
			OldValue=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,1)
			InterChangeReplyCommandData(WhichInterChange,WhichAnswer,1)=AdjustInt("Data2: ", InterChangeReplyCommandData(WhichInterChange,WhichAnswer,1), 1, 10, DelayTime)
			If OldValue<>InterChangeReplyCommandData(WhichInterChange,WhichAnswer,1)
				AddUnsavedChange()
			EndIf

			Cmd=InterChangeReplyCommand(WhichInterChange,WhichAnswer)
			Data1=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,0)
			Data2=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,1)

			tex$=GetCMDData2NameAndValue(Cmd, Data2, ": ")+WithJoinerIfNotEmpty$(GetCmdData2ExtraInfo$(Cmd,Data1,Data2),": ")
			ShowTooltipCenterAligned(TooltipX, TooltipY, tex$)
		Case 5
			OldValue=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,2)
			InterChangeReplyCommandData(WhichInterChange,WhichAnswer,2)=AdjustInt("Data3: ", InterChangeReplyCommandData(WhichInterChange,WhichAnswer,2), 1, 10, DelayTime)
			If OldValue<>InterChangeReplyCommandData(WhichInterChange,WhichAnswer,2)
				AddUnsavedChange()
			EndIf

			Cmd=InterChangeReplyCommand(WhichInterChange,WhichAnswer)
			Data1=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,0)
			Data2=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,1)
			Data3=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,2)

			tex$=GetCMDData3NameAndValue(Cmd, Data2, Data3, ": ")+WithJoinerIfNotEmpty$(GetCmdData3ExtraInfo$(Cmd,Data1,Data3),": ")
			ShowTooltipCenterAligned(TooltipX, TooltipY, tex$)
		Case 6
			OldValue=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,3)
			InterChangeReplyCommandData(WhichInterChange,WhichAnswer,3)=AdjustInt("Data4: ", InterChangeReplyCommandData(WhichInterChange,WhichAnswer,3), 1, 10, DelayTime)
			If OldValue<>InterChangeReplyCommandData(WhichInterChange,WhichAnswer,3)
				AddUnsavedChange()
			EndIf

			Cmd=InterChangeReplyCommand(WhichInterChange,WhichAnswer)
			Data4=InterChangeReplyCommandData(WhichInterChange,WhichAnswer,3)

			tex$=GetCMDData4NameAndValue(Cmd, Data4, ": ")
			ShowTooltipCenterAligned(TooltipX, TooltipY, tex$)
		End Select

		If Modified
			ColEffect=-1
			TxtEffect=-1
		EndIf
	EndIf

	; Change Askabout
	If MouseY()>LetterHeight*22 And MouseY()<LetterHeight*23 And MouseX()>LetterX(5) And MouseX()<LetterX(21)
		target=AdjustInt("AskAbout: ", WhichAskabout, 1, 10, DelayTime)
		SetAskabout(target)
	EndIf
	; Change AskaboutData
	If MouseY()>LetterHeight*24.5 And MouseY()<LetterHeight*26.5
		TooltipY=LetterHeight*28.5
		If MouseX()<LetterX(8)
			OldValue=AskAboutActive(WhichAskAbout)
			AskAboutActive(WhichAskAbout)=AdjustInt("Active: ", AskAboutActive(WhichAskAbout), 1, 10, DelayTime)
			If OldValue<>AskAboutActive(WhichAskAbout)
				AddUnsavedChange()
			EndIf

			ShowTooltipCenterAligned(LetterX(4),TooltipY,GetAskAboutActiveName$(AskAboutActive(WhichAskAbout)))

		Else If MouseX()<LetterX(22.5)
			OldValue=AskAboutInterChange(WhichAskAbout)
			AskAboutInterChange(WhichAskAbout)=AdjustInt("Interchange: ", AskAboutInterChange(WhichAskAbout), 1, 10, DelayTime)
			If OldValue<>AskAboutInterChange(WhichAskAbout)
				AddUnsavedChange()
			EndIf

			ShowTooltipCenterAligned(LetterX(15.25),TooltipY,"Destination Interchange: "+PreviewCurrentDialog$(AskAboutInterChange(WhichAskAbout)))
		Else If MouseX()<LetterX(38)
			OldValue=AskAboutRepeat(WhichAskAbout)
			AskAboutRepeat(WhichAskAbout)=AdjustInt("Repeat: ", AskAboutRepeat(WhichAskAbout), 1, 10, DelayTime)
			If OldValue<>AskAboutRepeat(WhichAskAbout)
				AddUnsavedChange()
			EndIf

			ShowTooltipCenterAligned(LetterX(27.5),TooltipY,GetAskAboutRepeatName$(AskAboutRepeat(WhichAskAbout)))
		EndIf

		If Modified
			ColEffect=-1
			TxtEffect=-1
		EndIf
	EndIf

	; Colours/Effects
	If LeftMouse=True And LeftMouseReleased=True
		LeftMouseReleased=False
		StartX=39
		For i=0 To 11
			If MouseX()>=LetterX(StartX+(i Mod 3)*2) And MouseX()<=LetterX(StartX+1+(i Mod 3)*2) And MouseY()>=LetterHeight*3 + LetterHeight*(i/3) And MouseY()<LetterHeight*4+LetterHeight*(i/3)
				ToggleColEffect(i)
			EndIf
		Next
		For i=0 To 11
			If MouseX()>=LetterX(StartX+(i Mod 2)*2.5) And MouseX()<=LetterX(StartX+2.5+(i Mod 2)*2.5) And MouseY()>=LetterHeight*7 + LetterHeight*(i/2) And MouseY()<LetterHeight*8+LetterHeight*(i/2)
				ToggleTxtEffect(i)
			EndIf
		Next
		If MouseX()>LetterX(StartX)
			If MouseY()>LetterHeight*14 And MouseY()<LetterHeight*15
				If GetConfirmation("Clear all text colors and effects on this interchange?")
					; clear text colors and effects
					For i=0 To NofTextCommands(WhichInterChange)-1
						DialogTextCommand$(WhichInterChange,i)=""
						DialogTextCommandpos(WhichInterChange,i)=-1
					Next
					NofTextCommands(WhichInterChange)=0

					AddUnsavedChange()
				EndIf
			ElseIf MouseY()>LetterHeight*16 And MouseY()<LetterHeight*17
				; copy interchange
				CopyInterChange(WhichInterChange)
			ElseIf MouseY()>LetterHeight*18 And MouseY()<LetterHeight*19
				; paste interchange
				If GetConfirmation("Pasting over this interchange will overwrite all of its text and answers.")
					PasteInterChange(WhichInterChange)
					AddUnsavedChange()
				EndIf
			ElseIf MouseY()>LetterHeight*20 And MouseY()<LetterHeight*21
				; paste interchange
				If GetConfirmation("Erase all text, answers, and effects on this interchange?")
					ClearInterChange(WhichInterChange)
					AddUnsavedChange()
				EndIf
			EndIf
		ElseIf MouseX()<LetterX(Len(AutofillLabel$)) And MouseY()>LetterHeight*19 And MouseY()<LetterHeight*20
			NewAnswer$=InputString$("Enter desired autofill answer text (leave blank to cancel): ")
			If NewAnswer$<>""
				For i=0 To MaxInterchanges
					If InterChangeIsEmpty(i)
						; Do nothing.
					ElseIf InterChangeReplyText$(i,0)=""
						InterChangeReplyText$(i,0)=NewAnswer$
						If i=MaxInterchanges
							InterChangeReplyFunction(i,0)=1
							InterChangeReplyData(i,0)=i
						ElseIf InterChangeIsEmpty(i+1) ; Repeated block because Blitz3D hates short circuit evaluation. Get me out of here already.
							InterChangeReplyFunction(i,0)=1
							InterChangeReplyData(i,0)=i
						Else
							InterChangeReplyFunction(i,0)=2
							InterChangeReplyData(i,0)=i+1
						EndIf
					EndIf
				Next
				AddUnsavedChange()
			EndIf
		EndIf
	EndIf

	If CtrlDown() ; ctrl+...
		If KeyPressed(17) Then ToggleColEffect(0) ; w: white
		If KeyPressed(18) Then ToggleColEffect(1) ; e: grey
		If KeyPressed(19) Then ToggleColEffect(2) ; r: red
		If KeyPressed(24) Then ToggleColEffect(3) ; o: orange
		If KeyPressed(21) Then ToggleColEffect(4) ; y: yellow
		If KeyPressed(34) Then ToggleColEffect(5) ; g: green
		If KeyPressed(48) Then ToggleColEffect(6) ; b: blue
		If KeyPressed(23) Then ToggleColEffect(7) ; i: indigo
		If KeyPressed(47) Then ToggleColEffect(8) ; v: violet
		If KeyPressed(30) Then ToggleColEffect(9) ; a: rainbow (all)
		If KeyPressed(33) Then ToggleColEffect(10) ; f: black+white (flashy)
		If KeyPressed(32) Then ToggleColEffect(11) ; d: warning (doomy)
		If KeyPressed(49) Then ToggleTxtEffect(0) ; n: none
		If KeyPressed(31) Then ToggleTxtEffect(1) ; s: shake
		If KeyPressed(36) Then ToggleTxtEffect(2) ; j: jitter
		If KeyPressed(45) Then ToggleTxtEffect(3) ; x: wave
		If KeyPressed(44) Then ToggleTxtEffect(4) ; z: bounce
		If KeyPressed(25) Then ToggleTxtEffect(5) ; p: zoom
		If KeyPressed(16) Then ToggleTxtEffect(6) ; q: zoom shake
		If KeyPressed(46) Then ToggleTxtEffect(7) ; c: circle
		If KeyPressed(50) Then ToggleTxtEffect(8) ; m: mobius
		If KeyPressed(22) Then ToggleTxtEffect(9) ; u: up+down
		If KeyPressed(38) Then ToggleTxtEffect(10) ; l: left+right
		If KeyPressed(20) Then ToggleTxtEffect(11) ; t: rt
	EndIf

	RenderLetters()
	RenderWorld()

	FinishDrawing()

End Function

Function ToggleColEffect(i)
	If ColEffect=i
		ColEffect=-1
	Else
		ColEffect=i
	EndIf
End Function

Function ToggleTxtEffect(i)
	If TxtEffect=i
		TxtEffect=-1
	Else
		TxtEffect=i
	EndIf
End Function

Function ClearDialogFile()
	; first clear all data
	NofInterchanges=1
	For i=0 To MaxInterChanges ;-1
		ClearInterChange(i)
	Next
	NofAskAbouts=0
	AskAboutTopText$=""
	For i=0 To MaxAskAbouts-1
		AskAboutText$(i)=""
		AskAboutActive(i)=-2
		AskAboutInterchange(i)=0
		AskAboutRepeat(i)=-1
	Next
End Function

Function ClearInterChange(i)

	NofInterChangeTextLines(i)=0
	For j=0 To MaxInterChangeTextLine
		InterChangeTextLine$(i,j)=""
	Next
	NofTextCommands(i)=0
	For j=0 To 199
		DialogTextCommand$(i,j)=""
		DialogTextCommandPos(i,j)=-1
	Next
	NofInterChangeReplies(i)=1
	For j=0 To MaxReply
		InterChangeReplyText$(i,j)=""

		; Make the default FNC end the dialog and return to this Interchange.
		InterChangeReplyFunction(i,j)=1
		InterChangeReplyData(i,j)=i

		InterChangeReplyCommand(i,j)=0
		For k=0 To 3
			InterChangeReplyCommandData(i,j,k)=0
		Next
	Next

End Function

Function CopyInterChange(i)

	CopiedNofInterChangeTextLines=NofInterChangeTextLines(i)
	For j=0 To MaxInterChangeTextLine
		CopiedInterChangeTextLine$(j)=InterChangeTextLine$(i,j)
	Next
	CopiedNofTextCommands=NofTextCommands(i)
	For j=0 To 199
		CopiedDialogTextCommand$(j)=DialogTextCommand$(i,j)
		CopiedDialogTextCommandPos(j)=DialogTextCommandPos(i,j)
	Next
	CopiedNofInterChangeReplies=NofInterChangeReplies(i)
	For j=0 To MaxReply
		CopiedInterChangeReplyText(j)=InterChangeReplyText$(i,j)

		CopiedInterChangeReplyFunction(j)=InterChangeReplyFunction(i,j)
		CopiedInterChangeReplyData(j)=InterChangeReplyData(i,j)

		CopiedInterChangeReplyCommand(j)=InterChangeReplyCommand(i,j)
		For k=0 To 3
			CopiedInterChangeReplyCommandData(j,k)=InterChangeReplyCommandData(i,j,k)
		Next
	Next

End Function

Function PasteInterChange(i)

	ClearInterChange(i)

	NofInterChangeTextLines(i)=CopiedNofInterChangeTextLines
	For j=0 To MaxInterChangeTextLine
		InterChangeTextLine$(i,j)=CopiedInterChangeTextLine$(j)
	Next
	NofTextCommands(i)=CopiedNofTextCommands
	For j=0 To 199
		DialogTextCommand$(i,j)=CopiedDialogTextCommand$(j)
		DialogTextCommandPos(i,j)=CopiedDialogTextCommandPos(j)
	Next
	NofInterChangeReplies(i)=CopiedNofInterChangeReplies
	For j=0 To MaxReply
		InterChangeReplyText$(i,j)=CopiedInterChangeReplyText(j)

		InterChangeReplyFunction(i,j)=CopiedInterChangeReplyFunction(j)
		InterChangeReplyData(i,j)=CopiedInterChangeReplyData(j)

		InterChangeReplyCommand(i,j)=CopiedInterChangeReplyCommand(j)
		For k=0 To 3
			InterChangeReplyCommandData(i,j,k)=CopiedInterChangeReplyCommandData(j,k)
		Next
	Next

End Function

Function InterChangeIsEmpty(i)

	For j=0 To MaxInterChangeTextLine
		If InterChangeTextLine$(i,j)<>""
			Return False
		EndIf
	Next

	For j=0 To MaxReply
		If InterChangeReplyText(i,j)<>""
			Return False
		EndIf
	Next

	Return True

End Function

Function ClearDialogPreview()
	; first clear all data
	PreviewNofInterchanges=1
	For i=0 To MaxInterChanges ;-1
		PreviewNofInterChangeTextLines(i)=0
		For j=0 To MaxInterChangeTextLine
			PreviewInterChangeTextLine$(i,j)=""
		Next
		PreviewNofTextCommands(i)=0
		For j=0 To 199
			PreviewDialogTextCommand$(i,j)=""
			PreviewDialogTextCommandPos(i,j)=-1
		Next
		PreviewNofInterChangeReplies(i)=1
		For j=0 To MaxReply
			PreviewInterChangeReplyText$(i,j)=""

			; Make the default FNC end the dialog and return to this Interchange.
			PreviewInterChangeReplyFunction(i,j)=1
			PreviewInterChangeReplyData(i,j)=i

			PreviewInterChangeReplyCommand(i,j)=0
			For k=0 To 3
				PreviewInterChangeReplyCommandData(i,j,k)=0
			Next
		Next

	Next
	PreviewNofAskAbouts=0
	PreviewAskAboutTopText$=""
	For i=0 To MaxAskAbouts-1
		PreviewAskAboutText$(i)=""
		PreviewAskAboutActive(i)=-2
		PreviewAskAboutInterchange(i)=0
		PreviewAskAboutRepeat(i)=-1
	Next
End Function

Function LoadDialogFile()

	ClearDialogFile()

	; yep - load
	file=ReadFile(GetAdventureDir$()+Str$(currentdialog)+".dia")

	NofInterchanges=ReadInt(file)
	For i=0 To NofInterchanges-1
		NofInterChangeTextLines(i)=ReadInt(file)
		For j=0 To NofInterChangeTextLines(i)-1
			InterChangeTextLine$(i,j)=ReadString$(file)
		Next
		NofTextCommands(i)=ReadInt(file)

		For j=0 To NofTextCommands(i)-1
			DialogTextCommand$(i,j)=ReadString$(file)
			DialogTextCommandPos(i,j)=ReadInt(file)
		Next
		NofInterChangeReplies(i)=ReadInt(file)
		For j=0 To NofInterChangeReplies(i)-1
			InterChangeReplyText$(i,j)=ReadString$(file)
			InterChangeReplyFunction(i,j)=ReadInt(file)
			InterChangeReplyData(i,j)=ReadInt(file)
			InterChangeReplyCommand(i,j)=ReadInt(file)
			For k=0 To 3
				InterChangeReplyCommandData(i,j,k)=ReadInt(file)
			Next
		Next
	Next
	NofAskAbouts=ReadInt(file)
	AskAboutTopText$=ReadString$(file)
	For i=0 To NofAskAbouts-1
		AskAboutText$(i)=ReadString$(File)
		AskAboutActive(i)=ReadInt(file)
		AskAboutInterchange(i)=ReadInt(file)
		AskAboutRepeat(i)=ReadInt(file)
	Next
	CloseFile file

	UnsavedChanges=0

End Function

Function PreviewDialogFile()

	ClearDialogPreview()

	; yep - load
	file=ReadFile(GetAdventureDir$()+Str$(PreviewCurrentDialog)+".dia")

	PreviewNofInterchanges=ReadInt(file)
	For i=0 To PreviewNofInterchanges-1
		PreviewNofInterChangeTextLines(i)=ReadInt(file)
		For j=0 To PreviewNofInterChangeTextLines(i)-1
			PreviewInterChangeTextLine$(i,j)=ReadString$(file)
		Next
		PreviewNofTextCommands(i)=ReadInt(file)

		For j=0 To PreviewNofTextCommands(i)-1
			PreviewDialogTextCommand$(i,j)=ReadString$(file)
			PreviewDialogTextCommandPos(i,j)=ReadInt(file)
		Next
		PreviewNofInterChangeReplies(i)=ReadInt(file)
		For j=0 To PreviewNofInterChangeReplies(i)-1
			PreviewInterChangeReplyText$(i,j)=ReadString$(file)
			PreviewInterChangeReplyFunction(i,j)=ReadInt(file)
			PreviewInterChangeReplyData(i,j)=ReadInt(file)
			PreviewInterChangeReplyCommand(i,j)=ReadInt(file)
			For k=0 To 3
				PreviewInterChangeReplyCommandData(i,j,k)=ReadInt(file)
			Next
		Next
	Next
	PreviewNofAskAbouts=ReadInt(file)
	PreviewAskAboutTopText$=ReadString$(file)
	For i=0 To PreviewNofAskAbouts-1
		PreviewAskAboutText$(i)=ReadString$(File)
		PreviewAskAboutActive(i)=ReadInt(file)
		PreviewAskAboutInterchange(i)=ReadInt(file)
		PreviewAskAboutRepeat(i)=ReadInt(file)
	Next
	CloseFile file

End Function

Function SaveDialogFile()

	file=WriteFile(GetAdventureDir$()+Str$(currentdialog)+".dia")

	;NofInterChanges=NofInterChanges+1 ; MS, why would you do this?

	WriteInt File,NofInterchanges

	For i=0 To NofInterchanges-1
		; calculuate nofinterchangetextlines
		For j=7 To 0 Step -1
			If InterChangeTextLine$(i,j)<>""
				NofInterChangeTextLines(i)=j+1
				j=-3
			EndIf
		Next
		If j=-1 NofInterChangeTextLines(i)=0

		If DialogKillLineEight
			If NofInterChangeTextLines(i)>7
				NofInterChangeTextLines(i)=7
			EndIf
		EndIf

		WriteInt File,NofInterChangeTextLines(i)
		For j=0 To NofInterChangeTextLines(i)-1
			WriteString file,InterChangeTextLine$(i,j)
		Next
		WriteInt File,NofTextCommands(i)
		For j=0 To NofTextCommands(i)-1
			WriteString file,DialogTextCommand$(i,j)
			WriteInt File,DialogTextCommandPos(i,j)
		Next

		; calculate nofinterchangereplies
		;For j=0 To 8
		;	If InterChangeReplyText$(i,j)=""
		;		NofInterChangeReplies(i)=j
		;		j=17
		;	EndIf
		;Next
		;If j=9 Then NofInterChangeReplies(i)=9

		For j=0 To MaxReply
			If InterChangeReplyText$(i,j)<>""
				NofInterChangeReplies(i)=j+1
			EndIf
		Next

		WriteInt File,NofInterChangeReplies(i)
		For j=0 To NofInterChangeReplies(i)-1
			WriteString file,InterChangeReplyText$(i,j)
			WriteInt File,InterChangeReplyFunction(i,j)
			WriteInt File,InterChangeReplyData(i,j)
			WriteInt File,InterChangeReplyCommand(i,j)
			For k=0 To 3
				WriteInt File,InterChangeReplyCommandData(i,j,k)
			Next
		Next
	Next
	; Calculate NofAskAbouts
	For j=0 To 99
		If AskAboutText$(j)=""
			NofAskAbouts=j
			j=200
		EndIf
	Next
	If j=100 Then NofAskAbouts=100
	WriteInt File,NofAskAbouts
	WriteString file,AskAboutTopText$
	For i=0 To NofAskAbouts-1
		WriteString file,AskAboutText$(i)
		WriteInt File,AskAboutActive(i)
		WriteInt File,AskAboutInterchange(i)
		WriteInt File,AskAboutRepeat(i)
	Next
	CloseFile file

	UnsavedChanges=0

End Function