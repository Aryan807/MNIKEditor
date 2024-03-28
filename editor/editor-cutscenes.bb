Function StartCutSceneMainLoop()
	Cls
	EditorMode=15
	;CurrentCutSceneNumber=1
	eventNo=1
	eventNoText=1
	curTextLine=1
	CutTextView=1
	curTextEvent=1
	CurrentEventType=0
	If AdventureCurrentArchive=1
		ex$="Archive/"
	Else
		ex$="Current/"
	EndIf
	; check existence of this cutscene file
	If FileType(GlobalDirName$+"/Custom/Editing/"+ex$+AdventureFileName$+Chr$(47)+Str$(CurrentCutSceneNumber)+".cut")=1
		LoadCutSceneFile()
	Else
		ClearCutSceneFile()
		endTime=200
	EndIf
	CurrentMaxEvents=NoOfCameraEvents
	CutTimer=STARTTIME
	;If NoOfLevelEvents>0
		PrevLevTimer=0
		CutLevelLevelIndex=1
		CurCutCam=1
		PrevCamTimer=0
		CurrentLevelNumber=CutLevelLevel(CutLevelLevelIndex)
		CameraViewport Camera1,0,0,600,450
		CameraRange Camera1,.1,50
		CameraZoom Camera1, 2
		
		;CameraProjMode Camera,1
		CameraClsMode Camera, false, true
		ClearSurface TextSurface
		;DebugLog GlobalDirName$+"/Custom/Editing/"+ex$+AdventureFileName$+Chr$(47)+CurrentLevelNumber+".wlv "+FileType(GlobalDirName$+"/Custom/Editing/"+ex2$+AdventureFileName$+Chr$(47)+CurrentLevelNumber+".wlv")
		If FileType(GlobalDirName$+"/Custom/Editing/"+ex$+AdventureFileName$+Chr$(47)+CurrentLevelNumber+".wlv")=1
			LoadLevel(CurrentLevelNumber)
			;If NoOfCameraEvents>0
			;	PositionEntity Camera1, PosXs(1),PosYs(1),PosZs(1)
			;	RotateEntity Camera1, RotXs(1),RotYs(1),RotZs(1)
			;Else
			;	RotateEntity Camera1,0,0,0
			;	PositionEntity Camera1,7,6,-14 
			;EndIf
			CameraProjMode Camera1,1
			;DebugLog "load"
		End If
		;DebugLog CurrentLevelNumber
	;End If

	For p.letter = Each letter
		Delete p
	Next
End Function

Function ResetCamera()
	CameraClsMode Camera, true, true
	CameraViewport Camera1,0,0,500,500
	CameraZoom Camera1, 1
	RotateEntity Camera1,65,0,0
	PositionEntity Camera1,7,6,-14 
End Function

