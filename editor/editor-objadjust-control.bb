Function AdjustObjectAdjuster(i)

	Fast=False
	If ShiftDown() Then Fast=True
	RawInput=False
	If CtrlDown() Then RawInput=True

	DelayTime=10 ;150
	SlowInt=1
	FastInt=10
	FastID=50
	FastTimer=25
	FastRotate=45
	SlowFloat#=0.01
	FastFloat#=0.1
	SlowScale#=0.001

	Select ObjectAdjuster$(i)
	Case "TextData0"
		If LeftMouse=True
			If FindAndReplaceKeyDown()
				If ConfirmFindAndReplace()
					Target$=CurrentObject\Attributes\TextData0
					CurrentObject\Attributes\TextData0$=InputString$("Replacement TextData0: ")
					ObjectAdjusterTextData0\Absolute=True
					For j=0 To NofObjects-1
						LevelObject.GameObject=LevelObjects(j)
						If LevelObject\Attributes\TextData0$=Target$
							LevelObject\Attributes\TextData0$=CurrentObject\Attributes\TextData0$
							UpdateLevelObjectModel(j)
						EndIf
					Next

					AddUnsavedChange()
				EndIf
			Else
				CurrentObject\Attributes\TextData0=InputString$("TextData0: ")
				ObjectAdjusterTextData0\Absolute=True
			EndIf
		EndIf
	Case "TextData1"
		If LeftMouse=True
			If FindAndReplaceKeyDown()
				If ConfirmFindAndReplace()
					Target$=CurrentObject\Attributes\TextData1$
					CurrentObject\Attributes\TextData1$=InputString$("Replacement TextData1: ")
					ObjectAdjusterTextData1\Absolute=True
					For j=0 To NofObjects-1
						LevelObject.GameObject=LevelObjects(j)
						If LevelObject\Attributes\TextData1$=Target$
							LevelObject\Attributes\TextData1$=CurrentObject\Attributes\TextData1$
							UpdateLevelObjectModel(j)
						EndIf
					Next

					AddUnsavedChange()
				EndIf
			Else
				CurrentObject\Attributes\TextData1$=InputString$("TextData1: ")
				ObjectAdjusterTextData1\Absolute=True
			EndIf
		EndIf
	Case "TextureName"
		If LeftMouse=True
			If FindAndReplaceKeyDown()
				If ConfirmFindAndReplace()
					Target$=CurrentObject\Attributes\TexName$
					InputTextureName("Replacement TextureName: ")
					For j=0 To NofObjects-1
						LevelObject.GameObject=LevelObjects(j)
						If LevelObject\Attributes\TexName$=Target$
							LevelObject\Attributes\TexName$=CurrentObject\Attributes\TexName$
							UpdateLevelObjectModel(j)
						EndIf
					Next

					AddUnsavedChange()
				EndIf
			Else
				InputTextureName("TextureName: ")
			EndIf
		EndIf

	Case "ModelName"
		If MouseScroll<>0
			If Left$(CurrentObject\Attributes\ModelName$,9)="!Obstacle"
				If ShiftDown()
					TheInt=FastInt
				Else
					TheInt=SlowInt
				EndIf
				ObstacleId=ObstacleNameToObstacleId(CurrentObject\Attributes\ModelName$)
				ObstacleId=ObstacleId+MouseScroll*TheInt
				If ObstacleId<0
					ObstacleId=0
				ElseIf ObstacleId>99
					ObstacleId=99
				EndIf
				If ObstacleId<10
					ObstacleIdString$="0"+ObstacleId
				Else
					ObstacleIdString$=ObstacleId
				EndIf
				CurrentObject\Attributes\ModelName$="!Obstacle"+ObstacleIdString$
			EndIf
		ElseIf LeftMouse=True
			If FindAndReplaceKeyDown()
				If ConfirmFindAndReplace()
					Target$=CurrentObject\Attributes\ModelName$
					InputModelName("Replacement ModelName: ")
					For j=0 To NofObjects-1
						LevelObject.GameObject=LevelObjects(j)
						If LevelObject\Attributes\ModelName$=Target$
							LevelObject\Attributes\ModelName$=CurrentObject\Attributes\ModelName$
							If CurrentObject\Attributes\ModelName$="!CustomModel"
								LevelObject\Attributes\TextData0$=CurrentObject\Attributes\TextData0$
							EndIf
							UpdateLevelObjectModel(j)
						EndIf
					Next

					AddUnsavedChange()
				EndIf
			Else
				InputModelName("ModelName: ")
			EndIf
		EndIf

	Case "DefensePower"
		OldValue=CurrentObject\Attributes\DefensePower

		CurrentObject\Attributes\DefensePower=AdjustObjectAdjusterInt(ObjectAdjusterDefensePower,CurrentObject\Attributes\DefensePower,SlowInt,FastInt,DelayTime)

		If CurrentObject\Attributes\DefensePower>34 Then CurrentObject\Attributes\DefensePower=0
		If CurrentObject\Attributes\DefensePower<0 Then CurrentObject\Attributes\DefensePower=34

		If CurrentObject\Attributes\DefensePower<>OldValue And SimulationLevel>=4
			PlaySoundFX(DefensePowerToSoundId(CurrentObject\Attributes\DefensePower),-1,-1)
		EndIf

	Case "AttackPower"
		CurrentObject\Attributes\AttackPower=AdjustObjectAdjusterInt(ObjectAdjusterAttackPower,CurrentObject\Attributes\AttackPower,SlowInt,FastInt,DelayTime)

	Case "DestructionType"
		CurrentObject\Attributes\DestructionType=AdjustObjectAdjusterInt(ObjectAdjusterDestructionType,CurrentObject\Attributes\DestructionType,SlowInt,FastInt,DelayTime)

	Case "YawAdjust"
		SlowFloat#=SlowInt
		FastFloat#=FastRotate
		CurrentObject\Attributes\YawAdjust=AdjustObjectAdjusterFloat(ObjectAdjusterYawAdjust,CurrentObject\Attributes\YawAdjust,SlowFloat#,FastFloat#,DelayTime)

		If CurrentObject\Attributes\YawAdjust>=360 Then CurrentObject\Attributes\YawAdjust=CurrentObject\Attributes\YawAdjust-360
		If CurrentObject\Attributes\YawAdjust<0 Then CurrentObject\Attributes\YawAdjust=CurrentObject\Attributes\YawAdjust+360

	Case "PitchAdjust"
		SlowFloat#=SlowInt
		FastFloat#=FastRotate
		CurrentObject\Attributes\PitchAdjust=AdjustObjectAdjusterFloat(ObjectAdjusterPitchAdjust,CurrentObject\Attributes\PitchAdjust,SlowFloat#,FastFloat#,DelayTime)

		If CurrentObject\Attributes\PitchAdjust>=360 Then CurrentObject\Attributes\PitchAdjust=CurrentObject\Attributes\PitchAdjust-360
		If CurrentObject\Attributes\PitchAdjust<0 Then CurrentObject\Attributes\PitchAdjust=CurrentObject\Attributes\PitchAdjust+360

	Case "RollAdjust"
		SlowFloat#=SlowInt
		FastFloat#=FastRotate
		CurrentObject\Attributes\RollAdjust=AdjustObjectAdjusterFloat(ObjectAdjusterRollAdjust,CurrentObject\Attributes\RollAdjust,SlowFloat#,FastFloat#,DelayTime)

		If CurrentObject\Attributes\RollAdjust>=360 Then CurrentObject\Attributes\RollAdjust=CurrentObject\Attributes\RollAdjust-360
		If CurrentObject\Attributes\RollAdjust<0 Then CurrentObject\Attributes\RollAdjust=CurrentObject\Attributes\RollAdjust+360

	Case "X"
		CurrentObject\Position\X=AdjustObjectAdjusterFloat(ObjectAdjusterX,CurrentObject\Position\X,SlowFloat#,FastFloat#,DelayTime)
	Case "Y"
		CurrentObject\Position\Y=AdjustObjectAdjusterFloat(ObjectAdjusterY,CurrentObject\Position\Y,SlowFloat#,FastFloat#,DelayTime)
	Case "Z"
		CurrentObject\Position\Z=AdjustObjectAdjusterFloat(ObjectAdjusterZ,CurrentObject\Position\Z,SlowFloat#,FastFloat#,DelayTime)

	Case "XAdjust"
		CurrentObject\Attributes\XAdjust=AdjustObjectAdjusterFloat(ObjectAdjusterXAdjust,CurrentObject\Attributes\XAdjust,SlowFloat#,FastFloat#,DelayTime)
	Case "YAdjust"
		CurrentObject\Attributes\YAdjust=AdjustObjectAdjusterFloat(ObjectAdjusterYAdjust,CurrentObject\Attributes\YAdjust,SlowFloat#,FastFloat#,DelayTime)
	Case "ZAdjust"
		CurrentObject\Attributes\ZAdjust=AdjustObjectAdjusterFloat(ObjectAdjusterZAdjust,CurrentObject\Attributes\ZAdjust,SlowFloat#,FastFloat#,DelayTime)

	Case "XScale"
		SlowFloat#=SlowScale#
		CurrentObject\Attributes\XScale=AdjustObjectAdjusterFloat(ObjectAdjusterXScale,CurrentObject\Attributes\XScale,SlowFloat#,FastFloat#,DelayTime)
	Case "YScale"
		SlowFloat#=SlowScale#
		CurrentObject\Attributes\YScale=AdjustObjectAdjusterFloat(ObjectAdjusterYScale,CurrentObject\Attributes\YScale,SlowFloat#,FastFloat#,DelayTime)
	Case "ZScale"
		SlowFloat#=SlowScale#
		CurrentObject\Attributes\ZScale=AdjustObjectAdjusterFloat(ObjectAdjusterZScale,CurrentObject\Attributes\ZScale,SlowFloat#,FastFloat#,DelayTime)

	Case "ID"
		FastInt=FastID
		CurrentObject\Attributes\ID=AdjustObjectAdjusterInt(ObjectAdjusterID,CurrentObject\Attributes\ID,SlowInt,FastInt,DelayTime)

	Case "Type"
		CurrentObject\Attributes\LogicType=AdjustObjectAdjusterInt(ObjectAdjusterLogicType,CurrentObject\Attributes\LogicType,SlowInt,FastInt,DelayTime)
	Case "SubType"
		CurrentObject\Attributes\LogicSubType=AdjustObjectAdjusterInt(ObjectAdjusterLogicSubType,CurrentObject\Attributes\LogicSubType,SlowInt,FastInt,DelayTime)

		If CurrentObject\Attributes\LogicType=179 ; Custom Item

			Min=-400
			Max=509

			If CurrentObject\Attributes\LogicSubType<Min
				CurrentObject\Attributes\LogicSubType=Max
			EndIf

			GapSubType(-400,-300)
			GapSubType(-300,-200)
			GapSubType(-195,-100)
			GapSubType(-98,-6)

			If CurrentObject\Attributes\LogicSubType>27 And CurrentObject\Attributes\LogicSubType<490
				CurrentObject\Attributes\LogicSubType=509
			Else If CurrentObject\Attributes\LogicSubType>489 And CurrentObject\Attributes\LogicSubType<509
				CurrentObject\Attributes\LogicSubType=27

			Else If CurrentObject\Attributes\LogicSubType>Max
				CurrentObject\Attributes\LogicSubType=Min

			Else If CurrentObject\Attributes\LogicSubType=8
				CurrentObject\Attributes\LogicSubType=10
			Else If CurrentObject\Attributes\LogicSubType=9
				CurrentObject\Attributes\LogicSubType=7

			Else If CurrentObject\Attributes\LogicSubType=18
				CurrentObject\Attributes\LogicSubType=20
			Else If CurrentObject\Attributes\LogicSubType=19
				CurrentObject\Attributes\LogicSubType=17

			EndIf

		EndIf
		If CurrentObject\Attributes\LogicType=230 ; FireFlower
			If CurrentObject\Attributes\LogicSubType<0 Then CurrentObject\Attributes\LogicSubType=3
			If CurrentObject\Attributes\LogicSubType>3 Then CurrentObject\Attributes\LogicSubType=0
		EndIf
		If CurrentObject\Attributes\LogicType=370 ; Crab
			If CurrentObject\Attributes\LogicSubType<0 Then CurrentObject\Attributes\LogicSubType=1
			If CurrentObject\Attributes\LogicSubType>1 Then CurrentObject\Attributes\LogicSubType=0
		EndIf

	Case "Active"
		CurrentObject\Attributes\Active=AdjustObjectAdjusterToggle(ObjectAdjusterActive,CurrentObject\Attributes\Active,SlowInt,FastInt,RawInput,0,1001,DelayTime)

	Case "ActivationSpeed"
		SlowInt=SlowInt*2
		FastInt=FastInt*2
		CurrentObject\Attributes\ActivationSpeed=AdjustObjectAdjusterInt(ObjectAdjusterActivationSpeed,CurrentObject\Attributes\ActivationSpeed,SlowInt,FastInt,DelayTime)
	Case "ActivationType"
		CurrentObject\Attributes\ActivationType=AdjustObjectAdjusterInt(ObjectAdjusterActivationType,CurrentObject\Attributes\ActivationType,SlowInt,FastInt,DelayTime)

		;If CurrentObject\Attributes\ModelName$="!SteppingStone"
		;	If LeftMouse=True Or RightMouse=True
		;		If CurrentObject\Attributes\ActivationType=3
		;			CurrentObject\Attributes\ActivationType=16
		;		Else If CurrentObject\Attributes\ActivationType=16
		;			CurrentObject\Attributes\ActivationType=21
		;		Else
		;			CurrentObject\Attributes\ActivationType=3
		;		EndIf
		;	EndIf
		;Else If CurrentObject\Attributes\ModelName$="!ColourGate"
		;	If LeftMouse=True Or RightMouse=True
		;		If CurrentObject\Attributes\ActivationType=1
		;			CurrentObject\Attributes\ActivationType=2
		;		Else If CurrentObject\Attributes\ActivationType=2
		;			CurrentObject\Attributes\ActivationType=3
		;		Else If CurrentObject\Attributes\ActivationType=3
		;			CurrentObject\Attributes\ActivationType=11
		;		Else If CurrentObject\Attributes\ActivationType=11
		;			CurrentObject\Attributes\ActivationType=21
		;		Else
		;			CurrentObject\Attributes\ActivationType=1
		;		EndIf
		;	EndIf
		;Else If CurrentObject\Attributes\ModelName$="!Autodoor"
		;	If LeftMouse=True Or RightMouse=True
		;		If CurrentObject\Attributes\ActivationType=11
		;			CurrentObject\Attributes\ActivationType=17
		;		Else If CurrentObject\Attributes\ActivationType=17
		;			CurrentObject\Attributes\ActivationType=18
		;		Else If CurrentObject\Attributes\ActivationType=18
		;			CurrentObject\Attributes\ActivationType=19
		;		Else If CurrentObject\Attributes\ActivationType=19
		;			CurrentObject\Attributes\ActivationType=20
		;		Else
		;			CurrentObject\Attributes\ActivationType=11
		;		EndIf
		;	EndIf

	Case "TimerMax1"
		FastInt=FastTimer
		CurrentObject\Attributes\TimerMax1=AdjustObjectAdjusterInt(ObjectAdjusterTimerMax1,CurrentObject\Attributes\TimerMax1,SlowInt,FastInt,DelayTime)
	Case "TimerMax2"
		FastInt=FastTimer
		CurrentObject\Attributes\TimerMax2=AdjustObjectAdjusterInt(ObjectAdjusterTimerMax2,CurrentObject\Attributes\TimerMax2,SlowInt,FastInt,DelayTime)
	Case "Timer"
		FastInt=FastTimer
		CurrentObject\Attributes\Timer=AdjustObjectAdjusterInt(ObjectAdjusterTimer,CurrentObject\Attributes\Timer,SlowInt,FastInt,DelayTime)

	Case "ButtonPush"
		CurrentObject\Attributes\ButtonPush=AdjustObjectAdjusterToggle(ObjectAdjusterButtonPush,CurrentObject\Attributes\ButtonPush,SlowInt,FastInt,RawInput,0,1,DelayTime)

	Case "WaterReact"
		CurrentObject\Attributes\WaterReact=AdjustObjectAdjusterInt(ObjectAdjusterWaterReact,CurrentObject\Attributes\WaterReact,SlowInt,FastInt,DelayTime)
	Case "Freezable"
		CurrentObject\Attributes\Freezable=AdjustObjectAdjusterInt(ObjectAdjusterFreezable,CurrentObject\Attributes\Freezable,SlowInt,FastInt,DelayTime)
	Case "Frozen"
		CurrentObject\Attributes\Frozen=AdjustObjectAdjusterInt(ObjectAdjusterFrozen,CurrentObject\Attributes\Frozen,SlowInt,FastInt,DelayTime)
	Case "Teleportable"
		CurrentObject\Attributes\Teleportable=AdjustObjectAdjusterToggle(ObjectAdjusterTeleportable,CurrentObject\Attributes\Teleportable,SlowInt,FastInt,RawInput,0,1,DelayTime)

	Case "Data0"
		If CurrentObject\Attributes\ModelName$="!Gem"
			ObjectAdjusterData0\RandomMinDefault=0
			ObjectAdjusterData0\RandomMaxDefault=2
		Else
			ObjectAdjusterData0\RandomMinDefault=0
			ObjectAdjusterData0\RandomMaxDefault=10
		EndIf

		OldData=CurrentObject\Attributes\Data0

		;CurrentObject\Attributes\Data0=AdjustInt("Data0: ", CurrentObject\Attributes\Data0, SlowInt, FastInt, DelayTime)
		CurrentObject\Attributes\Data0=AdjustObjectAdjusterInt(ObjectAdjusterData0,CurrentObject\Attributes\Data0,SlowInt,FastInt,DelayTime)

		CurrentObject\Attributes\ModelName$=CurrentObject\Attributes\ModelName$
		CurrentObject\Attributes\LogicType=CurrentObject\Attributes\LogicType

		If CurrentObject\Attributes\ModelName$="!Scritter" ;Or CurrentObject\Attributes\ModelName$="!Cuboid" Or CurrentObject\Attributes\LogicType=424
			; colours 0-6
			If CurrentObject\Attributes\Data0>6 CurrentObject\Attributes\Data0=0
			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=6

;		Else If CurrentObject\Attributes\ModelName$="!Obstacle51" Or CurrentObject\Attributes\ModelName$="!Obstacle55" Or CurrentObject\Attributes\ModelName$="!Obstacle59"
			; Shape
;			If CurrentObject\Attributes\Data0>3 CurrentObject\Attributes\Data0=0
;			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=3
		EndIf

		If IsObjectLogicFourColorButton(CurrentObject\Attributes\LogicType,CurrentObject\Attributes\LogicSubType)
			SetThreeOtherDataIfNotEqual(1,2,3,0,OldData)
		EndIf

		If CurrentObject\Attributes\ModelName$="!Retrolasergate"
			; Color
			If CurrentObject\Attributes\Data0>63 CurrentObject\Attributes\Data0=0
			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=63
		EndIf

		If CurrentObject\Attributes\ModelName$="!Gem"
			; Shape
			If CurrentObject\Attributes\Data0>2 CurrentObject\Attributes\Data0=0
			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=2
		EndIf
;		If CurrentObject\Attributes\ModelName$="!Crystal"
;			If CurrentObject\Attributes\Data0>1 CurrentObject\Attributes\Data0=0
;			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=1
;		EndIf
		If CurrentObject\Attributes\LogicType=260 ; Spikeyball
			If CurrentObject\Attributes\Data1=2
				If CurrentObject\Attributes\Data0>7 CurrentObject\Attributes\Data0=0
				If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=7
			Else
				If CurrentObject\Attributes\Data0>3 CurrentObject\Attributes\Data0=0
				If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=3
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=230 ; FireFlower
			If CurrentObject\Attributes\Data0>7 CurrentObject\Attributes\Data0=0
			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=7
		EndIf
		If CurrentObject\Attributes\LogicType=220 ; Turtle
			; Direction
			If CurrentObject\Attributes\Data0>3 CurrentObject\Attributes\Data0=0
			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=3
		EndIf
		If CurrentObject\Attributes\ModelName="!Kaboom"
			; texture
			If CurrentObject\Attributes\Data0>5 CurrentObject\Attributes\Data0=1
			If CurrentObject\Attributes\Data0<1 CurrentObject\Attributes\Data0=5
		EndIf

		If CurrentObject\Attributes\ModelName$="!Wisp"
			; texture
			If CurrentObject\Attributes\Data0>9 CurrentObject\Attributes\Data0=0
			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=9
		EndIf

		If CurrentObject\Attributes\ModelName$="!Sign"
			; shape
			If CurrentObject\Attributes\Data0>5 CurrentObject\Attributes\Data0=0
			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=5
		EndIf

		If CurrentObject\Attributes\ModelName$="!WaterFall"
			; liquid type
			If CurrentObject\Attributes\Data0>2 CurrentObject\Attributes\Data0=0
			If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=2
		EndIf

		If CurrentObject\Attributes\ModelName$="!NPC"
			; Texture
			If CurrentObject\Attributes\Data0>8 CurrentObject\Attributes\Data0=1
			If CurrentObject\Attributes\Data0<1 CurrentObject\Attributes\Data0=8
		EndIf

		If CurrentObject\Attributes\LogicType=470 Or CurrentObject\Attributes\LogicType=471 ; ghost or wraith
			If CurrentObject\Attributes\Data1<2 CurrentObject\Attributes\Data1=2
		EndIf

		; Q - player NPC functionality (lock adjuster since it's set in-engine)
		If CurrentObject\Attributes\ModelName$="!PlayerNPC"
			If CurrentObject\Attributes\Data0<>1 CurrentObject\Attributes\Data0=1
		EndIf
	Case "Data1"
		If CurrentObject\Attributes\ModelName$="!Gem"
			ObjectAdjusterData1\RandomMinDefault=0
			ObjectAdjusterData1\RandomMaxDefault=6
		Else
			ObjectAdjusterData1\RandomMinDefault=0
			ObjectAdjusterData1\RandomMaxDefault=10
		EndIf

		OldData=CurrentObject\Attributes\Data1

		;CurrentObject\Attributes\Data1=AdjustInt("Data1: ", CurrentObject\Attributes\Data1, SlowInt, FastInt, DelayTime)
		CurrentObject\Attributes\Data1=AdjustObjectAdjusterInt(ObjectAdjusterData1,CurrentObject\Attributes\Data1,SlowInt,FastInt,DelayTime)

;		If CurrentObject\Attributes\ModelName$="!Obstacle51" Or CurrentObject\Attributes\ModelName$="!Obstacle55" Or CurrentObject\Attributes\ModelName$="!Obstacle59"
;			; Texture
;			If CurrentObject\Attributes\Data1>3 CurrentObject\Attributes\Data1=0
;			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=3
;		EndIf

		If CurrentObject\Attributes\LogicType=190
			; particle spray intensity
			If CurrentObject\Attributes\Data1>3 CurrentObject\Attributes\Data1=1
			If CurrentObject\Attributes\Data1<1 CurrentObject\Attributes\Data1=3
		EndIf

		If CurrentObject\Attributes\LogicType=230 ; FireFlower
			If CurrentObject\Attributes\Data1>3 CurrentObject\Attributes\Data1=0
			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=3
		EndIf

		If CurrentObject\Attributes\LogicType=242 ; Cuboid

			If CurrentObject\Attributes\Data1>1 CurrentObject\Attributes\Data1=0
			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=1

		EndIf

		If CurrentObject\Attributes\LogicType=260 ; SpikeyBall
			If CurrentObject\Attributes\Data1>2 CurrentObject\Attributes\Data1=0
			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=2

			If CurrentObject\Attributes\Data1=2
				If CurrentObject\Attributes\Data0>7 CurrentObject\Attributes\Data0=0
				If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=7
			Else
				If CurrentObject\Attributes\Data0>3 CurrentObject\Attributes\Data0=0
				If CurrentObject\Attributes\Data0<0 CurrentObject\Attributes\Data0=3
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=250 ; Chomper
			If CurrentObject\Attributes\Data1>3 CurrentObject\Attributes\Data1=0
			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=3
		EndIf
		If CurrentObject\Attributes\LogicType=220 ; Turtle
			If CurrentObject\Attributes\Data1>1 CurrentObject\Attributes\Data1=0
			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=1
		EndIf
		If CurrentObject\Attributes\LogicType=370 ; Crab
			If CurrentObject\Attributes\Data1>3 CurrentObject\Attributes\Data1=0
			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=3
		EndIf

		; Q - player NPC functionality
		If IsModelNPC(CurrentObject\Attributes\ModelName$)
			; Expression
			If CurrentObject\Attributes\Data1>4 CurrentObject\Attributes\Data1=0
			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=4
		EndIf

		; ufo or retro z-bot or zipbot or zapbot
;		If CurrentObject\Attributes\LogicType=422 Or CurrentObject\Attributes\LogicType=423 Or CurrentObject\Attributes\LogicType=430 Or CurrentObject\Attributes\LogicType=431
;			; turning
;			If CurrentObject\Attributes\Data1>1 CurrentObject\Attributes\Data1=0
;			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=1
;		EndIf

		If CurrentObject\Attributes\ModelName$="!Portal Warp"
			; ???
			If CurrentObject\Attributes\Data1>1 CurrentObject\Attributes\Data1=0
			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=1
		EndIf

		If CurrentObject\Attributes\ModelName$="!Sign"
			; texture
			If CurrentObject\Attributes\Data1>5 CurrentObject\Attributes\Data1=0
			If CurrentObject\Attributes\Data1<0 CurrentObject\Attributes\Data1=5
		EndIf

		If CommandAdjusterStartDataIndex()=0
			If CurrentObject\Attributes\Data0=10 Or CurrentObject\Attributes\Data0=11
				If CurrentObject\Attributes\Data1<>OldData And SimulationLevel>=4
					PlaySoundFX(CurrentObject\Attributes\Data1,-1,-1)
				EndIf
			EndIf
		EndIf

	Case "Data2"
		If CurrentObject\Attributes\ModelName$="!NPC"
			; Use vanilla ranges
			; Hats
			ObjectAdjusterData2\RandomMinDefault=0
			ObjectAdjusterData2\RandomMaxDefault=56
		Else
			ObjectAdjusterData2\RandomMinDefault=0
			ObjectAdjusterData2\RandomMaxDefault=10
		EndIf

		;CurrentObject\Attributes\Data2=AdjustInt("Data2: ", CurrentObject\Attributes\Data2, SlowInt, FastInt, DelayTime)
		CurrentObject\Attributes\Data2=AdjustObjectAdjusterInt(ObjectAdjusterData2,CurrentObject\Attributes\Data2,SlowInt,FastInt,DelayTime)

		If CurrentObject\Attributes\LogicType=280 Or CurrentObject\Attributes\LogicType=410 ; Spring or FlipBridge
			; direction 0-7
			If CurrentObject\Attributes\Data2>7 CurrentObject\Attributes\Data2=0
			If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=7
		EndIf
		If CurrentObject\Attributes\LogicType=281 Or CurrentObject\Attributes\LogicType=282 Or CurrentObject\Attributes\LogicType=45 Or CurrentObject\Attributes\LogicType=46 ; Suctube or Suctube X or Conveyor
			; direction 0-3
			If CurrentObject\Attributes\Data2>3 CurrentObject\Attributes\Data2=0
			If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=3
		EndIf

		; transporter, pushbot
		If CurrentObject\Attributes\LogicType=210 Or CurrentObject\Attributes\LogicType=432
			; direction 0-3
			If CurrentObject\Attributes\Data2>3 CurrentObject\Attributes\Data2=0
			If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=3
		EndIf

		If CurrentObject\Attributes\LogicType=90
			If (CurrentObject\Attributes\LogicSubType Mod 32)=16 Or (CurrentObject\Attributes\LogicSubType Mod 32)=17
				; direction 0-1
				If CurrentObject\Attributes\Data2>1 CurrentObject\Attributes\Data2=0
				If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=1
			EndIf
			If CurrentObject\Attributes\LogicSubType=11
				Select CurrentObject\Attributes\Data0
				Case 0
					; x goal
					If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=0
				Case 1
					; talkable
					If CurrentObject\Attributes\Data2<-1 CurrentObject\Attributes\Data2=-1
				End Select
			EndIf
		EndIf
		If CurrentObject\Attributes\LogicType=190
			; particle spray direction
			If CurrentObject\Attributes\Data2>5 CurrentObject\Attributes\Data2=0
			If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=5
		EndIf

		If CurrentObject\Attributes\ModelName$="!NPC"
			;If CurrentObject\Attributes\Data2>56 CurrentObject\Attributes\Data2=0
			;If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=56
			CurrentObject\Attributes\Data3=1

		EndIf

		If CurrentObject\Attributes\ModelName$="!Thwart"
			; colour
			If CurrentObject\Attributes\Data2>7 CurrentObject\Attributes\Data2=0
			If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=7
		EndIf

		If CurrentObject\Attributes\ModelName$="!Wraith"
			; Doubles as both magic type and texture
			If CurrentObject\Attributes\Data2>2 CurrentObject\Attributes\Data2=0
			If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=2
		EndIf

		; Q - player NPC functionality (lock adjuster since it's set in-engine)
		If CurrentObject\Attributes\ModelName$="!PlayerNPC"
			If CurrentObject\Attributes\Data2<>0 CurrentObject\Attributes\Data2=0
		EndIf
;		If CurrentObject\Attributes\LogicType=433 ; Z-Bot NPC
;			If CurrentObject\Attributes\Data2>1 CurrentObject\Attributes\Data2=0
;			If CurrentObject\Attributes\Data2<0 CurrentObject\Attributes\Data2=1
;		EndIf

	Case "Data3"
		OldData=CurrentObject\Attributes\Data3

		;CurrentObject\Attributes\Data3=AdjustInt("Data3: ", CurrentObject\Attributes\Data3, SlowInt, FastInt, DelayTime)
		CurrentObject\Attributes\Data3=AdjustObjectAdjusterInt(ObjectAdjusterData3,CurrentObject\Attributes\Data3,SlowInt,FastInt,DelayTime)

		If CurrentObject\Attributes\LogicType=190
			If CurrentObject\Attributes\Data3<0 Then CurrentObject\Attributes\Data3=0
			Select CurrentObject\Attributes\LogicSubType
			Case 4
				If CurrentObject\Attributes\Data3>1 Then CurrentObject\Attributes\Data3=0
			Case 5
				If CurrentObject\Attributes\Data3>6 Then CurrentObject\Attributes\Data3=0
			End Select
		EndIf

		If CurrentObject\Attributes\LogicType=40 ; stepping stone
			; sound
			If CurrentObject\Attributes\Data3>3 CurrentObject\Attributes\Data3=0
			If CurrentObject\Attributes\Data3<0 CurrentObject\Attributes\Data3=3
		EndIf
		If CurrentObject\Attributes\LogicType=90 And CurrentObject\Attributes\LogicSubType=11 ; button
			Select CurrentObject\Attributes\Data0
			Case 0
				; y goal
				If CurrentObject\Attributes\Data3<0 CurrentObject\Attributes\Data3=0
			Case 1
				; y goal
				If CurrentObject\Attributes\Data3<-1 CurrentObject\Attributes\Data3=4
				If CurrentObject\Attributes\Data3>4 CurrentObject\Attributes\Data3=-1
			Case 2
				; how many particles
				If CurrentObject\Attributes\Data3<0 CurrentObject\Attributes\Data3=9
				If CurrentObject\Attributes\Data3>9 CurrentObject\Attributes\Data3=0
			End Select
		EndIf
		If CurrentObject\Attributes\LogicType=230 ; FireFlower
			; hitpoints
			If CurrentObject\Attributes\Data3<0 CurrentObject\Attributes\Data3=0
		EndIf

;		If CurrentObject\Attributes\LogicType=432 ; moobot
			; pushbot left/right turn,
;			If CurrentObject\Attributes\Data3<0 CurrentObject\Attributes\Data3=2
;			If CurrentObject\Attributes\Data3>2 CurrentObject\Attributes\Data3=0
;		EndIf
		If  CurrentObject\Attributes\LogicType=45 ; conveyor lead
			; turn direction
			If CurrentObject\Attributes\Data3<0 CurrentObject\Attributes\Data3=1
			If CurrentObject\Attributes\Data3>1 CurrentObject\Attributes\Data3=0
		EndIf

;		If  CurrentObject\Attributes\LogicType=46 ; conveyor tail
;			If CurrentObject\Attributes\Data3<1 CurrentObject\Attributes\Data3=1
;
;		EndIf

;		If CurrentObject\Attributes\ModelName$="!Suctube" Or CurrentObject\Attributes\ModelName$="!SuctubeX"
;			; Suctube tex
;			If CurrentObject\Attributes\Data3<0 CurrentObject\Attributes\Data3=0
;			If CurrentObject\Attributes\Data3>2 CurrentObject\Attributes\Data3=2
;		EndIf

		If SimulationLevel>=4 And SomethingWasAdjusted
			If CommandAdjusterStartDataIndex()=2
				If CurrentObject\Attributes\Data2=10 Or CurrentObject\Attributes\Data2=11
					;If CurrentObject\Attributes\Data3<>OldData
					PlaySoundFX(CurrentObject\Attributes\Data3,-1,-1)
					;EndIf
				EndIf
			EndIf

			If CurrentObject\Attributes\LogicType=190
				If CurrentObject\Attributes\LogicSubType=4 ; Sparks
					If CurrentObject\Attributes\Data3>=1
						SoundPitch SoundFX(16),Rand(24000,29000)
						PlaySoundFX(16,-1,-1)
					EndIf
				ElseIf CurrentObject\Attributes\LogicSubType=5 ; Blinker
					If CurrentObject\Attributes\Data3>=1
						If CurrentObject\Attributes\Data3=1 ; quiet magic
							SoundPitch SoundFX(90),Rand(16000,20000)
							PlaySoundFX(90,-1,-1)
						EndIf
						If CurrentObject\Attributes\Data3=2 ; loud mecha

							PlaySoundFX(35,-1,-1)
						EndIf

						If CurrentObject\Attributes\Data3=3 ; variable gong
							SoundPitch SoundFX(13),Rand(24000,44000)
							PlaySoundFX(13,-1,-1)
						EndIf
						If CurrentObject\Attributes\Data3=4 ; grow

							PlaySoundFX(92,-1,-1)
						EndIf

						If CurrentObject\Attributes\Data3=5 ; floing

							PlaySoundFX(93,-1,-1)
						EndIf

						If CurrentObject\Attributes\Data3=6 ; gem

							PlaySoundFX(11,-1,-1)
						EndIf
					EndIf
				EndIf
			EndIf
		EndIf

		; Q - player NPC functionality (lock adjuster since it's set in-engine)
		If CurrentObject\Attributes\ModelName$="!PlayerNPC"
			If CurrentObject\Attributes\Data3<>0 CurrentObject\Attributes\Data3=0
		EndIf
	Case "Data4"
		If CurrentObject\Attributes\ModelName$="!NPC"
			; Glasses
			ObjectAdjusterData4\RandomMinDefault=101
			ObjectAdjusterData4\RandomMaxDefault=116
		Else
			ObjectAdjusterData4\RandomMinDefault=0
			ObjectAdjusterData4\RandomMaxDefault=10
		EndIf

		Adj=1
		AdjFast=10
		If CurrentObject\Attributes\LogicType=90 And CurrentObject\Attributes\LogicSubType=10 ; LevelExit
			Adj=45
			AdjFast=45
		EndIf

		OldData=CurrentObject\Attributes\Data4

		;CurrentObject\Attributes\Data4=AdjustInt("Data4: ", CurrentObject\Attributes\Data4, Adj, AdjFast, DelayTime)
		CurrentObject\Attributes\Data4=AdjustObjectAdjusterInt(ObjectAdjusterData4,CurrentObject\Attributes\Data4,Adj,AdjFast,DelayTime)

		If CurrentObject\Attributes\LogicType=90
			If CurrentObject\Attributes\LogicSubType=10 ; LevelExit
				;playerstartingyaw
				If CurrentObject\Attributes\Data4<0 Then CurrentObject\Attributes\Data4=360-45
				If CurrentObject\Attributes\Data4>359 Then CurrentObject\Attributes\Data4=0
			ElseIf CurrentObject\Attributes\LogicSubType=11 ; NPC Modifier
				If (CurrentObject\Attributes\Data0=0 Or CurrentObject\Attributes\Data0=2)
					; repeatable
					If CurrentObject\Attributes\Data4<0 CurrentObject\Attributes\Data4=1
					If CurrentObject\Attributes\Data4>1 CurrentObject\Attributes\Data4=0
				ElseIf CurrentObject\Attributes\Data0=1
					; yaw
					If CurrentObject\Attributes\Data4<-1 CurrentObject\Attributes\Data4=359
					If CurrentObject\Attributes\Data4>359 CurrentObject\Attributes\Data4=0
				EndIf
			ElseIf IsObjectSubTypeFourColorButton(CurrentObject\Attributes\LogicSubType)
				SetThreeOtherDataIfNotEqual(5,6,7,4,OldData)
			EndIf
		EndIf

		If CurrentObject\Attributes\ModelName$="!NPC"
;			If CurrentObject\Attributes\Data4=-1 CurrentObject\Attributes\Data4=116
;			If CurrentObject\Attributes\Data4=1 CurrentObject\Attributes\Data4=101
;			If CurrentObject\Attributes\Data4=100 CurrentObject\Attributes\Data4=0
;			If CurrentObject\Attributes\Data4=117 CurrentObject\Attributes\Data4=0

			; Set the glasses color back to 1.
			CurrentObject\Attributes\Data5=0
		EndIf

		If CurrentObject\Attributes\LogicType=190
			If CurrentObject\Attributes\Data4<0 Then CurrentObject\Attributes\Data4=0
			If CurrentObject\Attributes\Data4>1 Then CurrentObject\Attributes\Data4=0
		EndIf

;		If CurrentObject\Attributes\LogicType=431 Or CurrentObject\Attributes\LogicType=422 ; Zapbot or UFO
;			; zapbot track?
;			If CurrentObject\Attributes\Data4<0 CurrentObject\Attributes\Data4=1
;			If CurrentObject\Attributes\Data4>1 CurrentObject\Attributes\Data4=0
;		EndIf

		If  CurrentObject\Attributes\ModelName$="!Conveyor"
			; visual type
			If CurrentObject\Attributes\Data4<0 CurrentObject\Attributes\Data4=4
			If CurrentObject\Attributes\Data4>4 CurrentObject\Attributes\Data4=0
		EndIf

		If CurrentObject\Attributes\LogicType=281 ; Suctube
			; sound
			If CurrentObject\Attributes\Data4<0 CurrentObject\Attributes\Data4=1
			If CurrentObject\Attributes\Data4>1 CurrentObject\Attributes\Data4=0
		EndIf

		; Q - player NPC functionality (lock adjuster since it's set in-engine)
		If CurrentObject\Attributes\ModelName$="!PlayerNPC"
			If CurrentObject\Attributes\Data4<>0 CurrentObject\Attributes\Data4=0
		EndIf
	Case "Data5"
		;CurrentObject\Attributes\Data5=AdjustInt("Data5: ", CurrentObject\Attributes\Data5, SlowInt, FastInt, DelayTime)
		CurrentObject\Attributes\Data5=AdjustObjectAdjusterInt(ObjectAdjusterData5,CurrentObject\Attributes\Data5,SlowInt,FastInt,DelayTime)

		If CurrentObject\Attributes\LogicType=90 ; button
			If (CurrentObject\Attributes\LogicSubType Mod 32)=15
				; repeatable
				If CurrentObject\Attributes\Data5>1 CurrentObject\Attributes\Data5=0
				If CurrentObject\Attributes\Data5<0 CurrentObject\Attributes\Data5=1
			EndIf
			If CurrentObject\Attributes\LogicSubType=11
				If CurrentObject\Attributes\Data0=0
					; timer
					If CurrentObject\Attributes\Data5<0 CurrentObject\Attributes\Data5=0
				ElseIf CurrentObject\Attributes\Data0=1
					; repeatable
					If CurrentObject\Attributes\Data5>1 CurrentObject\Attributes\Data5=0
					If CurrentObject\Attributes\Data5<0 CurrentObject\Attributes\Data5=1
				EndIf
			ElseIf CurrentObject\Attributes\LogicSubType=10
				; levelexit flyover
				If CurrentObject\Attributes\Data5>1 CurrentObject\Attributes\Data5=0
				If CurrentObject\Attributes\Data5<0 CurrentObject\Attributes\Data5=1
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=45 Or CurrentObject\Attributes\LogicType=46 ; Conveyor
			; Logic
			If CurrentObject\Attributes\Data5>1 CurrentObject\Attributes\Data5=0
			If CurrentObject\Attributes\Data5<0 CurrentObject\Attributes\Data5=1
		EndIf

		If CurrentObject\Attributes\ModelName$="!GlowWorm"  Or CurrentObject\Attributes\ModelName$="!Zipper"
			If CurrentObject\Attributes\Data5>255 CurrentObject\Attributes\Data5=0
			If CurrentObject\Attributes\Data5<0 CurrentObject\Attributes\Data5=255
		EndIf

		If CurrentObject\Attributes\LogicType=281 ;CurrentObject\Attributes\ModelName$="!Suctube"
			; particles
			If CurrentObject\Attributes\Data5>1 CurrentObject\Attributes\Data5=0
			If CurrentObject\Attributes\Data5<0 CurrentObject\Attributes\Data5=1
		EndIf

		; Q - player NPC functionality (lock adjuster since it's set in-engine)
		If CurrentObject\Attributes\ModelName$="!PlayerNPC"
			If CurrentObject\Attributes\Data5<>0 CurrentObject\Attributes\Data5=0
		EndIf
	Case "Data6"
		If CurrentObject\Attributes\LogicType=110 ; Stinker NPC
			ObjectAdjusterData6\RandomMinDefault=0
			ObjectAdjusterData6\RandomMaxDefault=2
		Else
			ObjectAdjusterData6\RandomMinDefault=0
			ObjectAdjusterData6\RandomMaxDefault=10
		EndIf

		;CurrentObject\Attributes\Data6=AdjustInt("Data6: ", CurrentObject\Attributes\Data6, 1, 10, 150)
		CurrentObject\Attributes\Data6=AdjustObjectAdjusterInt(ObjectAdjusterData6,CurrentObject\Attributes\Data6,SlowInt,FastInt,DelayTime)

		If CurrentObject\Attributes\LogicType=90 And CurrentObject\Attributes\LogicSubType=11 ; NPC Modifier
			If CurrentObject\Attributes\Data0=0 ; NPC Move
				; timer reset
				If CurrentObject\Attributes\Data6<0 CurrentObject\Attributes\Data6=0
			;ElseIf CurrentObject\Attributes\Data0=1
				; walk anim
				;If CurrentObject\Attributes\Data6<-1 CurrentObject\Attributes\Data6=2
				;If CurrentObject\Attributes\Data6>2 CurrentObject\Attributes\Data6=-1
			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=110 ; Stinker NPC
			; WalkAnim
			;If CurrentObject\Attributes\Data6>2 CurrentObject\Attributes\Data6=0
			;If CurrentObject\Attributes\Data6<0 CurrentObject\Attributes\Data6=2
		EndIf

;		If CurrentObject\Attributes\LogicType=290 Or CurrentObject\Attributes\LogicType=380 Or CurrentObject\Attributes\LogicType=433 ; Thwart or Ice Troll or Z-Bot NPC
;			; Shooter
;			If CurrentObject\Attributes\Data6>1 CurrentObject\Attributes\Data6=0
;			If CurrentObject\Attributes\Data6<0 CurrentObject\Attributes\Data6=1
;		EndIf

		If CurrentObject\Attributes\ModelName$="!GlowWorm"  Or CurrentObject\Attributes\ModelName$="!Zipper"
			If CurrentObject\Attributes\Data6>255 CurrentObject\Attributes\Data6=0
			If CurrentObject\Attributes\Data6<0 CurrentObject\Attributes\Data6=255
		EndIf

	Case "Data7"
		If CurrentObject\Attributes\LogicType=110 ; Stinker NPC
			ObjectAdjusterData7\RandomMinDefault=0
			ObjectAdjusterData7\RandomMaxDefault=30
		Else
			ObjectAdjusterData7\RandomMinDefault=0
			ObjectAdjusterData7\RandomMaxDefault=10
		EndIf

		CurrentObject\Attributes\Data7=AdjustObjectAdjusterInt(ObjectAdjusterData7,CurrentObject\Attributes\Data7,SlowInt,FastInt,DelayTime)

		If CurrentObject\Attributes\LogicType=90 And CurrentObject\Attributes\LogicSubType=11 And CurrentObject\Attributes\Data0=1 ; NPC Modifier
			; turn

			If CurrentObject\Attributes\Data7=-2 CurrentObject\Attributes\Data7=25
			If CurrentObject\Attributes\Data7=26 CurrentObject\Attributes\Data7=-1
			If CurrentObject\Attributes\Data7=6 CurrentObject\Attributes\Data7=10
			If CurrentObject\Attributes\Data7=9 CurrentObject\Attributes\Data7=5
			If CurrentObject\Attributes\Data7=16 CurrentObject\Attributes\Data7=20
			If CurrentObject\Attributes\Data7=19 CurrentObject\Attributes\Data7=15

;			If CurrentObject\Attributes\Data7=-2 CurrentObject\Attributes\Data7=35
;			If CurrentObject\Attributes\Data7=36 CurrentObject\Attributes\Data7=-1
;
;			If CurrentObject\Attributes\Data7=6 CurrentObject\Attributes\Data7=10
;			If CurrentObject\Attributes\Data7=9 CurrentObject\Attributes\Data7=5
;			If CurrentObject\Attributes\Data7=16 CurrentObject\Attributes\Data7=20
;			If CurrentObject\Attributes\Data7=19 CurrentObject\Attributes\Data7=15
;			If CurrentObject\Attributes\Data7=26 CurrentObject\Attributes\Data7=30
;			If CurrentObject\Attributes\Data7=29 CurrentObject\Attributes\Data7=25

		EndIf

		If CurrentObject\Attributes\ModelName$="!GlowWorm"  Or CurrentObject\Attributes\ModelName$="!Zipper"
			If CurrentObject\Attributes\Data7>255 CurrentObject\Attributes\Data7=0
			If CurrentObject\Attributes\Data7<0 CurrentObject\Attributes\Data7=255

		EndIf

;		If CurrentObject\Attributes\LogicType=110 ; Stinker NPC
;
;			; Turn ; Stinker NPC Turn
;			If CurrentObject\Attributes\Data7=-1 CurrentObject\Attributes\Data7=35
;			If CurrentObject\Attributes\Data7=36 CurrentObject\Attributes\Data7=0
;
;			If CurrentObject\Attributes\Data7=6 CurrentObject\Attributes\Data7=10
;			If CurrentObject\Attributes\Data7=9 CurrentObject\Attributes\Data7=5
;			If CurrentObject\Attributes\Data7=16 CurrentObject\Attributes\Data7=20
;			If CurrentObject\Attributes\Data7=19 CurrentObject\Attributes\Data7=15
;			If CurrentObject\Attributes\Data7=26 CurrentObject\Attributes\Data7=30
;			If CurrentObject\Attributes\Data7=29 CurrentObject\Attributes\Data7=25
;
;		EndIf

		If CurrentObject\Attributes\LogicType=110 Or CurrentObject\Attributes\LogicType=390 ; Stinker NPC or Kaboom NPC
			; Turn
			If CurrentObject\Attributes\Data7=-1 CurrentObject\Attributes\Data7=25
			If CurrentObject\Attributes\Data7=26 CurrentObject\Attributes\Data7=0
			If CurrentObject\Attributes\Data7=6 CurrentObject\Attributes\Data7=10
			If CurrentObject\Attributes\Data7=9 CurrentObject\Attributes\Data7=5
			If CurrentObject\Attributes\Data7=16 CurrentObject\Attributes\Data7=20
			If CurrentObject\Attributes\Data7=19 CurrentObject\Attributes\Data7=15
		EndIf

	Case "Data8"
		;CurrentObject\Attributes\Data8=AdjustInt("Data8: ", CurrentObject\Attributes\Data8, 1, 10, 150)
		PrevValue=CurrentObject\Attributes\Data8
		CurrentObject\Attributes\Data8=AdjustObjectAdjusterInt(ObjectAdjusterData8,CurrentObject\Attributes\Data8,SlowInt,FastInt,DelayTime)
		NewValue=CurrentObject\Attributes\Data8

		If CurrentObject\Attributes\LogicType=90 Or CurrentObject\Attributes\LogicType=210 ; button or transporter
			; ActivateID (Pla is -2, so skip -1 to get there)
			If NewValue>PrevValue
				If CurrentObject\Attributes\Data8<0 Then CurrentObject\Attributes\Data8=0
			Else
				If CurrentObject\Attributes\Data8<0 Then CurrentObject\Attributes\Data8=-2
			EndIf
;			If LeftMouse=True
;				If CurrentObject\Attributes\Data8<0 Then CurrentObject\Attributes\Data8=0
;			ElseIf RightMouse=True
;				If CurrentObject\Attributes\Data8<0 Then CurrentObject\Attributes\Data8=-2
;			EndIf
		EndIf

		If CurrentObject\Attributes\LogicType=110 ; Stinker NPC
			; IdleAnim
			If CurrentObject\Attributes\Data8>10 CurrentObject\Attributes\Data8=0
			If CurrentObject\Attributes\Data8<0 CurrentObject\Attributes\Data8=10
		EndIf

		If CurrentObject\Attributes\LogicType=390 ; Kaboom NPC
			; IdleAnim
			If CurrentObject\Attributes\Data8>5 CurrentObject\Attributes\Data8=0
			If CurrentObject\Attributes\Data8<0 CurrentObject\Attributes\Data8=5
		EndIf

		If CurrentObject\Attributes\LogicType=400 ; Baby Boomer
			; Boom?
			If CurrentObject\Attributes\Data8>1 CurrentObject\Attributes\Data8=0
			If CurrentObject\Attributes\Data8<0 CurrentObject\Attributes\Data8=1
		EndIf

		If CurrentObject\Attributes\LogicType=120 ; Wee Stinker
			; Texture
			If CurrentObject\Attributes\Data8>2 CurrentObject\Attributes\Data8=0
			If CurrentObject\Attributes\Data8<0 CurrentObject\Attributes\Data8=2
		EndIf

	Case "Data9"
		;CurrentObject\Attributes\Data9=AdjustInt("Data9: ", CurrentObject\Attributes\Data9, 1, 10, 150)
		CurrentObject\Attributes\Data9=AdjustObjectAdjusterInt(ObjectAdjusterData9,CurrentObject\Attributes\Data9,SlowInt,FastInt,DelayTime)

		If CurrentObject\Attributes\ModelName$="!CustomModel" And CurrentObject\Attributes\LogicType=160
			; Deadly
			If CurrentObject\Attributes\Data9>1 CurrentObject\Attributes\Data9=0
			If CurrentObject\Attributes\Data9<0 CurrentObject\Attributes\Data9=1
		EndIf

		If CurrentObject\Attributes\LogicType=90 And CurrentObject\Attributes\LogicSubType=11 And CurrentObject\Attributes\Data0=1 ; NPC Change
			; anim
			If CurrentObject\Attributes\Data9<-1 CurrentObject\Attributes\Data9=10
			If CurrentObject\Attributes\Data9>10 CurrentObject\Attributes\Data9=-1
		EndIf

		If CurrentObject\Attributes\LogicType=45 Or CurrentObject\Attributes\LogicType=46 ; Conveyor
			If CurrentObject\Attributes\Data9<1 CurrentObject\Attributes\Data9=1
		EndIf

	Case "Talkable"
		CurrentObject\Attributes\Talkable=AdjustObjectAdjusterInt(ObjectAdjusterTalkable,CurrentObject\Attributes\Talkable,SlowInt,FastInt,DelayTime)

	Case "MovementSpeed"
		CurrentObject\Attributes\MovementSpeed=AdjustObjectAdjusterInt(ObjectAdjusterMovementSpeed,CurrentObject\Attributes\MovementSpeed,SlowInt,FastInt,DelayTime)

	Case "MovementType"
		CurrentObject\Attributes\MovementType=AdjustObjectAdjusterInt(ObjectAdjusterMovementType,CurrentObject\Attributes\MovementType,SlowInt,FastInt,DelayTime)

	Case "MovementTypeData"
		CurrentObject\Attributes\MovementTypeData=AdjustObjectAdjusterInt(ObjectAdjusterMovementTypeData,CurrentObject\Attributes\MovementTypeData,SlowInt,FastInt,DelayTime)

	Case "TileTypeCollision"
		CurrentObject\Attributes\TileTypeCollision=AdjustObjectAdjusterBits(ObjectAdjusterTileTypeCollision,CurrentObject\Attributes\TileTypeCollision,i,DelayTime)

	Case "ObjectTypeCollision"
		CurrentObject\Attributes\ObjectTypeCollision=AdjustObjectAdjusterBits(ObjectAdjusterObjectTypeCollision,CurrentObject\Attributes\ObjectTypeCollision,i,DelayTime)

	Case "ScaleAdjust"
		CurrentObject\Attributes\ScaleAdjust=AdjustObjectAdjusterFloat(ObjectAdjusterScaleAdjust,CurrentObject\Attributes\ScaleAdjust,SlowFloat#,FastFloat#,DelayTime)

	Case "Exclamation"
		CurrentObject\Attributes\Exclamation=AdjustObjectAdjusterInt(ObjectAdjusterExclamation,CurrentObject\Attributes\Exclamation,SlowInt,FastInt,DelayTime)

	Case "Linked"
		CurrentObject\Attributes\Linked=AdjustObjectAdjusterInt(ObjectAdjusterLinked,CurrentObject\Attributes\Linked,SlowInt,FastInt,DelayTime)
	Case "LinkBack"
		CurrentObject\Attributes\LinkBack=AdjustObjectAdjusterInt(ObjectAdjusterLinkBack,CurrentObject\Attributes\LinkBack,SlowInt,FastInt,DelayTime)

	Case "Parent"
		CurrentObject\Attributes\Parent=AdjustObjectAdjusterInt(ObjectAdjusterParent,CurrentObject\Attributes\Parent,SlowInt,FastInt,DelayTime)
	Case "Child"
		CurrentObject\Attributes\Child=AdjustObjectAdjusterInt(ObjectAdjusterChild,CurrentObject\Attributes\Child,SlowInt,FastInt,DelayTime)

	Case "DX"
		CurrentObject\Attributes\DX=AdjustObjectAdjusterFloat(ObjectAdjusterDX,CurrentObject\Attributes\DX,SlowFloat#,FastFloat#,DelayTime)
	Case "DY"
		CurrentObject\Attributes\DY=AdjustObjectAdjusterFloat(ObjectAdjusterDY,CurrentObject\Attributes\DY,SlowFloat#,FastFloat#,DelayTime)
	Case "DZ"
		CurrentObject\Attributes\DZ=AdjustObjectAdjusterFloat(ObjectAdjusterDZ,CurrentObject\Attributes\DZ,SlowFloat#,FastFloat#,DelayTime)

	Case "MoveXGoal"
		ObjectAdjusterMoveXGoal\RandomMaxDefault=LevelWidth-1
		CurrentObject\Attributes\MoveXGoal=AdjustObjectAdjusterInt(ObjectAdjusterMoveXGoal,CurrentObject\Attributes\MoveXGoal,SlowInt,FastInt,DelayTime)
	Case "MoveYGoal"
		ObjectAdjusterMoveYGoal\RandomMaxDefault=LevelHeight-1
		CurrentObject\Attributes\MoveYGoal=AdjustObjectAdjusterInt(ObjectAdjusterMoveYGoal,CurrentObject\Attributes\MoveYGoal,SlowInt,FastInt,DelayTime)

	Case "Data10"
		CurrentObject\Attributes\Data10=AdjustObjectAdjusterInt(ObjectAdjusterData10,CurrentObject\Attributes\Data10,SlowInt,FastInt,DelayTime)

	Case "Caged"
		CurrentObject\Attributes\Caged=AdjustObjectAdjusterInt(ObjectAdjusterCaged,CurrentObject\Attributes\Caged,SlowInt,FastInt,DelayTime)
	Case "Dead"
		CurrentObject\Attributes\Dead=AdjustObjectAdjusterInt(ObjectAdjusterDead,CurrentObject\Attributes\Dead,SlowInt,FastInt,DelayTime)
	Case "DeadTimer"
		CurrentObject\Attributes\DeadTimer=AdjustObjectAdjusterInt(ObjectAdjusterDeadTimer,CurrentObject\Attributes\DeadTimer,SlowInt,25,DelayTime)
	Case "MovementTimer"
		CurrentObject\Attributes\MovementTimer=AdjustObjectAdjusterInt(ObjectAdjusterMovementTimer,CurrentObject\Attributes\MovementTimer,SlowInt,25,DelayTime)

	Case "Flying"
		CurrentObject\Attributes\Flying=AdjustObjectAdjusterInt(ObjectAdjusterFlying,CurrentObject\Attributes\Flying,SlowInt,FastInt,DelayTime)

	Case "Indigo"
		CurrentObject\Attributes\Indigo=AdjustObjectAdjusterInt(ObjectAdjusterIndigo,CurrentObject\Attributes\Indigo,SlowInt,FastInt,DelayTime)

	Case "Speed"
		CurrentObject\Attributes\Speed=AdjustObjectAdjusterFloat(ObjectAdjusterSpeed,CurrentObject\Attributes\Speed,SlowFloat#,FastFloat#,DelayTime)
	Case "Radius"
		CurrentObject\Attributes\Radius=AdjustObjectAdjusterFloat(ObjectAdjusterRadius,CurrentObject\Attributes\Radius,SlowFloat#,FastFloat#,DelayTime)

	Case "Status"
		CurrentObject\Attributes\Status=AdjustObjectAdjusterInt(ObjectAdjusterStatus,CurrentObject\Attributes\Status,SlowInt,FastInt,DelayTime)

	End Select

	; avoid false positives from pressing enter
	If LeftMouse=True Or RightMouse=True Or MouseScroll<>0
		BuildCurrentObjectModel()
		CurrentObjectWasChanged()
	EndIf

End Function