Function CutSceneMainLoop()
	;If DisplayFullScreen=True Cls
	Cls
	LevelTimer=LevelTimer+1
	;UpdateWorld 
	;RenderWorld
	Text 0,587,"ADVENTURE: "+AdventureFileName$
	If CurrentCutSceneNumber<10
		Text 700,470,"CUTSCENE: 0"+CurrentCutSceneNumber
	Else
		Text 500-9*8,587,"CUTSCENE: "+CurrentCutSceneNumber
	EndIf
	
	Text 653,5," GLOBALS"
	StartX=650
	StartY=20
	Color 200,0,0
	Rect StartX,StartY,150,42,True
	Color 100,100,100
	Rect 0,460,600,30,True
	Color 255,255,255
	lineX=Floor((Float(CutTimer-STARTTIME)/Float(endTime-STARTTIME))*Float(600))
	Line lineX, 460, lineX, 489
	Color 255,255,255
	
	Text StartX,StartY+2,"End Time: "+endTime
	Text StartX,StartY+15,"End Type: "+endType
	If CutSceneSkipable
		Text StartX+100,StartY+15,"S: YES"
	Else
		Text StartX+100,StartY+15,"S: NO"
	End If
	Text StartX,StartY+28,"D0:"+cutData0
	Text StartX+50,StartY+28,"D1:"+cutData1
	Text StartX+100,StartY+28,"D2:"+cutData2
	
	;camera view
	If NoOfCameraEvents>0
		If CutTimer<=CamStopTime(NoOfCameraEvents)
			PositionEntity Camera1, PosXs(CurCutCam)+CutTimer*PosXv(CurCutCam),PosZs(CurCutCam)+CutTimer*PosZv(CurCutCam),PosYs(CurCutCam)+CutTimer*PosYv(CurCutCam)
			RotateEntity Camera1, RotXs(CurCutCam)+CutTimer*RotXv(CurCutCam),RotZs(CurCutCam)+CutTimer*RotZv(CurCutCam),RotYs(CurCutCam)+CutTimer*RotYv(CurCutCam)
			;ClearSurface TextSurface
		End If
	EndIf
	
	;textview
	If NoOfTextEvents>0 And CutTextView
		If CutTimer<=CutTextTime(NoOfTextEvents)
			For i=1 To CutTextNoOfLines(curTextEvent)
				DisplayText3(CutText(curTextEvent,i),CutTextX(curTextEvent,i),CutTextY(curTextEvent,i),CutTextSize(curTextEvent,i),CutTextSpacing(curTextEvent,i),CutSceneRed(curTextEvent,i),CutSceneGreen(curTextEvent,i),CutSceneBlue(curTextEvent,i))
			Next
		End If
	End If
	
	;StartY=50
	;Color 200,0,0
	;Rect StartX,StartY,150,35,True
	;Color 255,255,255
	Color 200,0,0
	Rect StartX,StartY+15+41,150,236,True
	Color 255,255,255
	Text StartX,StartY+43,"EVENTS"
	Text StartX,StartY+56,"<<"
	Text StartX+130,StartY+56,">>"
	Select CurrentEventType
	Case 0
		CurrentMaxEvents=NoOfCameraEvents
		Text StartX+20,StartY+56,"Camera"
		Text StartX+20,StartY+251,"No Of Events: "+NoOfCameraEvents
		If NoOfCameraEvents>0
			Text StartX+50,StartY+82,"Time: "+CamStopTime(eventNo)
			
			If CamEditMode=0
				Text StartX+15,StartY+95,"PosXs: "
				Text StartX+23,StartY+108,PosXs(eventNo)
				Text StartX+85,StartY+95,"PosXv: "
				Text StartX+93,StartY+108,PosXv(eventNo)
				
				Text StartX+15,StartY+121,"PosYs: "
				Text StartX+23,StartY+134,PosYs(eventNo)
				Text StartX+85,StartY+121,"PosYv: "
				Text StartX+93,StartY+134,PosYv(eventNo)
				
				Text StartX+15,StartY+147,"PosZs: "
				Text StartX+23,StartY+160,PosZs(eventNo)
				Text StartX+85,StartY+147,"PosZv: "
				Text StartX+93,StartY+160,PosZv(eventNo)
				
				Text StartX+15,StartY+173,"RotXs: "
				Text StartX+23,StartY+186,RotXs(eventNo)
				Text StartX+85,StartY+173,"RotXv: "
				Text StartX+93,StartY+186,RotXv(eventNo)
				
				Text StartX+15,StartY+199,"RotYs: "
				Text StartX+23,StartY+212,RotYs(eventNo)
				Text StartX+85,StartY+199,"RotYv: "
				Text StartX+93,StartY+212,RotYv(eventNo)
				
				Text StartX+15,StartY+225,"RotZs: "
				Text StartX+23,StartY+238,RotZs(eventNo)
				Text StartX+85,StartY+225,"RotZv: "
				Text StartX+93,StartY+238,RotZv(eventNo)
			End If
		End If
	Case 1
		CurrentMaxEvents=NoOfLevelEvents
		Text StartX+20,StartY+56,"Level"
		Text StartX+20,StartY+251,"No Of Events: "+NoOfLevelEvents
		If NoOfLevelEvents>0
			Text StartX+50,StartY+82,"Time: "+CutLevelTime(eventNo)
			Text StartX+50,StartY+95,"Level: "+CutLevelLevel(eventNo)
		EndIf
	Case 2
		CurrentMaxEvents=NoOfLightEvents
		Text StartX+20,StartY+56,"Lights"
		Text StartX+20,StartY+251,"No Of Events: "+NoOfLightEvents
		If NoOfLightEvents>0
			Text StartX+50,StartY+82,"Time: "+CutLightTime(eventNo)
			Text StartX+15,StartY+95," Rs: "
			Text StartX+23,StartY+108,Rs(eventNo)
			Text StartX+85,StartY+95," Rv: "
			Text StartX+93,StartY+108,Rv(eventNo)
			
			Text StartX+15,StartY+121," Gs: "
			Text StartX+23,StartY+134,Gs(eventNo)
			Text StartX+85,StartY+121," Gv: "
			Text StartX+93,StartY+134,Gv(eventNo)
			
			Text StartX+15,StartY+147," Bs: "
			Text StartX+23,StartY+160,Bs(eventNo)
			Text StartX+85,StartY+147," Bv: "
			Text StartX+93,StartY+160,Bv(eventNo)
		EndIf
	Case 3
		CurrentMaxEvents=NoOfMusicEvents
		Text StartX+20,StartY+56,"Music"
		Text StartX+20,StartY+251,"No Of Events: "+NoOfMusicEvents
		If NoOfMusicEvents>0
			Text StartX+50,StartY+82,"Time: "+CutMusicTime(eventNo)
			Text StartX+50,StartY+95,"Track: "+CutMusicTrack(eventNo)
		EndIf
	Case 4
		CurrentMaxEvents=NoOfMusicFadeEvents
		Text StartX+20,StartY+56,"Music Fade"
		Text StartX+20,StartY+251,"No Of Events: "+NoOfMusicFadeEvents
		If NoOfMusicFadeEvents>0
			Text StartX+10,StartY+82,"Start Time: "+CutMusicFadeStart(eventNo)
			Text StartX+20,StartY+95,"End Time: "+CutMusicFadeEnd(eventNo)
			Text StartX+15,StartY+108," Vs: "
			Text StartX+23,StartY+121,Vs(eventNo)
			Text StartX+85,StartY+108," Vv: "
			Text StartX+93,StartY+121,Vv(eventNo)
		EndIf
	Case 5
		CurrentMaxEvents=NoOfSoundEvents
		Text StartX+20,StartY+56,"Sound"
		Text StartX+20,StartY+251,"No Of Events: "+NoOfSoundEvents
		If NoOfSoundEvents>0
			Text StartX+50,StartY+82,"Time: "+CutSoundTime(eventNo)
			Text StartX+50,StartY+95,"Sound: "+CutSoundID(eventNo)
		EndIf
	Default
		Text StartX+20,StartY+56,"???"
	End Select
	Text StartX+50,StartY+69,"Event: "+eventNo
	Text StartX+20,StartY+264,"Add Event"
	Text StartX+20,StartY+277,"Remove LAST"
	Text StartX+20,StartY+303,"Add Text Event"
	Text StartX+10,StartY+316,"Remove Text Event"
	If CutTextView
		Text StartX+10,StartY+342,"Display Text: Yes"
	Else
		Text StartX+10,StartY+342,"Display Text: No"
	End If
	;Text StartX+20,StartY+2,CurrentEventType
	
	Text 0,500,"Timer: "+CutTimer
	Text 450,500,"No Of Text Events: "+NoOfTextEvents
	If NoOfTextEvents>0
		Text 500,513,"Add Line"
		Text 500,526,"Remove Line"
		Text 500,539,"No Of Lines: "+CutTextNoOfLines(eventNoText)
		Text 0,513,"Text ID: "+eventNoText
		Text 200,513,"Time: "+CutTextTime(eventNoText)
		If CutTextNoOfLines(eventNoText)>0
			Text 100,513,"Line: "+curTextLine
			Text 0,526,"Edit Text Line: "+CutText(eventNoText,curTextLine)
			Text 0,539,"X: "+CutTextX(eventNoText,curTextLine)
			Text 100,539,"Y: "+CutTextY(eventNoText,curTextLine)
			Text 200,539,"Size: "+CutTextSize(eventNoText,curTextLine)
			Text 300,539,"Spacing: "+CutTextSpacing(eventNoText,curTextLine)
			Text 0,552,"Red: "+CutSceneRed(eventNoText,curTextLine)
			Text 100,552,"Green: "+CutSceneGreen(eventNoText,curTextLine)
			Text 200,552,"Blue: "+CutSceneBlue(eventNoText,curTextLine)
		End If
	End If
	;Text StartX,StartY+2,"<<"
	
	;Text StartX,StartY+2,"  "+EventText
	;Text StartX+80-16,StartY+2,">>"
	
	If MouseX()>700 And MouseY()>515 And MouseY()<555
		Color 255,255,0
		Text 704,520," > CANCEL <"
		Text 704,535," >AND EXIT<"
	Else
		Color 255,255,255
		Text 720,520," CANCEL"
		Text 720,535,"AND EXIT"
	EndIf
	If MouseX()>700 And MouseY()>560 And MouseY()<600
		Color 255,255,0
		Text 696,565," >SAVE SCENE<"
		Text 696,580," > AND EXIT <"
	Else
		Color 255,255,255
		Text 712,565,"SAVE SCENE"
		Text 712,580," AND EXIT"
	EndIf
	Color 255,255,255
	
	Fast=False
	If KeyDown(42) Or KeyDown(54) Then Fast=True
	
	Cntrl=False
	If KeyDown(29) Or KeyDown(157) Then Cntrl=True
	
	CopyMode=False
	If Cntrl And KeyDown(46) Then CopyMode=True
	
	PasteMode=False
	If Cntrl And KeyDown(47) Then PasteMode=True
	
	MX=MouseX()
	MY=MouseY()
	If MouseDown(1)=True
		LeftMouse=True
	Else
		LeftMouse=False
		LeftMouseReleased=True
	EndIf
	If MouseDown(2)=True
		RightMouse=True
	Else
		RightMouse=False
		RightMouseReleased=True
	EndIf
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
	
	If Fast
		adj=10
	Else
		adj=1
	End If
	
	If MX>StartX+20 And MY>StartY+303 And MY<StartY+316 And MX<780 ;add text event
		If LeftMouse=True
			NoOfTextEvents=NoOfTextEvents+1
			If NoOfTextEvents>MaxNoOfTextEvents
				NoOfTextEvents=MaxNoOfTextEvents
			Else
				CutTextTime(NoOfTextEvents)=100
				CutTextNoOfLines(NoOfTextEvents)=0
			End If 
			Delay 120
		;Else If RightMouse=True
			
		End If
	Else If MX>StartX+10 And MY>StartY+316 And MY<StartY+329 ;remove text event
		If LeftMouse=True
			NoOfTextEvents=NoOfTextEvents-1
			If NoOfTextEvents<0
				NoOfTextEvents=0
			End If 
			Delay 120
		End If
	End If
	
	If MX>StartX+10 And MY>StartY+342 And MY<StartY+355 ;toggleText
		If LeftMouse=True
			CutTextView=Not CutTextView
			Delay 120
		End If
	End If
	
	If MX>500 And MY>513 And MY<526 And MX<600 ;add line
		If NoOfTextEvents>0
			If LeftMouse=True
				CutTextNoOfLines(eventNoText)=CutTextNoOfLines(eventNoText)+1
				If CutTextNoOfLines(eventNoText)>MaxNoOfTextLines
					CutTextNoOfLines(eventNoText)=MaxNoOfTextLines
				Else
					CutText(eventNoText,CutTextNoOfLines(eventNoText))=""
					CutTextX(eventNoText,CutTextNoOfLines(eventNoText))=0.0
					CutTextY(eventNoText,CutTextNoOfLines(eventNoText))=0.0
					CutTextSize(eventNoText,CutTextNoOfLines(eventNoText))=1.0
					CutTextSpacing(eventNoText,CutTextNoOfLines(eventNoText))=1.0
					CutSceneRed(eventNoText,CutTextNoOfLines(eventNoText))=255
					CutSceneGreen(eventNoText,CutTextNoOfLines(eventNoText))=255
					CutSceneBlue(eventNoText,CutTextNoOfLines(eventNoText))=255
				End If
				Delay 120
			;Else If RightMouse=True
				
			End If
		End If
	Else If MX>500 And MY>526 And MY<539 And MX<600 ;remove line
		If NoOfTextEvents>0
			If LeftMouse=True
				CutTextNoOfLines(eventNoText)=CutTextNoOfLines(eventNoText)-1
				If CutTextNoOfLines(eventNoText)<0
					CutTextNoOfLines(eventNoText)=0
				End If
				Delay 120
			End If
		End If
	End If
	
	If NoOfTextEvents>0
		If MY>513 And MY<526
			If MX<100
				If LeftMouse=True
					eventNoText=eventNoText+adj
					If eventNoText>NoOfTextEvents
						eventNoText=NoOfTextEvents
					Else
						curTextLine=1
					End If
					Delay 120
				Else If RightMouse=True
					eventNoText=eventNoText-adj
					If eventNoText<1
						eventNoText=1
					Else
						curTextLine=1
					End If
					Delay 120
				End If
			Else If MX<200
				If LeftMouse=True
					curTextLine=curTextLine+adj
					If curTextLine>CutTextNoOfLines(eventNoText)
						curTextLine=CutTextNoOfLines(eventNoText)
					End If
					Delay 120
				Else If RightMouse=True
					curTextLine=curTextLine-adj
					If curTextLine<1
						curTextLine=1
					End If
					Delay 120
				End If
			Else If MX<300
				If LeftMouse=True
					If Cntrl
						Locate 0,0
						Color 0,0,0
						Rect 0,0,500,40,True
						Color 255,255,255
						CutTextTime(eventNoText)=Input$("Enter Time:")
					Else
						CutTextTime(eventNoText)=CutTextTime(eventNoText)+adj
						CheckCutText()
						Delay 120
					End If
					
				Else If RightMouse=True
					CutTextTime(eventNoText)=CutTextTime(eventNoText)-adj
					CheckCutText()
					Delay 120
				End If
			End If
		Else If MY>526 And MY<539 And CutTextNoOfLines(eventNoText)>0
			If MX<500 
				If LeftMouse=True
					Locate 0,0
					Color 0,0,0
					Rect 0,0,500,40,True
					Color 255,255,255
					CutText(eventNoText,curTextLine)=Input$("Enter Text:")
				Else If RightMouse=True
					Locate 0,0
					If CopyMode
						SetClipboard(CutText(eventNoText,curTextLine))
						Print "Text copied."
						Delay 1000
					Else If PasteMode
						CutText(eventNoText,curTextLine)=GetClipboard()
						Print "Text pasted."
						Delay 1000
					End If
				End If
			End If
		Else If MY>539 And MY<552 And CutTextNoOfLines(eventNoText)>0
			If MX<100
				If LeftMouse=True
					CutTextX(eventNoText,curTextLine)=CutTextX(eventNoText,curTextLine)+adj*0.1
					Delay 100
				Else If RightMouse=True
					CutTextX(eventNoText,curTextLine)=CutTextX(eventNoText,curTextLine)-adj*0.1
					Delay 100
				End If
			Else If MX<200
				If LeftMouse=True
					CutTextY(eventNoText,curTextLine)=CutTextY(eventNoText,curTextLine)+adj*0.1
					Delay 100
				Else If RightMouse=True
					CutTextY(eventNoText,curTextLine)=CutTextY(eventNoText,curTextLine)-adj*0.1
					Delay 100
				End If
			Else If MX<300
				If LeftMouse=True
					CutTextSize(eventNoText,curTextLine)=CutTextSize(eventNoText,curTextLine)+adj*0.1
					Delay 100
				Else If RightMouse=True
					CutTextSize(eventNoText,curTextLine)=CutTextSize(eventNoText,curTextLine)-adj*0.1
					Delay 100
				End If
			Else If MX<400
				If LeftMouse=True
					CutTextSpacing(eventNoText,curTextLine)=CutTextSpacing(eventNoText,curTextLine)+adj*0.1
					Delay 100
				Else If RightMouse=True
					CutTextSpacing(eventNoText,curTextLine)=CutTextSpacing(eventNoText,curTextLine)-adj*0.1
					Delay 100
				End If
			End If
		Else If MY>552 And MY<565 And CutTextNoOfLines(eventNoText)>0
			If MX<100
				If LeftMouse=True
					CutSceneRed(eventNoText,curTextLine)=CutSceneRed(eventNoText,curTextLine)+adj
					Delay 100
				Else If RightMouse=True
					CutSceneRed(eventNoText,curTextLine)=CutSceneRed(eventNoText,curTextLine)-adj
					Delay 100
				End If
			Else If MX<200
				If LeftMouse=True
					CutSceneGreen(eventNoText,curTextLine)=CutSceneGreen(eventNoText,curTextLine)+adj
					Delay 100
				Else If RightMouse=True
					CutSceneGreen(eventNoText,curTextLine)=CutSceneGreen(eventNoText,curTextLine)-adj
					Delay 100
				End If
			Else If MX<300
				If LeftMouse=True
					CutSceneBlue(eventNoText,curTextLine)=CutSceneBlue(eventNoText,curTextLine)+adj
					Delay 100
				Else If RightMouse=True
					CutSceneBlue(eventNoText,curTextLine)=CutSceneBlue(eventNoText,curTextLine)-adj
					Delay 100
				End If
			End If
		End If
	End If
	
	
	
	If MY>460 And MY<490 And MX<600 And LeftMouse=True
		CutTimer=Floor((Float(MX)/Float(600))*Float(endTime-STARTTIME))+STARTTIME
		CheckUpdateCutWLV()
		CheckCutCamera()
		CheckCutText()
		;Print Float(MX)/Float(600)+" "+Float(MX)+" "+Float(600)
	End If
	
	If MX>StartX And MX<StartX+115 And MY>StartY+2 and MY<StartY+15
		If LeftMouse=True
			If Cntrl
				Locate 0,0
				Color 0,0,0
				Rect 0,0,500,40,True
				Color 255,255,255
				endTime=Input$("Enter Time:")
			Else
				endTime=endTime+adj
				Delay 100
			End If
		Else If RightMouse=True
			endTime=endTime-adj
			If endTime<200
				endTime=200
			End If
			Delay 100
		End If
	Else If MX>StartX And MX<StartX+100 And MY>StartY+15 and MY<StartY+28
		If LeftMouse=True
			endType=endType+adj
			If endType>5
				endType=5
			End If
			Delay 100
		Else If RightMouse=True
			endType=endType-adj
			If endType<0
				endType=0
			End If
			Delay 100
		End If
	Else If MX>StartX+100 And MY>StartY+15 And MY<StartY+28 And LeftMouse=True
		CutSceneSkipable=Not CutSceneSkipable
		Delay 120
	Else If MX>StartX And MX<StartX+50 And MY>StartY+28 and MY<StartY+41
		If LeftMouse=True
			cutData0=cutData0+adj
			Delay 100
		Else If RightMouse=True
			cutData0=cutData0-adj
			Delay 100
		End If
	Else If MX>StartX+50 And MX<StartX+100 And MY>StartY+28 and MY<StartY+41
		If LeftMouse=True
			cutData1=cutData1+adj
			Delay 100
		Else If RightMouse=True
			cutData1=cutData1-adj
			Delay 100
		End If
	Else If MX>StartX+100 And MY>StartY+28 and MY<StartY+41
		If LeftMouse=True
			cutData2=cutData2+adj
			Delay 100
		Else If RightMouse=True
			cutData2=cutData2-adj
			Delay 100
		End If
	End If
	
	If LeftMouse=True And MX<100 And MY>500 And MY<513
		CutTimer=CutTimer+adj
		If CutTimer>endTime
			CutTimer=endTime
		End If
		Delay 80
		CheckUpdateCutWLV()
		CheckCutCamera()
		CheckCutText()
	Else If RightMouse=True And MX<100 And MY>500 And MY<513
		CutTimer=CutTimer-adj
		If CutTimer<100
			CutTimer=100
		End If
		Delay 80
		CheckUpdateCutWLV()
		CheckCutCamera()
		CheckCutText()
	End If
	
	;modifiers
	Select CurrentEventType
	Case 0 ;cameras
		If MX>670 And MX<770 And MY>102 And MY<115
			If LeftMouse=True
				If Cntrl
					Locate 0,0
					Color 0,0,0
					Rect 0,0,500,40,True
					Color 255,255,255
					CamStopTime(eventNo)=Input$("Enter Time:")
				Else
					CamStopTime(eventNo)=CamStopTime(eventNo)+adj
					CheckCutCamera()
					Delay 80
				End If
			Else If RightMouse=True
				CamStopTime(eventNo)=CamStopTime(eventNo)-adj
				CheckCutCamera()
				Delay 80
			End If
		End If
		If MX>StartX+15 And MX<StartX+31+30
			If MY>95+StartY And My<121+StartY
				If LeftMouse=True
					PosXs(eventNo)=PosXs(eventNo)+adj*0.1
					Delay 100
				Else If RightMouse=True
					PosXs(eventNo)=PosXs(eventNo)-adj*0.1
					Delay 100
				End If
			End If
			If MY>121+StartY And My<147+StartY
				If LeftMouse=True
					PosYs(eventNo)=PosYs(eventNo)+adj*0.1
					Delay 100
				Else If RightMouse=True
					PosYs(eventNo)=PosYs(eventNo)-adj*0.1
					Delay 100
				End If
			End If
			If MY>147+StartY And My<173+StartY
				If LeftMouse=True
					PosZs(eventNo)=PosZs(eventNo)+adj*0.1
					Delay 100
				Else If RightMouse=True
					PosZs(eventNo)=PosZs(eventNo)-adj*0.1
					Delay 100
				End If
			End If
			If MY>173+StartY And My<199+StartY
				If LeftMouse=True
					RotXs(eventNo)=RotXs(eventNo)+adj*0.1
					Delay 100
				Else If RightMouse=True
					RotXs(eventNo)=RotXs(eventNo)-adj*0.1
					Delay 100
				End If
			End If
			If MY>199+StartY And My<225+StartY
				If LeftMouse=True
					RotYs(eventNo)=RotYs(eventNo)+adj*0.1
					Delay 100
				Else If RightMouse=True
					RotYs(eventNo)=RotYs(eventNo)-adj*0.1
					Delay 100
				End If
			End If
			If MY>225+StartY And My<251+StartY
				If LeftMouse=True
					RotZs(eventNo)=RotZs(eventNo)+adj*0.1
					Delay 100
				Else If RightMouse=True
					RotZs(eventNo)=RotZs(eventNo)-adj*0.1
					Delay 100
				End If
			End If
		Else If MX>StartX+85 And MX<StartX+101+30
			If MY>95+StartY And My<121+StartY
				If LeftMouse=True
					PosXv(eventNo)=PosXv(eventNo)+adj*0.0001
					Delay 100
				Else If RightMouse=True
					PosXv(eventNo)=PosXv(eventNo)-adj*0.0001
					Delay 100
				End If
			End If
			If MY>121+StartY And My<147+StartY
				If LeftMouse=True
					PosYv(eventNo)=PosYv(eventNo)+adj*0.0001
					Delay 100
				Else If RightMouse=True
					PosYv(eventNo)=PosYv(eventNo)-adj*0.0001
					Delay 100
				End If
			End If
			If MY>147+StartY And My<173+StartY
				If LeftMouse=True
					PosZv(eventNo)=PosZv(eventNo)+adj*0.0001
					Delay 100
				Else If RightMouse=True
					PosZv(eventNo)=PosZv(eventNo)-adj*0.0001
					Delay 100
				End If
			End If
			If MY>173+StartY And My<199+StartY
				If LeftMouse=True
					RotXv(eventNo)=RotXv(eventNo)+adj*0.0001
					Delay 100
				Else If RightMouse=True
					RotXv(eventNo)=RotXv(eventNo)-adj*0.0001
					Delay 100
				End If
			End If
			If MY>199+StartY And My<225+StartY
				If LeftMouse=True
					RotYv(eventNo)=RotYv(eventNo)+adj*0.0001
					Delay 100
				Else If RightMouse=True
					RotYv(eventNo)=RotYv(eventNo)-adj*0.0001
					Delay 100
				End If
			End If
			If MY>225+StartY And My<251+StartY
				If LeftMouse=True
					RotZv(eventNo)=RotZv(eventNo)+adj*0.0001
					Delay 100
				Else If RightMouse=True
					RotZv(eventNo)=RotZv(eventNo)-adj*0.0001
					Delay 100
				End If
			End If
		End If
	Case 1 ;levels
		If MX>670 And MX<770 And MY>102 And MY<115
			If LeftMouse=True
				If Cntrl
					Locate 0,0
					Color 0,0,0
					Rect 0,0,500,40,True
					Color 255,255,255
					CutLevelTime(eventNo)=Input$("Enter Time:")
				Else
					CutLevelTime(eventNo)=CutLevelTime(eventNo)+adj
					CheckUpdateCutWLV()
					Delay 80
				End If
			Else If RightMouse=True
				CutLevelTime(eventNo)=CutLevelTime(eventNo)-adj
				CheckUpdateCutWLV()
				Delay 80
			End If
		Else If MX>670 And MX<770 And MY>115 And MY<128
			If LeftMouse=True
				CutLevelLevel(eventNo)=CutLevelLevel(eventNo)+adj
				CheckUpdateCutWLV()
				Delay 100
			Else If RightMouse=True
				CutLevelLevel(eventNo)=CutLevelLevel(eventNo)-adj
				CheckUpdateCutWLV()
				Delay 100
			End If
		End If
	Case 2 ;lights
		If MX>670 And MX<770 And MY>102 And MY<115
			If LeftMouse=True
				If Cntrl
					Locate 0,0
					Color 0,0,0
					Rect 0,0,500,40,True
					Color 255,255,255
					CutLightTime(eventNo)=Input$("Enter Time:")
				Else
					CutLightTime(eventNo)=CutLightTime(eventNo)+adj
					Delay 100
				End if
			Else If RightMouse=True
				CutLightTime(eventNo)=CutLightTime(eventNo)-adj
				Delay 100
			End If
		End If
		If MX>StartX+15 And MX<StartX+31+30
			If MY>95+StartY And My<121+StartY
				If LeftMouse=True
					Rs(eventNo)=Rs(eventNo)+adj*0.1
					Delay 100
				Else If RightMouse=True
					Rs(eventNo)=Rs(eventNo)-adj*0.1
					Delay 100
				End If
			End If
			If MY>121+StartY And My<147+StartY
				If LeftMouse=True
					Gs(eventNo)=Gs(eventNo)+adj*0.1
					Delay 100
				Else If RightMouse=True
					Gs(eventNo)=Gs(eventNo)-adj*0.1
					Delay 100
				End If
			End If
			If MY>147+StartY And My<173+StartY
				If LeftMouse=True
					Bs(eventNo)=Bs(eventNo)+adj*0.1
					Delay 100
				Else If RightMouse=True
					Bs(eventNo)=Bs(eventNo)-adj*0.1
					Delay 100
				End If
			End If
		Else If MX>StartX+85 And MX<StartX+101+30
			If MY>95+StartY And My<121+StartY
				If LeftMouse=True
					Rv(eventNo)=Rv(eventNo)+adj*0.01
					Delay 100
				Else If RightMouse=True
					Rv(eventNo)=Rv(eventNo)-adj*0.01
					Delay 100
				End If
			End If
			If MY>121+StartY And My<147+StartY
				If LeftMouse=True
					Gv(eventNo)=Gv(eventNo)+adj*0.01
					Delay 100
				Else If RightMouse=True
					Gv(eventNo)=Gv(eventNo)-adj*0.01
					Delay 100
				End If
			End If
			If MY>147+StartY And My<173+StartY
				If LeftMouse=True
					Bv(eventNo)=Bv(eventNo)+adj*0.01
					Delay 100
				Else If RightMouse=True
					Bv(eventNo)=Bv(eventNo)-adj*0.01
					Delay 100
				End If
			End If
		End If
	Case 3 ;music
		If MX>670 And MX<770 And MY>102 And MY<115
			If LeftMouse=True
				If Cntrl
					Locate 0,0
					Color 0,0,0
					Rect 0,0,500,40,True
					Color 255,255,255
					CutMusicTime(eventNo)=Input$("Enter Time:")
				Else
					CutMusicTime(eventNo)=CutMusicTime(eventNo)+adj
					Delay 100
				End If
			Else If RightMouse=True
				CutMusicTime(eventNo)=CutMusicTime(eventNo)-adj
				Delay 100
			End If
		Else If MX>670 And MX<770 And MY>115 And MY<128
			If LeftMouse=True
				CutMusicTrack(eventNo)=CutMusicTrack(eventNo)+adj
				Delay 100
			Else If RightMouse=True
				CutMusicTrack(eventNo)=CutMusicTrack(eventNo)-adj
				Delay 100
			End If
		End If
	Case 4 ;music fade
		If MX>660 And MX<790 And MY>102 And MY<115
			If LeftMouse=True
				If Cntrl
					Locate 0,0
					Color 0,0,0
					Rect 0,0,500,40,True
					Color 255,255,255
					CutMusicFadeStart(eventNo)=Input$("Enter Time:")
				Else
					CutMusicFadeStart(eventNo)=CutMusicFadeStart(eventNo)+adj
					Delay 100
				End If
			Else If RightMouse=True
				CutMusicFadeStart(eventNo)=CutMusicFadeStart(eventNo)-adj
				Delay 100
			End If
		Else If MX>670 And MX<790 And MY>115 And MY<128
			If LeftMouse=True
				If Cntrl
					Locate 0,0
					Color 0,0,0
					Rect 0,0,500,40,True
					Color 255,255,255
					CutMusicFadeEnd(eventNo)=Input$("Enter Time:")
				Else
					CutMusicFadeEnd(eventNo)=CutMusicFadeEnd(eventNo)+adj
					Delay 100
				End If
			Else If RightMouse=True
				CutMusicFadeEnd(eventNo)=CutMusicFadeEnd(eventNo)-adj
				Delay 100
			End If
		End If
		If MX>StartX+15 And MX<StartX+31+30
			If MY>121+StartY And My<147+StartY
				If LeftMouse=True
					Vs(eventNo)=Vs(eventNo)+adj*0.1
					Delay 100
				Else If RightMouse=True
					Vs(eventNo)=Vs(eventNo)-adj*0.1
					Delay 100
				End If
			End If
		Else If MX>StartX+85 And MX<StartX+101+30
			If MY>121+StartY And My<147+StartY
				If LeftMouse=True
					Vv(eventNo)=Vv(eventNo)+adj*0.01
					Delay 100
				Else If RightMouse=True
					Vv(eventNo)=Vv(eventNo)-adj*0.01
					Delay 100
				End If
			End If
		End If
	Case 5 ;sounds
		If MX>670 And MX<770 And MY>102 And MY<115
			If LeftMouse=True
				If Cntrl
					Locate 0,0
					Color 0,0,0
					Rect 0,0,500,40,True
					Color 255,255,255
					CutSoundTime(eventNo)=Input$("Enter Time:")
				Else
					CutSoundTime(eventNo)=CutSoundTime(eventNo)+adj
					Delay 80
				End If
			Else If RightMouse=True
				CutSoundTime(eventNo)=CutSoundTime(eventNo)-adj
				Delay 80
			End If
		Else If MX>670 And MX<770 And MY>115 And MY<128
			If LeftMouse=True
				CutSoundID(eventNo)=CutSoundID(eventNo)+adj
				Delay 100
			Else If RightMouse=True
				CutSoundID(eventNo)=CutSoundID(eventNo)-adj
				Delay 100
			End If
		End If
	End Select
	If LeftMouse=True And MX>670 And MX<770 And MY>89 And MY<102
		If eventNo<CurrentMaxEvents
			eventNo=eventNo+1
		Else
			eventNo=1
		EndIf
		Repeat
		Until MouseDown(1)=0 And MouseDown(2)=0
	Else If RightMouse=True And MX>670 And MX<770 And MY>89 And MY<102
		If eventNo>1
			eventNo=eventNo-1
		Else
			eventNo=CurrentMaxEvents
		EndIf
		Repeat
		Until MouseDown(1)=0 And MouseDown(2)=0
	End If
	
	
	;add events
	If LeftMouse=True And MX>670 And MX<770 And MY>284 And MY<297
		Select CurrentEventType
		Case 0
			If NoOfCameraEvents<=MaxEvents
				NoOfCameraEvents=NoOfCameraEvents+1
				CamStopTime(NoOfCameraEvents)=200
				PosXs(NoOfCameraEvents)=7
				PosYs(NoOfCameraEvents)=-14
				PosZs(NoOfCameraEvents)=6
				RotXs(NoOfCameraEvents)=65
				RotYs(NoOfCameraEvents)=0
				RotZs(NoOfCameraEvents)=0
				PosXv(NoOfCameraEvents)=0
				PosYv(NoOfCameraEvents)=0
				PosZv(NoOfCameraEvents)=0
				RotXv(NoOfCameraEvents)=0
				RotYv(NoOfCameraEvents)=0
				RotZv(NoOfCameraEvents)=0
			End If
		Case 1
			If NoOfLevelEvents<=MaxEvents
				NoOfLevelEvents=NoOfLevelEvents+1
				CutLevelTime(NoOfLevelEvents)=100 ;default
				CutLevelLevel(NoOfLevelEvents)=0 ;default
			End If
		Case 2
			If NoOfLightEvents<=MaxEvents
				NoOfLightEvents=NoOfLightEvents+1
				Rs(NoOfLightEvents)=255
				Gs(NoOfLightEvents)=255
				Bs(NoOfLightEvents)=255
				Rv(NoOfLightEvents)=0
				Gv(NoOfLightEvents)=0
				Bv(NoOfLightEvents)=0
			End If
		Case 3
			If NoOfMusicEvents<=MaxEvents
				NoOfMusicEvents=NoOfMusicEvents+1
				CutMusicTime(NoOfMusicEvents)=100
				CutMusicTrack(NoOfMusicEvents)=0
			End If
		Case 4
			If NoOfMusicFadeEvents<=MaxEvents
				NoOfMusicFadeEvents=NoOfMusicFadeEvents+1
				CutMusicFadeStart(NoOfMusicFadeEvents)=200
				CutMusicFadeEnd(NoOfMusicFadeEvents)=100
				Vs(NoOfMusicFadeEvents)=20
				Vv(NoOfMusicFadeEvents)=-0.01
			End If
		Case 5
			If NoOfSoundEvents<=MaxEvents
				NoOfSoundEvents=NoOfSoundEvents+1
				CutSoundTime(NoOfSoundEvents)=100
				CutSoundID(NoOfSoundEvents)=0
			End If
		End Select
		Repeat
		Until MouseDown(1)=0 And MouseDown(2)=0
	Else If LeftMouse=True And MX>670 And MX<770 And MY>297 And MY<310
		Select CurrentEventType
		Case 0
			If NoOfCameraEvents>0
				;PosXs(NoOfCameraEvents)=0
				;PosYs(NoOfCameraEvents)=0
				;PosZs(NoOfCameraEvents)=0
				NoOfCameraEvents=NoOfCameraEvents-1
			End If
		Case 1
			If NoOfLevelEvents>0
				NoOfLevelEvents=NoOfLevelEvents-1
			End If
		Case 2
			If NoOfLightEvents>0
				NoOfLightEvents=NoOfLightEvents-1
			End If
		Case 3
			If NoOfMusicEvents>0
				NoOfMusicEvents=NoOfMusicEvents-1
			End If
		Case 4
			If NoOfMusicFadeEvents>0
				NoOfMusicFadeEvents=NoOfMusicFadeEvents-1
			End If
		Case 5
			If NoOfSoundEvents>0
				NoOfSoundEvents=NoOfSoundEvents-1
			End If
		End Select
		Repeat
		Until MouseDown(1)=0 And MouseDown(2)=0
	End If
	
	If LeftMouse=True And MX>650+130 And MX<670+130 And MY>76 And MY<96
		If CurrentEventType<5
			CurrentEventType=CurrentEventType+1
		Else
			CurrentEventType=0
		End If
		eventNo=1
		Repeat
		Until MouseDown(1)=0 And MouseDown(2)=0
	Else If LeftMouse=True And MX>650 And MX<670 And MY>76 And MY<96
		If CurrentEventType>0
			CurrentEventType=CurrentEventType-1
		Else
			CurrentEventType=5
		End If
		eventNo=1
		Repeat
		Until MouseDown(1)=0 And MouseDown(2)=0
	End If
	
	If LeftMouse=True And LeftMouseReleased=True
		If MX>700
			If my>515 And my<555
				ResetCamera()
				ResumeMaster()
				EditorMode=10
				Repeat
				Until MouseDown(1)=False	
			Else If my>560 And my<600
				SaveCutSceneFile()
				ResetCamera()
				ResumeMaster()
				EditorMode=10
				Repeat
				Until MouseDown(1)=False
			EndIf
		EndIf
	EndIf
	
	RenderLetters()
	RenderWorld()
	Flip
End Function

Function DisplayText3(mytext$,x#,y#,size#,spacing#,red,green,blue)
	For i=1 To Len(mytext$)
		let=Asc(Mid$(mytext$,i,1))-32
		AddLetter(let,(-.97+(x+i-1)*.0396*size*spacing*0.75)-0.01,(.7-y*.0623*size*spacing*0.75)+0.01,1.0,0,.044*size*0.75,0,0,0,0,0,0,0,0,1,red,green,blue)	
	Next
End Function

Function CheckUpdateCutWLV()
	If CutLevelLevelIndex+1<=MaxEvents
		If AdventureCurrentArchive=1
		ex$="Archive/"
		Else
			ex$="Current/"
		EndIf
		;DebugLog "abc "+CutLevelLevelIndex ;CutLevelTime(CutLevelLevelIndex+1)
		If Not CurrentLevelNumber=CutLevelLevel(CutLevelLevelIndex)
			CurrentLevelNumber=CutLevelLevel(CutLevelLevelIndex)
			If FileType(GlobalDirName$+"/Custom/Editing/"+ex$+AdventureFileName$+Chr$(47)+CurrentLevelNumber+".wlv")=1
				LoadLevel(CurrentLevelNumber)
				;DebugLog "loadq "+CurrentLevelNumber
				CameraProjMode Camera1,1
			Else
				CameraProjMode Camera1,0
			End If
		Else If CutTimer>=CutLevelTime(CutLevelLevelIndex+1) And CutLevelLevelIndex+1<=NoOfLevelEvents
			;PrevLevTimer=CutLevelTime(CutLevelLevelIndex+1)
			CutLevelLevelIndex=CutLevelLevelIndex+1
			CurrentLevelNumber=CutLevelLevel(CutLevelLevelIndex)
			;DebugLog "def "+CurrentLevelNumber
			;DebugLog "prev "+PrevLevTimer
			If FileType(GlobalDirName$+"/Custom/Editing/"+ex$+AdventureFileName$+Chr$(47)+CurrentLevelNumber+".wlv")=1
				LoadLevel(CurrentLevelNumber)
				CameraProjMode Camera1,1
				;DebugLog "load "+CurrentLevelNumber
			Else
				CameraProjMode Camera1,0
			End If
		Else If CutLevelLevelIndex>1 ;CutTimer<=PrevLevTimer And PrevLevTimer>0
			If CutTimer<=CutLevelTime(CutLevelLevelIndex)
				;DebugLog "aa "+CutLevelLevelIndex
				CutLevelLevelIndex=CutLevelLevelIndex-1
				;PrevLevTimer=CutLevelTime(CutLevelLevelIndex-1)
				CurrentLevelNumber=CutLevelLevel(CutLevelLevelIndex)
				If FileType(GlobalDirName$+"/Custom/Editing/"+ex$+AdventureFileName$+Chr$(47)+CurrentLevelNumber+".wlv")=1
					LoadLevel(CurrentLevelNumber)
					CameraProjMode Camera1,1
					;DebugLog "load "+CurrentLevelNumber
				Else
					CameraProjMode Camera1,0
				End If
			End If
		End If
	End If
End Function

Function CheckCutCamera()
	;DebugLog CurCutCam
	If CurCutCam<=MaxEvents
		;DebugLog "timer "+CutTimer
		;DebugLog "stop "+CamStopTime(CurCutCam+1)
		If CutTimer>=CamStopTime(CurCutCam) And CurCutCam+1<=NoOfCameraEvents
			;PrevCamTimer=CamStopTime(CurCutCam)
			CurCutCam=CurCutCam+1
		Else If CurCutCam>1;CutTimer<=PrevCamTimer And PrevCamTimer>0
			;PrevCamTimer=CamStopTime(CurCutCam)
			If CutTimer<=CamStopTime(CurCutCam-1)
				CurCutCam=CurCutCam-1
			End If
		End If
	End If
End Function

Function CheckCutText()
	If curTextEvent<=MaxEvents
		If CutTimer>=CutTextTime(curTextEvent) And curTextEvent+1<=NoOfTextEvents
			curTextEvent=curTextEvent+1
		Else If curTextEvent>1
			If CutTimer<=CutTextTime(curTextEvent-1)
				curTextEvent=curTextEvent-1
			End If
		End If
	End If
End Function

Function SaveCutSceneFile()
	If AdventureCurrentArchive=1
		ex2$="Archive/"
	Else
		ex2$="Current/"
	EndIf
	file=WriteFile (GlobalDirName$+"/Custom/Editing/"+ex2$+AdventureFileName$+Chr$(47)+CurrentCutSceneNumber+".cut")
	WriteInt file,0 ;version
	WriteInt file,NoOfCameraEvents
	WriteInt file,NoOfLevelEvents
	WriteInt file,NoOfLightEvents
	WriteInt file,NoOfMusicEvents
	WriteInt file,NoOfMusicFadeEvents
	WriteInt file,NoOfSoundEvents
	WriteInt file,NoOfTextEvents
	WriteInt file,endTime
	WriteInt file,endType
	WriteInt file,cutData0
	WriteInt file,cutData1
	WriteInt file,cutData2
	WriteInt file,CutSceneSkipable
	
	For i=1 To NoOfCameraEvents
		WriteInt file,CamStopTime(i)
		WriteFloat file,PosXs(i)
		WriteFloat file,PosZs(i)
		WriteFloat file,PosYs(i)
		WriteFloat file,PosXv(i)
		WriteFloat file,PosZv(i)
		WriteFloat file,PosYv(i)
		
		WriteFloat file,RotXs(i)
		WriteFloat file,RotZs(i)
		WriteFloat file,RotYs(i)
		WriteFloat file,RotXv(i)
		WriteFloat file,RotZv(i)
		WriteFloat file,RotYv(i)
	Next
	For i=1 To NoOfLevelEvents
		WriteInt file,CutLevelTime(i)
		WriteInt file,CutLevelLevel(i)
	Next
	For i=1 To NoOfLightEvents
		WriteInt file,CutLightTime(i)
		WriteFloat file,Rs(i)
		WriteFloat file,Gs(i)
		WriteFloat file,Bs(i)
		WriteFloat file,Rv(i)
		WriteFloat file,Gv(i)
		WriteFloat file,Bv(i)
	Next
	For i=1 To NoOfMusicEvents
		WriteInt file,CutMusicTime(i)
		WriteInt file,CutMusicTrack(i)
	Next
	For i=1 To NoOfMusicFadeEvents
		WriteInt file,CutMusicFadeStart(i)
		WriteInt file,CutMusicFadeEnd(i)
		WriteFloat file,Vs(i)
		WriteFloat file,Vv(i)
	Next
	For i=1 To NoOfSoundEvents
		WriteInt file,CutSoundTime(i)
		WriteInt file,CutSoundID(i)
	Next
	;text events
	For i=1 To NoOfTextEvents
		WriteInt file,CutTextTime(i)
		WriteInt file,CutTextNoOfLines(i)
		For j=1 To CutTextNoOfLines(i)
			WriteString file,CutText(i,j)
			WriteFloat file,CutTextX(i,j)
			WriteFloat file,CutTextY(i,j)
			WriteFloat file,CutTextSize(i,j)
			WriteFloat file,CutTextSpacing(i,j)
			WriteInt file,CutSceneRed(i,j)
			WriteInt file,CutSceneGreen(i,j)
			WriteInt file,CutSceneBlue(i,j)
			WriteInt file,CutSceneRedRand(i,j,1)
			WriteInt file,CutSceneRedRand(i,j,2)
			WriteInt file,CutSceneGreenRand(i,j,1)
			WriteInt file,CutSceneGreenRand(i,j,2)
			WriteInt file,CutSceneBlueRand(i,j,1)
			WriteInt file,CutSceneBlueRand(i,j,2)
			WriteFloat file,CutSceneRedV(i,j)
			WriteFloat file,CutSceneGreenV(i,j)
			WriteFloat file,CutSceneBlueV(i,j)
		Next
	Next
	CloseFile file
End Function

Function LoadCutSceneFile()
	ClearCutSceneFile()
	If AdventureCurrentArchive=1
		ex$="Archive/"
	Else
		ex$="Current/"
	EndIf
	; yep - load
	file=ReadFile(GlobalDirName$+"/Custom/Editing/"+ex$+AdventureFileName$+Chr$(47)+Str$(CurrentCutSceneNumber)+".cut")
	version=ReadInt(file)
	NoOfCameraEvents=ReadInt(file)
	NoOfLevelEvents=ReadInt(file)
	NoOfLightEvents=ReadInt(file)
	NoOfMusicEvents=ReadInt(file)
	NoOfMusicFadeEvents=ReadInt(file)
	NoOfSoundEvents=ReadInt(file)
	NoOfTextEvents=ReadInt(file)
	endTime=ReadInt(file)
	endType=ReadInt(file)
	cutData0=ReadInt(file)
	cutData1=ReadInt(file)
	cutData2=ReadInt(file)
	CutSceneSkipable=ReadInt(file)
	For i=1 To NoOfCameraEvents
		CamStopTime(i)=ReadInt(file)
		PosXs(i)=ReadFloat(file)
		PosZs(i)=ReadFloat(file)
		PosYs(i)=ReadFloat(file)
		PosXv(i)=ReadFloat(file)
		PosZv(i)=ReadFloat(file)
		PosYv(i)=ReadFloat(file)
		
		RotXs(i)=ReadFloat(file)
		RotZs(i)=ReadFloat(file)
		RotYs(i)=ReadFloat(file)
		RotXv(i)=ReadFloat(file)
		RotZv(i)=ReadFloat(file)
		RotYv(i)=ReadFloat(file)
	Next
	For i=1 To NoOfLevelEvents
		CutLevelTime(i)=ReadInt(file)
		CutLevelLevel(i)=ReadInt(file)
	Next
	For i=1 To NoOfLightEvents
		CutLightTime(i)=ReadInt(file)
		Rs(i)=ReadFloat(file)
		Gs(i)=ReadFloat(file)
		Bs(i)=ReadFloat(file)
		Rv(i)=ReadFloat(file)
		Gv(i)=ReadFloat(file)
		Bv(i)=ReadFloat(file)
	Next
	For i=1 To NoOfMusicEvents
		CutMusicTime(i)=ReadInt(file)
		CutMusicTrack(i)=ReadInt(file)
	Next
	For i=1 To NoOfMusicFadeEvents
		CutMusicFadeStart(i)=ReadInt(file)
		CutMusicFadeEnd(i)=ReadInt(file)
		Vs(i)=ReadFloat(file)
		Vv(i)=ReadFloat(file)
	Next
	For i=1 To NoOfSoundEvents
		CutSoundTime(i)=ReadInt(file)
		CutSoundID(i)=ReadInt(file)
	Next
	;text events
	For i=1 To NoOfTextEvents
		CutTextTime(i)=ReadInt(file)
		CutTextNoOfLines(i)=ReadInt(file)
		For j=1 To CutTextNoOfLines(i)
			CutText(i,j)=ReadString(file)
			CutTextX(i,j)=ReadFloat(file)
			CutTextY(i,j)=ReadFloat(file)
			CutTextSize(i,j)=ReadFloat(file)
			CutTextSpacing(i,j)=ReadFloat(file)
			CutSceneRed(i,j)=ReadInt(file)
			CutSceneGreen(i,j)=ReadInt(file)
			CutSceneBlue(i,j)=ReadInt(file)
			CutSceneRedRand(i,j,1)=ReadInt(file)
			CutSceneRedRand(i,j,2)=ReadInt(file)
			CutSceneGreenRand(i,j,1)=ReadInt(file)
			CutSceneGreenRand(i,j,2)=ReadInt(file)
			CutSceneBlueRand(i,j,1)=ReadInt(file)
			CutSceneBlueRand(i,j,2)=ReadInt(file)
			CutSceneRedV(i,j)=ReadFloat(file)
			CutSceneGreenV(i,j)=ReadFloat(file)
			CutSceneBlueV(i,j)=ReadFloat(file)
		Next
	Next
	CloseFile file
End Function

Function ClearCutSceneFile()
	NoOfCameraEvents=0
	NoOfLevelEvents=0
	NoOfLightEvents=0
	NoOfMusicEvents=0
	NoOfMusicFadeEvents=0
	NoOfSoundEvents=0
	NoOfTextEvents=0
	endTime=0
	endType=0
	cutData0=0
	cutData1=0
	cutData2=0
	CutSceneSkipable=0
	For i=1 To MaxEvents
		CamStopTime(i)=0
		PosXs(i)=0
		PosYs(i)=0
		PosZs(i)=0
		PosXv(i)=0
		PosYv(i)=0
		PosZv(i)=0
		
		RotXs(i)=0
		RotYs(i)=0
		RotZs(i)=0
		RotXv(i)=0
		RotYv(i)=0
		RotZv(i)=0
		
		CutLevelTime(i)=0
		CutLevelLevel(i)=0
		
		CutMusicTime(i)=0
		CutMusicTrack(i)=0
		
		CutMusicFadeStart(i)=0
		CutMusicFadeEnd(i)=0
		Vs(i)=0
		Vv(i)=0
		
		CutSoundTime(i)=0
		CutSoundID(i)=0
		
	Next
End Function