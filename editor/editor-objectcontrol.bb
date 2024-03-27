Function PlayerTileX()

	Return BrushCursorX

End Function

Function PlayerTileY()

	; Make player-facing objects turn approximately south when cursor is not present.
	If BrushCursorY=BrushCursorInvalid
		Return 100000000
	Else
		Return BrushCursorY
	EndIf

End Function

Function PlayerX#()

	Return PlayerTileX()+0.5

End Function

Function PlayerY#()

	Return PlayerTileY()+0.5

End Function

Function ControlParticleEmitters(i)

	;If ObjectActive(i)=0 Then Return

	TheX#=SimulatedObjectXAdjust(i)+LevelObjects(i)\Position\TileX+0.5
	TheY#=SimulatedObjectYAdjust(i)+LevelObjects(i)\Position\TileY+0.5
	TheZ#=SimulatedObjectZAdjust(i)

	Select SimulatedObjectSubType(i)
	Case 1
		; steam
		If SimulatedObjectStatus(i)=0
			; not steaming - check if start
			If Rand(0,400)<=SimulatedObjectData(i,1)*2
				SimulatedObjectStatus(i)=1
			EndIf
		Else
			If Rand(0,200*SimulatedObjectData(i,1))<2

				SimulatedObjectStatus(i)=0

			EndIf
			Select SimulatedObjectData(i,2)
			Case 0
				If Rand(0,25)<SimulatedObjectData(i,1)*3-2 AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,0,.03,0,0,.01,0,0,0,100,3)
			Case 1
				If Rand(0,10)<SimulatedObjectData(i,1) AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,0,-.03,0,0,.01,0,0,0,100,3)
			Case 2
				If Rand(0,10)<SimulatedObjectData(i,1) AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,.03,0,0,0,.01,0,0,0,100,3)
			Case 3
				If Rand(0,10)<SimulatedObjectData(i,1) AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,-.03,0,0,0,.01,0,0,0,100,3)
			Case 4
				If Rand(0,10)<SimulatedObjectData(i,1) AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,0,0,.03,0,.01,0,0,0,100,3)
			Case 5
				If Rand(0,10)<SimulatedObjectData(i,1) AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,0,0,-.03,0,.01,0,0,0,100,3)
			End Select

		EndIf
	Case 2
		; splish
		If Rand (0,1000)<=SimulatedObjectData(i,1)*2
			AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.01,0,0,0,0,.01,0,0,0,100,4)
		EndIf

	Case 3
		; fountain
		Select SimulatedObjectData(i,2)
		Case 0
			If Rand(0,12)<SimulatedObjectData(i,1)*3-2 AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,Rnd(-.01,.01),Rnd(.05,.07),Rnd(-.01,.01),0,.001,0,-.001,0,100,3)
		Case 1
			If Rand(0,12)<SimulatedObjectData(i,1)*3-2 AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,Rnd(-.01,.01),-Rnd(0,.02),Rnd(-.01,.01),0,.001,0,-.001,0,100,3)
		Case 2
			If Rand(0,12)<SimulatedObjectData(i,1)*3-2 AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,Rnd(.05,.07),Rnd(.02,.01),Rnd(-.01,.01),0,.001,0,-.001,0,100,3)
		Case 3
			If Rand(0,12)<SimulatedObjectData(i,1)*3-2 AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,-Rnd(.05,.07),Rnd(.02,.01),Rnd(-.01,.01),0,.001,0,-.001,0,100,3)
		Case 4
			If Rand(0,12)<SimulatedObjectData(i,1)*3-2 AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,Rnd(-.01,.01),Rnd(.02,.01),Rnd(.05,.07),0,.001,0,-.001,0,100,3)
		Case 5
			If Rand(0,12)<SimulatedObjectData(i,1)*3-2 AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,Rnd(-.01,.01),Rnd(.02,.01),-Rnd(.05,.07),0,.001,0,-.001,0,100,3)
		End Select

	Case 4
		; sparks
		If Rand(0,1000)<SimulatedObjectData(i,1)*SimulatedObjectData(i,1)
			If SimulatedObjectData(i,3)>=1 And False ; Disabled
				SoundPitch SoundFX(16),Rand(24000,29000)
				PlaySoundFX(16,TheX#,TheY#)
			EndIf
			For j=0 To SimulatedObjectData(i,1)*30
				Select SimulatedObjectData(i,2)
				Case 0
					AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0.01,.2,Rnd(-.01,.01),Rnd(.09,.11),Rnd(-.01,.01),0,.0001,0,-.0015,0,50,3)
				Case 1
					AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0.01,.2,Rnd(-.01,.01),-Rnd(0,.02),Rnd(-.01,.01),0,.0001,0,-.0015,0,50,3)
				Case 2
					AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0.01,.2,Rnd(.01,.04),Rnd(.03,.01),Rnd(-.01,.01),0,.0001,0,-.0015,0,50,3)
				Case 3
					AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0.01,.2,-Rnd(.01,.04),Rnd(.03,.01),Rnd(-.01,.01),0,.0001,0,-.0015,0,50,3)
				Case 4
					AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0.01,.2,Rnd(-.01,.01),Rnd(.03,.01),Rnd(.01,.04),0,.0001,0,-.0015,0,50,3)
				Case 5
					AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0.01,.2,Rnd(-.01,.01),Rnd(.03,.01),-Rnd(.01,.04),0,.0001,0,-.0015,0,50,3)
				End Select
			Next
		EndIf
	Case 5
		; blinker
		If (SimulatedObjectData(i,4)=0 And Rand(0,200)<SimulatedObjectData(i,1)) Or (SimulatedObjectData(i,4)=1 And LevelTimer Mod (500-SimulatedObjectData(i,1)*100)=0)

			If SimulatedObjectData(i,3)>=1 And False ; Disabled
				If SimulatedObjectData(i,3)=1 ; quiet magic
					SoundPitch SoundFX(90),Rand(16000,20000)
					PlaySoundFX(90,TheX#,TheY#)
				EndIf
				If SimulatedObjectData(i,3)=2 ; loud mecha

					PlaySoundFX(35,TheX#,TheY#)
				EndIf

				If SimulatedObjectData(i,3)=3 ; variable gong
					SoundPitch SoundFX(13),Rand(24000,44000)
					PlaySoundFX(13,TheX#,TheY#)
				EndIf
				If SimulatedObjectData(i,3)=4 ; grow

					PlaySoundFX(92,TheX#,TheY#)
				EndIf

				If SimulatedObjectData(i,3)=5 ; floing

					PlaySoundFX(93,TheX#,TheY#)
				EndIf

				If SimulatedObjectData(i,3)=6 ; gem

					PlaySoundFX(11,TheX#,TheY#)
				EndIf
			EndIf

			AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0.01,.4,0,0,0,0,.0005,0,0,0,100,3)

		EndIf

	Case 6
		; circle
		If (SimulatedObjectData(i,4)=0 And Rand(0,200)<SimulatedObjectData(i,1)) Or (SimulatedObjectData(i,4)=1 And LevelTimer Mod (500-SimulatedObjectData(i,1)*100)=0)

			For j=0 To 44
				Select SimulatedObjectData(i,2)
				Case 0,1
					AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,.01*SimulatedObjectData(i,1)*Cos(j*8),0,.01*SimulatedObjectData(i,1)*Sin(j*8),0,.001,0,0,0,100,3)
				Case 2,3
					AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,0,.01*SimulatedObjectData(i,1)*Cos(j*8),.01*SimulatedObjectData(i,1)*Sin(j*8),0,.001,0,0,0,100,3)
				Case 4,5
					AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,.01*SimulatedObjectData(i,1)*Cos(j*8),.01*SimulatedObjectData(i,1)*Sin(j*8),0,0,.001,0,0,0,100,3)

				End Select
			Next
		EndIf

	Case 7
		; spiral
		Select SimulatedObjectData(i,2)
		Case 0
			AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,.02*SimulatedObjectData(i,1)*Cos((Leveltimer*SimulatedObjectData(i,1)) Mod 360),0,.02*SimulatedObjectData(i,1)*Sin((Leveltimer*SimulatedObjectData(i,1)) Mod 360),0,.001,0,0,0,100,3)
		Case 2
			AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,0,.02*SimulatedObjectData(i,1)*Cos((Leveltimer*SimulatedObjectData(i,1)) Mod 360),.02*SimulatedObjectData(i,1)*Sin((Leveltimer*SimulatedObjectData(i,1)) Mod 360),0,.001,0,0,0,100,3)
		Case 4
			AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,.02*SimulatedObjectData(i,1)*Cos((Leveltimer*SimulatedObjectData(i,1)) Mod 360),.02*SimulatedObjectData(i,1)*Sin((Leveltimer*SimulatedObjectData(i,1)) Mod 360),0,0,.001,0,0,0,100,3)

		Case 1
			AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,-.02*SimulatedObjectData(i,1)*Cos((Leveltimer*SimulatedObjectData(i,1)) Mod 360),0,.02*SimulatedObjectData(i,1)*Sin((Leveltimer*SimulatedObjectData(i,1)) Mod 360),0,.001,0,0,0,100,3)
		Case 3
			AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,0,-.02*SimulatedObjectData(i,1)*Cos((Leveltimer*SimulatedObjectData(i,1)) Mod 360),.02*SimulatedObjectData(i,1)*Sin((Leveltimer*SimulatedObjectData(i,1)) Mod 360),0,.001,0,0,0,100,3)
		Case 5
			AddParticle(SimulatedObjectData(i,0),TheX#,TheZ#,-TheY#,0,.2,-.02*SimulatedObjectData(i,1)*Cos((Leveltimer*SimulatedObjectData(i,1)) Mod 360),.02*SimulatedObjectData(i,1)*Sin((Leveltimer*SimulatedObjectData(i,1)) Mod 360),0,0,.001,0,0,0,100,3)

		End Select

	End Select

End Function

Function ControlWaterfall(i)

	If SimulatedObjectYawAdjust(i)=0
		k1=1
		k2=0
	EndIf
	If SimulatedObjectYawAdjust(i)=90
		k1=0
		k2=1
	EndIf
	If SimulatedObjectYawAdjust(i)=-90 Or SimulatedObjectYawAdjust(i)=270
		k1=0
		k2=-1
	EndIf

	If Rand(0,100)<10
		If SimulatedObjectData(i,0)=1
			AddParticle(1,SimulatedObjectX(i)+k1*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))+k2*Rnd(0.55,0.6),SimulatedObjectZAdjust(i),-SimulatedObjectY(i)+k2*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))-k1*Rnd(0.55,0.6),0,.11,k1*Rnd(-.005,0.005)+k2*Rnd(0,.005),Rnd(0.01,0.03),-k1*Rnd(0,.001)+k2*Rnd(-.005,0.005),0,0,0,-0.0004,0,100,3)
		Else
			AddParticle(5,SimulatedObjectX(i)+k1*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))+k2*Rnd(0.55,0.6),SimulatedObjectZAdjust(i),-SimulatedObjectY(i)+k2*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))-k1*Rnd(0.55,0.6),0,.11,k1*Rnd(-.005,0.005)+k2*Rnd(0,.005),Rnd(0.01,0.03),-k1*Rnd(0,.001)+k2*Rnd(-.005,0.005),0,0,0,-0.0004,0,100,3)
		EndIf
	EndIf

	If Rand(0,100)<3
		If SimulatedObjectData(i,0)=0
			AddParticle(6,SimulatedObjectX(i)+k1*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))+k2*Rnd(0.65,0.7),Rnd(SimulatedObjectZAdjust(i),SimulatedObjectZAdjust(i)+SimulatedObjectZScale(i)/2.0),-SimulatedObjectY(i)+k2*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))-k1*0.6,0,.5,k2*Rnd(0,0.005),Rnd(0.01,0.02),0,0,.01,0,0,0,100,3)
		Else If SimulatedObjectData(i,0)=1
			AddParticle(24,SimulatedObjectX(i)+k1*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))+k2*Rnd(0.65,0.7),Rnd(SimulatedObjectZAdjust(i),SimulatedObjectZAdjust(i)+SimulatedObjectZScale(i)/2.0),-SimulatedObjectY(i)+k2*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))-k1*0.6,0,.5,k2*Rnd(0,0.005),Rnd(0.01,0.02),0,0,.01,0,0,0,100,3)
		Else
			AddParticle(27,SimulatedObjectX(i)+k1*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))+k2*Rnd(0.65,0.7),Rnd(SimulatedObjectZAdjust(i),SimulatedObjectZAdjust(i)+SimulatedObjectZScale(i)/2.0),-SimulatedObjectY(i)+k2*Rnd(-.5*SimulatedObjectXScale(i),.5*SimulatedObjectXScale(i))-k1*0.6,0,.5,k2*Rnd(0,0.005),Rnd(0.01,0.02),0,0,.01,0,0,0,100,3)
		EndIf
	EndIf
	If Rand(0,100)<10
		If SimulatedObjectData(i,0)=1
			AddParticle(32,SimulatedObjectX(i)+k1*Rnd(-.35*SimulatedObjectXScale(i),.35*SimulatedObjectXScale(i))+k2*0.5,(-.2*SimulatedObjectZScale(i))+SimulatedObjectZAdjust(i),-SimulatedObjectY(i)+k2*Rnd(-.35*SimulatedObjectXScale(i),.35*SimulatedObjectXScale(i))-k1*0.5,0,.1,0,0,0,0,.012,0,0,0,100,4)
		Else
			AddParticle(4,SimulatedObjectX(i)+k1*Rnd(-.35*SimulatedObjectXScale(i),.35*SimulatedObjectXScale(i))+k2*0.5,(-.2*SimulatedObjectZScale(i))+SimulatedObjectZAdjust(i),-SimulatedObjectY(i)+k2*Rnd(-.35*SimulatedObjectXScale(i),.35*SimulatedObjectXScale(i))-k1*0.5,0,.2,0,0,0,0,.012,0,0,0,100,4)
		EndIf
	EndIf

End Function

Function ControlVoid(i)

	Obj.GameObject=LevelObjects(i)

	If SimulatedObjectData(i,0)=0
		SimulatedObjectData(i,0)=Rand(1,360)
	EndIf

	SimulatedObjectZ(i)=-.2

	PositionTexture voidtexture,((leveltimer/10) Mod 100)/100.0,((leveltimer/10) Mod 100)/100.0

	SimulatedObjectXScale(i)=(.8+.4*Sin((leveltimer+SimulatedobjectData(i,0)) Mod 360))*(1.0+Float(SimulatedObjectData(i,2)))
	SimulatedObjectYScale(i)=(.8+.4*Sin((leveltimer+SimulatedobjectData(i,0)) Mod 360))*(1.0+Float(SimulatedObjectData(i,2)))

	SimulatedObjectZScale(i)=(1.3+.6*Sin((leveltimer*2+SimulatedobjectData(i,0)) Mod 360));*(1.0+Float(ObjectData(i,2)))

	TurnEntity Obj\Model\Entity,0,.1,0

	If Obj\Attributes\ModelName$="!Void"
		surface=GetSurface(Obj\Model\Entity,1)
		For i=0 To 17
			VertexCoords surface,i*2,VertexX(surface,i*2),(1+.6*Sin(i*80+((LevelTimer*4) Mod 360))),VertexZ(surface,i*2)
		Next
		For i=18 To 35
			VertexCoords surface,i*2,VertexX(surface,i*2),(.5+.4*Sin(i*160+((LevelTimer*4) Mod 360))),VertexZ(surface,i*2)
		Next
	EndIf

End Function

Function ControlTeleporter(i)

	If Rand(0,100)<5 And (SimulatedObjectActive(i)>0 Or SimulationLevel<2)
		a=Rand(0,360)
		b#=Rnd(0.002,0.006)
		AddParticle(23,SimulatedObjectX(i)+0.5*Sin(a),0,-SimulatedObjectY(i)-0.5*Cos(a),0,.2,b*Sin(a),0.015,-b*Cos(a),1,0,0,0,0,150,3)
	EndIf

	MyId=CalculateEffectiveID(LevelObjects(i)\Attributes)

	SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+.5
	If MyId Mod 5<3
		; standard effect
		;ScaleEntity ObjectEntity(i),1,ObjectActive(i)/1001.0,1
	Else If MyId Mod 5=3
		; unstable effect
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+4.5
		SimulatedObjectXScale(i)=0.6+0.4*Sin ((LevelTimer/7) Mod 360)
		;SimulatedObjectZScale(i)=ObjectActive(i)/1001.0
		SimulatedObjectYScale(i)=0.6+0.4*Cos ((LevelTimer/2) Mod 360)
		;ScaleEntity ObjectEntity(i),0.6+0.4*Sin ((LevelTimer/7) Mod 360),,
	Else
		; pulsating effect
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+9.5
		SimulatedObjectXScale(i)=0.6+0.4*Sin ((LevelTimer) Mod 360)
		;SimulatedObjectZScale(i)=ObjectActive(i)/1001.0
		SimulatedObjectYScale(i)=0.6+0.4*Sin ((LevelTimer) Mod 360)

		;ScaleEntity ObjectEntity(i),,ObjectActive(i)/1001.0,)
	EndIf

	;SimulateObjectRotation(i)
	;SimulateObjectScale(i)

End Function

Function ControlObstacle(i)

	ModelName$=LevelObjects(i)\Attributes\ModelName$

	; (no control, but used to adjust leveltilelogic)
	If ModelName$="!Obstacle03" ; volcano
		If Rand(0,40)=0
			AddParticle(Rand(24,26),SimulatedObjectX(i)+Rnd(-.7,.7),1.8+SimulatedObjectZAdjust(i),-SimulatedObjectY(i)+Rnd(-.9,.7),0,.2,0,Rnd(0.01,0.03),0,0,.03,0,0,0,100,3)
		EndIf
		If Rand(0,10)=0
			If Rand(0,5)=0
				part22=1
			Else
				part22=0
			EndIf
			AddParticle(part22,SimulatedObjectX(i)+Rnd(-.3,.3),1.5+SimulatedObjectZAdjust(i),-SimulatedObjectY(i)+Rnd(-.5,.3),0,.6,0,Rnd(0.01,0.03),0,0,.01,0,0,0,100,3)
		EndIf
	Else If ModelName$="!Obstacle04" ; acid pool
		If Rand(0,100)=0
			AddParticle(27,SimulatedObjectX(i)+Rnd(-.5,.5),1+SimulatedObjectZAdjust(i),-SimulatedObjectY(i)+Rnd(-.7,.5),0,.11,0,Rnd(0.01,0.03),0,0,.01,0,0,0,100,3)
		EndIf
		If Rand(0,100)=0
			AddParticle(35,SimulatedObjectX(i)+Rnd(-.3,.6),2.0+SimulatedObjectZAdjust(i),-SimulatedObjectY(i)+Rnd(-.6,.3),0,.04,0,0,0,0,.001,0,0,0,100,4)
		EndIf

	Else If ModelName$="!Obstacle45" ; waterwheel
		If SimulatedObjectYawAdjust(i)=0
			SimulatedObjectRoll(i)=SimulatedObjectRoll(i)+2
		EndIf
		If SimulatedObjectYawAdjust(i)=180
			SimulatedObjectRoll(i)=SimulatedObjectRoll(i)-2
		EndIf
		If SimulatedObjectYawAdjust(i)=90
			SimulatedObjectPitch(i)=SimulatedObjectPitch(i)+2
		EndIf
		If SimulatedObjectYawAdjust(i)=270
			SimulatedObjectPitch(i)=SimulatedObjectPitch(i)-2
		EndIf

	Else If ModelName$="!Obstacle48" ; UFO - by mistake in here
		If SimulatedObjectData(i,0)=0
			SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+1
		EndIf
	Else If ModelName$="!Crystal" ; UFO - by mistake in here
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+1

	EndIf
	If ModelName$="!CustomModel"	; Custom Model
		ControlCustomModel(i)
	EndIf

End Function

Function ControlCustomModel(i)

	Obj.GameObject=LevelObjects(i)

;	If ObjectOldX(i)=-999;0 And ObjectOldY(i)=0 And ObjectOldZ(i)=0
;		ObjectOldX(i)=ObjectXAdjust(i)
;		ObjectOldY(i)=ObjectYAdjust(i)
;		ObjectOldZ(i)=ObjectZAdjust(i)
;	EndIf

	;ObjectScaleAdjust(i)*(1.5+0.8*Sin((leveltimer+ObjectData(i,7)+30) Mod 360))

	SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+SimulatedObjectData(i,0)
	If SimulatedObjectYaw(i)>360 Then SimulatedObjectYaw(i)=SimulatedObjectYaw(i)-360
	If SimulatedObjectYaw(i)<0 Then SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+360

	SimulatedObjectPitch(i)=SimulatedObjectPitch(i)+SimulatedObjectData(i,1)
	If SimulatedObjectPitch(i)>360 Then SimulatedObjectPitch(i)=SimulatedObjectPitch(i)-360
	If SimulatedObjectPitch(i)<0 Then SimulatedObjectPitch(i)=SimulatedObjectPitch(i)+360

	SimulatedObjectroll(i)=SimulatedObjectroll(i)+SimulatedObjectData(i,2)
	If SimulatedObjectroll(i)>360 Then SimulatedObjectroll(i)=SimulatedObjectroll(i)-360
	If SimulatedObjectroll(i)<0 Then SimulatedObjectroll(i)=SimulatedObjectroll(i)+360

	BaseX#=Obj\Attributes\XAdjust
	BaseY#=Obj\Attributes\YAdjust
	BaseZ#=Obj\Attributes\ZAdjust

	If SimulatedObjectData(i,3)>0
		; Technically these ObjectX/Y/ZAdjust instances should be OldX/Y/Z. But no one's crazy enough to edit OldX/Y/Z directly, right?
		SimulatedObjectXAdjust(i)=BaseX#+Float(SimulatedObjectData(i,3))*Sin((leveltimer Mod 36000)*Float(SimulatedObjectData(i,6)/100.0))
	Else
		SimulatedObjectXAdjust(i)=BaseX#+Float(SimulatedObjectData(i,3))*Cos((leveltimer Mod 36000)*Float(SimulatedObjectData(i,6)/100.0))
	EndIf
	If SimulatedObjectData(i,4)>0
		SimulatedObjectYAdjust(i)=BaseY#+Float(SimulatedObjectData(i,4))*Sin((leveltimer Mod 36000)*Float(SimulatedObjectData(i,7)/100.0))
	Else
		SimulatedObjectYAdjust(i)=BaseY#+Float(SimulatedObjectData(i,4))*Cos((leveltimer Mod 36000)*Float(SimulatedObjectData(i,7)/100.0))
	EndIf
	If SimulatedObjectData(i,5)>0
		SimulatedObjectZAdjust(i)=BaseZ#+Float(SimulatedObjectData(i,5))*Sin((leveltimer Mod 36000)*Float(SimulatedObjectData(i,8)/100.0))
	Else
		SimulatedObjectZAdjust(i)=BaseZ#+Float(SimulatedObjectData(i,5))*Cos((leveltimer Mod 36000)*Float(SimulatedObjectData(i,8)/100.0))
	EndIf

End Function

Function ControlGoldStar(i)

	Obj.GameObject=LevelObjects(i)

	SimulatedObjectZ(i)=.8
;	If AdventureCurrentStatus=0
;		ObjectXScale(i)=.5
;		ObjectYScale(i)=.5
;		ObjectZScale(i)=.5
;		ObjectZ(i)=.4
;	EndIf

	SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+2

	If SimulatedObjectActive(i)<1001 And (SimulatedObjectActive(i)<>0 Or SimulationLevel>=2) Then Return

	a=Rand(0,300)
	If a<50
		AddParticle(19,Obj\Position\TileX+0.5,.7+SimulatedObjectZAdjust(i),-Obj\Position\TileY-0.5,Rand(0,360),0.16,Rnd(-.015,.015),0.03,Rnd(-.015,.015),0,0.001,0,-.00025,0,100,3)
	EndIf

End Function

Function ControlGoldCoin(i)

	If SimulatedObjectActive(i)<1001 And SimulatedObjectActive(i)>0
		Pos.GameObjectPosition=LevelObjects(i)\Position

		; picked up animation
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+10

		If SimulatedObjectActive(i)>600
			SimulatedObjectZ(i)=.2+Float(1000-SimulatedObjectActive(i))/400.0
		Else
			SimulatedObjectZ(i)=1.2
		EndIf
		If SimulatedObjectActive(i)=400
			; Little Spark
			For j=1 To 20
				AddParticle(19,Pos\TileX+0.5,1.6,-Pos\TileY-0.5,Rand(0,360),0.15,Rnd(-.035,.035),Rnd(-.015,.015),Rnd(-.035,.035),0,0,0,0,0,50,3)
			Next
		EndIf
;		If ObjectActive(i)<600
;			ObjectScaleXAdjust(i)=Float(ObjectActive(i))/600.0
;			ObjectScaleYAdjust(i)=Float(ObjectActive(i))/600.0
;			ObjectScaleZAdjust(i)=Float(ObjectActive(i))/600.0
;
;		EndIf
	Else
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+3
		SimulatedObjectZ(i)=0
	EndIf

End Function

Function ControlGem(i)

	Obj.GameObject=LevelObjects(i)
	Pos.GameObjectPosition=Obj\Position

	;If ObjectActive(i) Mod 2=1 Then ShowEntity ObjectEntity(i) ; What did MS mean by this?

	If SimulatedObjectActive(i)<1001 And SimulatedObjectActive(i)>0
		; picked up animation
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+10

		If SimulatedObjectActive(i)>600
			SimulatedObjectZ(i)=.6+Float(1000-SimulatedObjectActive(i))/400.0
		Else
			SimulatedObjectZ(i)=1.6
		EndIf
		If SimulatedObjectActive(i)=400

			; Little Spark
			For j=1 To 20
				AddParticle(19,Pos\TileX+0.5,1.6,-Pos\TileY-0.5,Rand(0,360),0.15,Rnd(-.035,.035),Rnd(-.015,.015),Rnd(-.035,.035),0,0,0,0,0,50,3)
			Next
			If WaEpisode=1 And (adventurecurrentnumber>=200 And adventurecurrentnumber<=203)
				; not in pacman level or WA1

			Else If Obj\Attributes\ID=-1
				If SimulatedObjectData(i,0)=0 Then AddParticle(14,Pos\TileX+0.5,1.6,-Pos\TileY-0.5,0,1,0,0.01,0,0,.01,0,0,0,50,3)
				If SimulatedObjectData(i,0)=1 Then AddParticle(15,Pos\TileX+0.5,1.6,-Pos\TileY-0.5,0,1,0,0.01,0,0,.01,0,0,0,50,3)
			EndIf
		EndIf
		If SimulatedObjectActive(i)<600
			SimulatedObjectScaleXAdjust(i)=Float(SimulatedObjectActive(i))/600.0
			SimulatedObjectScaleYAdjust(i)=Float(SimulatedObjectActive(i))/600.0
			SimulatedObjectScaleZAdjust(i)=Float(SimulatedObjectActive(i))/600.0
		EndIf

	Else
		If SimulatedObjectData(i,0)=2
			If Rand(0,10)<3

				AddParticle(16+SimulatedObjectData(i,1)+Rand(0,1)*8,Pos\TileX+.5,Rnd(0,1),-Pos\TileY-.5,0,.01,Rnd(-.01,.01),Rnd(-.01,.01),Rnd(0,.02),Rnd(-4,4),.01,0,0,0,70,3)
			EndIf
		EndIf

		If SimulatedObjectYaw(i)=0 And SimulatedObjectData(i,0)<>1
			SimulatedObjectRoll(i)=Rand(-10,10)
			SimulatedObjectYaw(i)=Rand(1,180)

		EndIf
		If SimulatedObjectData(i,0)=0 Or SimulatedObjectData(i,0)=2 Then SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+Rnd(1.8,2.2)
		If SimulatedObjectData(i,0)=1 Then SimulatedObjectPitch(i)=SimulatedObjectPitch(i)+Rnd(2,3)+(i Mod 3)/3.0
		SimulatedObjectZ(i)=.4
	EndIf

End Function

Function ControlKey(i)

	If SimulatedObjectActive(i)<1001 And SimulatedObjectActive(i)>0

		; picked up animation

		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+10

		If SimulatedObjectActive(i)>600
			SimulatedObjectZ(i)=.6+2*Float(1000-SimulatedObjectActive(i))/400.0
		Else
			SimulatedObjectZ(i)=2.6
		EndIf
		If SimulatedObjectActive(i)=400
			Pos.GameObjectPosition=LevelObjects(i)\Position

			; Little Spark
			For j=1 To 60
				AddParticle(Rnd(16,23),Pos\TileX+0.5,2.6,-Pos\TileY-0.5,Rand(0,360),0.2,Rnd(-.035,.035),Rnd(-.015,.015),Rnd(-.035,.035),0,0,0,0,0,50,3)
			Next
		EndIf
		If SimulatedObjectActive(i)<600
			SimulatedObjectScaleXAdjust(i)=Float(SimulatedObjectActive(i))/600.0
			SimulatedObjectScaleYAdjust(i)=Float(SimulatedObjectActive(i))/600.0
			SimulatedObjectScaleZAdjust(i)=Float(SimulatedObjectActive(i))/600.0
		EndIf

	Else
		Obj.GameObject=LevelObjects(i)

	;	ObjectYaw(i)=ObjectYaw(i)+2
		If Obj\Attributes\ModelName$="!KeyCard"
			SimulatedObjectYaw(i)=((leveltimer) Mod 90)*4
		Else
			SimulatedObjectRoll(i)=30*Sin((leveltimer) Mod 360)
		EndIf
		SimulatedObjectZ(i)=.4
	EndIf

End Function

Function ControlCustomItem(i)

	If SimulatedObjectActive(i)<1001 And SimulatedObjectActive(i)>0
		; picked up animation
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+10

		If SimulatedObjectActive(i)>600
			SimulatedObjectZ(i)=.6+2*Float(1000-SimulatedObjectActive(i))/400.0
		Else
			SimulatedObjectZ(i)=2.6
		EndIf
		If SimulatedObjectActive(i)=400
			Pos.GameObjectPosition=LevelObjects(i)\Position

			; Little Spark
			For j=1 To 60
				AddParticle(Rnd(16,23),Pos\TileX+0.5,2.6,-Pos\TileY-0.5,Rand(0,360),0.2,Rnd(-.035,.035),Rnd(-.015,.015),Rnd(-.035,.035),0,0,0,0,0,50,3)
			Next
		EndIf
;		If ObjectActive(i)<600
;			ObjectScaleXAdjust(i)=Float(ObjectActive(i))/600.0
;			ObjectScaleYAdjust(i)=Float(ObjectActive(i))/600.0
;			ObjectScaleZAdjust(i)=Float(ObjectActive(i))/600.0
;
;		EndIf

	Else

		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+Cos(Leveltimer Mod 360)

		SimulatedObjectZ(i)=.5

	EndIf

	;SimulateObjectPosition(i)
	;SimulateObjectRotation(i)

End Function

Function ControlSigns(i)

	Select SimulatedObjectData(i,2)

	Case 0
		; nuthin'
	Case 1
		; Sway
		SimulatedObjectPitch(i)=5*Sin((leveltimer*1.5) Mod 360)
		SimulatedObjectYaw(i)=5*Sin((leveltimer/2) Mod 360)
		SimulatedObjectRoll(i)=5*Sin(leveltimer Mod 360)

	Case 2
		; Bounce
		SimulatedObjectScaleZAdjust(i)=1.0+0.15*Sin((Leveltimer*4) Mod 360)
		SimulatedObjectRoll(i)=5*Sin((leveltimer*2) Mod 360)
	Case 3
		; turn
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+3
	End Select

End Function

Function ControlRetroRainbowCoin(i)

	If SimulatedObjectActive(i)<1001 And SimulatedObjectActive(i)>0
		; picked up animation
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+10

		If SimulatedObjectActive(i)>600
			SimulatedObjectZ(i)=1.2+Float(1000-SimulatedObjectActive(i))/400.0
		Else
			SimulatedObjectZ(i)=2.2
		EndIf
		If SimulatedObjectActive(i)=400
			Pos.GameObjectPosition=LevelObjects(i)\Position

			; Little Spark
			For j=1 To 20
				AddParticle(19,Pos\TileX+0.5,2.6,-Pos\TileY-0.5,Rand(0,360),0.15,Rnd(-.035,.035),Rnd(-.015,.015),Rnd(-.035,.035),0,0,0,0,0,50,3)
			Next
		EndIf
		If SimulatedObjectActive(i)<600
			SimulatedObjectScaleXAdjust(i)=Float(SimulatedObjectActive(i))/600.0
			SimulatedObjectScaleYAdjust(i)=Float(SimulatedObjectActive(i))/600.0
			SimulatedObjectScaleZAdjust(i)=Float(SimulatedObjectActive(i))/600.0
		EndIf

	Else
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+3
		SimulatedObjectZ(i)=0
	EndIf

End Function

Function ControlWisp(i)

	Obj.GameObject=LevelObjects(i)
	EntityFX Obj\Model\Entity,1

	If Leveltimer Mod 360 =0
		a=Rand(0,100)
		If a<60
			SimulatedObjectStatus(i)=0
		Else If a<80

			SimulatedObjectStatus(i)=1
		Else If a<90

			Simulatedobjectstatus(i)=2
		Else

			Simulatedobjectstatus(i)=3
		EndIf

	EndIf
	If SimulatedObjectStatus(i)=0
		SimulatedObjectZ(i)=0.6+.1*Sin(leveltimer Mod 360)
		SimulatedObjectYaw(i)=30*Sin((leveltimer*4) Mod 360)
		SimulatedObjectRoll(i)=20*Sin((leveltimer*2) Mod 360)
	Else If SimulatedObjectStatus(i)=1

		SimulatedObjectZ(i)=0.6+.1*Sin(leveltimer Mod 360)
		SimulatedObjectYaw(i)=180*Sin((leveltimer) Mod 360)
		SimulatedObjectRoll(i)=20*Sin((leveltimer*2) Mod 360)
	Else If SimulatedObjectStatus(i)=2

		SimulatedObjectZ(i)=0.6+.1*Sin(leveltimer Mod 360)
		SimulatedObjectYaw(i)=360*Sin((leveltimer/2) Mod 360)
		SimulatedObjectRoll(i)=180*Sin((leveltimer*2) Mod 360)
	Else If SimulatedObjectStatus(i)=3
		SimulatedObjectZ(i)=0.6+.4*Sin(leveltimer Mod 180)
		SimulatedObjectYaw(i)=60*Sin((leveltimer*4) Mod 360)
		SimulatedObjectRoll(i)=20*Sin((leveltimer*2) Mod 360)

	EndIf
	If Rand(0,100)<3 And SimulatedObjectActive(i)=1001
		Pos.GameObjectPosition=Obj\Position
		AddParticle(Rand(16,23),Pos\TileX+0.5,.7,-Pos\TileY-0.5,Rand(0,360),0.16,Rnd(-.015,.015),0.03,Rnd(-.015,.015),0,0.001,0,-.00025,0,100,3)
	EndIf

End Function

Function ControlRetroZbotUfo(i)

	Attributes.GameObjectAttributes=LevelObjects(i)\Attributes
	If Attributes\LogicType<>423 And Attributes\LogicType<>430
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+2
	EndIf

	;SimulateObjectRotation(i)

End Function

Function ControlRetroLaserGate(i)

	If SimulatedObjectYawAdjust(i)=0 Or SimulatedObjectYawAdjust(i)=180
		SimulatedObjectPitch(i)=(SimulatedObjectPitch(i)+2) Mod 360
	Else
		SimulatedObjectRoll(i)=(SimulatedObjectRoll(i)+2) Mod 360
	EndIf

	Obj.GameObject=LevelObjects(i)
	; This behavior is OpenWA-exclusive.
	EntityAlpha Obj\Model\Entity,0.5

End Function

Function ControlTentacle(i)

	If SimulatedObjectData(i,0)=0 Then SimulatedObjectData(i,0)=Rand(-10,10)
	SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+Float(SimulatedObjectData(i,0))/10.0

	;SimulateObjectRotation(i)

End Function

Function ControlRainbowBubble(i)

	Obj.GameObject=LevelObjects(i)

	If SimulatedObjectStatus(i)=0
		SimulatedObjectStatus(i)=1
		SimulatedObjectData(i,2)=Rand(0,360)
	EndIf

	EntityAlpha Obj\Model\Entity,.8
	EntityBlend Obj\Model\Entity,3

	l=leveltimer/4+SimulatedObjectData(i,2)

	SimulatedObjectXScale(i)=0.5-0.1*Sin((leveltimer + SimulatedObjectData(i,2)) Mod 360)

	SimulatedObjectYScale(i)=0.5-0.1*Sin((leveltimer + SimulatedObjectData(i,2)) Mod 360)

	SimulatedObjectZScale(i)=0.6+0.2*Sin((leveltimer + SimulatedObjectData(i,2)) Mod 360)

	SimulatedObjectPitch(i)=(SimulatedObjectPitch(i)+1) Mod 360
	SimulatedObjectYaw(i)=360*Sin(l Mod 360)
	SimulatedObjectRoll(i)=180*Cos(l Mod 360)

	SimulatedObjectZ(i)=0.5+0.3*Abs(Sin((leveltimer + SimulatedObjectData(i,2)) Mod 360))

	;SimulateObjectPosition(i)
	;SimulateObjectRotation(i)
	;SimulateObjectScale(i)

End Function

Function ControlBowler(i)

	Direction=SimulatedObjectData(i,0)
	If SimulatedObjectData(i,1)<>2
		Direction=Direction*2
	EndIf
	SimulatedObjectYawAdjust(i)=(-45*Direction +3600) Mod 360

	SimulatedObjectPitch2(i)=(SimulatedObjectPitch2(i)+Rnd(3,5)) Mod 360
	SimulatedObjectZ(i)=.4+.4*Sin((Leveltimer*4) Mod 180)

	;SimulateObjectPosition(i)
	;SimulateObjectRotation(i)

End Function

Function ControlMothership(i)

	SimulatedObjectYaw(i)=((SimulatedObjectYaw(i)+.3) Mod 360)

	If SimulatedObjectMovementSpeed(i)<>10 ; first time
		SimulatedObjectData(i,1)=-1
		SimulatedObjectYawAdjust(i)=0
		SimulatedObjectMovementSpeed(i)=10
		SimulatedObjectTileTypeCollision(i)=0
		SimulatedObjectZ(i)=4

		;CreateShadow(i,ObjectScaleAdjust(i)*5)
	EndIf

	If SimulatedObjectStatus(i)<0

		SimulatedObjectRoll(i)=((SimulatedObjectRoll(i)+.3) Mod 360)

		SimulatedObjectPitch(i)=((SimulatedObjectPitch(i)-.1) Mod 360)

		AddParticle(Rand(0,3),SimulatedObjectX(i)+Rnd(-.1,.1),SimulatedObjectZ(i)+Rnd(-.1,.1),-SimulatedObjectY(i)+Rnd(-.1,.1),0,Rnd(0.1,.5),Rnd(-.1,.1),Rnd(-.01,.01),Rnd(-.1,.1),3,.02,0,0,0,125,3)

;		If SimulatedObjectStatus(i) Mod 30 = 0
;			PlaySoundFX(96,SimulatedObjectX(i),SimulatedObjectY(i))
;		EndIf

		SimulatedObjectStatus(i)=SimulatedObjectStatus(i)-1
		;If ObjectStatus(i)=-200
		;	destroyobject(i,0)
		;	NofZBotsInAdventure=NofZBotsInAdventure-1
		;EndIf

	Else
		SimulatedObjectSubType(i)=SimulatedObjectSubType(i)+1
		If SimulatedObjectSubType(i)>100
			SimulatedObjectSubType(i)=0
;			PlaySoundFX(95,SimulatedObjectX(i),SimulatedObjectY(i))
		EndIf
	EndIf

End Function

Function ControlRubberducky(i)

	If SimulatedObjectTileTypeCollision(i)=0
		SimulatedObjectTileTypeCollision(i)=-1
		SimulatedObjectData(i,1)=Rand(1,3)
		SimulatedObjectData(i,2)=Rand(0,360)
	EndIf

	SimulatedObjectroll(i)=1*SimulatedObjectData(i,1)*Sin((LevelTimer+SimulatedObjectData(i,2)) Mod 360)
	SimulatedObjectpitch(i)=2*SimulatedObjectData(i,1)*Cos((LevelTimer*3+SimulatedObjectData(i,2))  Mod 360)

	If SimulatedObjectData(i,0)<>1
		TurnObjectTowardDirection(i,PlayerX()-SimulatedObjectX(i),PlayerY()-SimulatedObjectY(i),2,0)
	EndIf

End Function

Function ControlGloveCharge(i)

	Obj.GameObject=LevelObjects(i)
	Pos.GameObjectPosition=Obj\Position

	SubType=Obj\Attributes\LogicSubType

	SimulatedObjectZ(i)=0.04

	myparticle=16+SimulatedObjectData(i,0)

	SimulatedObjectData(i,3)=1

	j2=Rand(0,359)
	If SubType=1 ; one time charge
		If leveltimer Mod 5 = 0
			AddParticle(myparticle,Pos\TileX+.5+.1*Sin(j2*3),0,-Pos\TileY-.5-.1*Cos(j2*3),0,.2,0,.03,0,4,0,0,0,0,50,3)
		EndIf
	Else If SubType=0; multi-charge
		If leveltimer Mod 2 = 0
			AddParticle(myparticle,Pos\TileX+.5+.3*Sin(j2*3),0,-Pos\TileY-.5-.3*Cos(j2*3),0,.3,0,.04,0,4,0,0,0,0,50,3)
		EndIf
	EndIf

End Function

Function ControlWindmillRotor(i)

	If SimulatedObjectYawAdjust(i)=0 Or SimulatedObjectYawAdjust(i)=180
		SimulatedObjectRoll(i)=SimulatedObjectRoll(i)+1
	Else
		SimulatedObjectPitch(i)=SimulatedObjectPitch(i)+1
	EndIf

	;SimulatedObjectZ(i)=5.65
	Obj.GameObject=LevelObjects(i)
	Obj\Attributes\ZAdjust=5.65

End Function

Function ControlIceFloat(i)
	SimulatedObjectPitch(i)=2*SimulatedObjectData(i,2)*Sin((LevelTimer + SimulatedObjectData(i,1)) Mod 360)
	SimulatedObjectRoll(i)=3*SimulatedObjectData(i,3)*Cos((LevelTimer+ SimulatedObjectData(i,1))  Mod 360)

	;SimulateObjectRotation(i)

End Function

Function ControlPlantFloat(i)

	Obj.GameObject=LevelObjects(i)

	If SimulatedObjectData(i,2)=0 Then SimulatedObjectData(i,2)=Rand(1,360)

	l=leveltimer+SimulatedObjectData(i,2)
	EntityColor Obj\Model\Entity,128+120*Cos(l Mod 360),128+120*Sin(l Mod 360),200+50*Cos(l Mod 360)

	;ObjectPitch(i)=4*ObjectData(i,2)*Sin((LevelTimer + ObjectData(i,1)) Mod 360)
	;Objectroll(i)=6*ObjectData(i,3)*Cos((LevelTimer+ ObjectData(i,1))  Mod 360)
	SimulatedObjectYaw(i)=leveltimer Mod 360

	;SimulateObjectRotation(i)

End Function

Function ControlBurstFlower(i)

	Obj.GameObject=LevelObjects(i)
	Pos.GameObjectPosition=Obj\Position

	If SimulatedObjectTileTypeCollision(i)=0
		SimulatedObjectTileTypeCollision(i)=1
		SimulatedObjectData(i,0)=Rand(0,360)
	EndIf

	SimulatedObjectData(i,0)=(SimulatedObjectData(i,0)+1) Mod 720
	SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+.5*Sin(SimulatedObjectData(i,0)/2)
	SimulatedObjectXScale(i)=0.3+0.02*Cos(SimulatedObjectData(i,0)*2)
	SimulatedObjectYScale(i)=0.3+0.02*Cos(SimulatedObjectData(i,0)*2)

	If SimulatedObjectData(i,1)>=0 And Rand(0,100)<2 And Obj\Attributes\Indigo=0 AddParticle(7,SimulatedObjectX(i),0.5,-SimulatedObjectY(i),Rand(0,360),0.4,0,0.02,0,Rnd(0,2),.01,0,0,0,50,4)

	If SimulatedObjectData(i,1)<0 Then SimulatedObjectData(i,1)=SimulatedObjectData(i,1)+1

	x=Pos\TileX
	y=Pos\TileY
	; player near or other stinkers near? increase burst timer
	If Obj\Attributes\Indigo=0
		flag=0
		For j=0 To nofobjects-1
			OtherObj.GameObject=LevelObjects(j)
			OtherPos.GameObjectPosition=OtherObj\Position
			OtherType=OtherObj\Attributes\LogicType
			If (OtherType=1 Or OtherType=110 Or OtherType=120) And Abs(x-OtherPos\TileX)<4 And Abs(y-OtherPos\TileY)<4
				; close enough
				flag=1
				Simulatedobjectdata(i,1)=SimulatedObjectData(i,1)+1
				If SimulatedObjectData(i,1)>0 And SimulatedObjectData(i,1) Mod 3 =0
					 AddParticle(8,SimulatedObjectX(i),0.8,-SimulatedObjectY(i),Rand(0,360),SimulatedObjectData(i,1)/200.0+.5,0,0,0,Rnd(0,2),0,0,0,0,30,4)
				EndIf

				If SimulatedObjectData(i,1)=150
					SimulatedObjectData(i,1)=-1000
					; fire spellballs
				EndIf
			EndIf
		Next
		If (Abs(x-PlayerTileX())<4 And Abs(y-PlayerTileY())<4)
			; close enough
			flag=1
			Simulatedobjectdata(i,1)=SimulatedObjectData(i,1)+1
			If SimulatedObjectData(i,1)>0 And SimulatedObjectData(i,1) Mod 3 =0
				 AddParticle(8,SimulatedObjectX(i),0.8,-SimulatedObjectY(i),Rand(0,360),SimulatedObjectData(i,1)/200.0+.5,0,0,0,Rnd(0,2),0,0,0,0,30,4)
			EndIf

			If SimulatedObjectData(i,1)=150
				SimulatedObjectData(i,1)=-1000
				; fire spellballs
			EndIf
		EndIf

		If flag=0 And SimulatedObjectData(i,1)>0
			SimulatedObjectData(i,1)=SimulatedObjectData(i,1)-1
		EndIf
	EndIf

End Function

Function ControlLurker(i)

	If SimulatedObjectData(i,0)=0
		; lurking
		If SimulatedObjectYawAdjust(i)<>0
			SimulatedObjectYaw(i)=SimulatedObjectYawAdjust(i)
			SimulatedObjectYawAdjust(i)=0
		EndIf
		SimulatedObjectPitch2(i)=180
		SimulatedObjectZ(i)=-0.1
		SimulatedObjectData(i,2)=-1
	EndIf

	;SimulateObjectPosition(i)
	;SimulateObjectRotation(i)

End Function

Function ControlSunSphere1(i)

	If SimulatedObjectData(i,9)=0
		SimulatedObjectData(i,9)=1

		SimulatedObjectData(i,7)=Rand(0,360)
		SimulatedObjectData(i,8)=Rand(50,100)
		;CreateSunSphere2(i)
	EndIf

	; in-game this uses ScaleAdjust as a multiplier, but this is pointless since it always gets set to 1.0 when nonzero
	SimulatedObjectZ(i)=1.5+0.8*Sin((leveltimer+SimulatedObjectData(i,7)+30) Mod 360)

	;SimulateObjectPosition(i)

End Function

Function ControlSunSphere2(i)
	SimulatedObjectZ(i)=1.5+0.8*Sin((leveltimer+SimulatedObjectData(i,7)) Mod 360)
	SimulatedObjectXScale(i)=0.5*(1+0.1*Cos(leveltimer Mod 360))
	SimulatedObjectYScale(i)=0.5*(1+0.1*Cos((leveltimer+30) Mod 360))
	SimulatedObjectZScale(i)=0.5*(1+0.1*Cos((leveltimer+60) Mod 360))

	If Rand(0,100)<3 AddParticle(Rand(16,23),SimulatedObjectX(i),SimulatedObjectZ(i),-SimulatedObjectY(i),Rand(0,360),0.16,Rnd(-.025,.025),Rnd(-.025,.025),Rnd(-.025,.025),0,0.001,0,0,0,100,3)

	;SimulateObjectPosition(i)
	;SimulateObjectScale(i)

End Function

Function ControlStinkerWee(i)

	Obj.GameObject=LevelObjects(i)
	Pos.GameObjectPosition=Obj\Position

	For i2=1 To 4
		; heating up smoke
		If SimulatedObjectData(i,6)>i2*120 And Rand(1,9)<4
			AddParticle(0+Floor(i2/2),SimulatedObjectX(i)+Rnd(-.1,.1),0.5,-SimulatedObjectY(i)+Rnd(-.1,.1),0,0.2,Rnd(-0.012,0.012),Rnd(0,0.12),Rnd(-0.012,0.012),5,0.01,0,0,0,35,3)
		EndIf
	Next

	If SimulatedObjectTileTypeCollision(i)=0
		SimulatedObjectTileTypeCollision(i)=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		SimulatedObjectMovementSpeed(i)=35
		SimulatedObjectSubType(i)=0  ; -2 dying, -1 exiting, 0- asleep, 1-follow, 2-directive, 3-about to fall asleep (still walking), 4 caged
		If Obj\Attributes\ModelName$="!StinkerWee"
			MaybeAnimateMD2(Obj\Model\Entity,1,Rnd(.002,.008),217,219,1)
		EndIf
		SimulatedObjectCurrentAnim(i)=1 ; 1-asleep, 2-getting up, 3-idle, 4-wave, 5-tap, 6-walk, 7 sit down, 8-fly, 9-sit on ice
		SimulatedObjectXScale(i)=0.025
		SimulatedObjectYScale(i)=0.025
		SimulatedObjectZScale(i)=0.025
	EndIf

	If Obj\Attributes\Dead=1
		; spinning out of control
		SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+10) Mod 360
		;ObjectZ(i)=ObjectZ(i)+.01
		;ObjectSubType(i)=-2
		Return
	EndIf
	If Obj\Attributes\Dead=3
		; drowning
		SimulatedObjectYaw(i)=90
		;ObjectZ(i)=ObjectZ(i)-.005
		;ObjectSubType(i)=-2
		Return
	EndIf

	If SimulatedObjectSubType(i)=-1
		TurnObjectTowardDirection(i,0,1,4,0)
		Return ; already on its way out
	EndIf

	If Obj\Attributes\Caged=True And SimulatedObjectSubType(i)<>4 And SimulatedObjectSubType(i)<>5
		; just Caged
		If SimulatedObjectData(i,8)>=0 And SimulatedObjectData(i,8)<=2
			EntityTexture Obj\Model\Entity,StinkerWeeTextureSad(SimulatedObjectData(i,8)+1)
		EndIf
		;PlaySoundFX(66,Pos\TileX,Pos\TileY)
		If SimulatedObjectSubType(i)=2
			SimulatedObjectSubType(i)=5
		Else
			SimulatedObjectSubType(i)=4
		EndIf

		If Obj\Attributes\ModelName$="!StinkerWee"
			MaybeAnimateMD2(Obj\Model\Entity,1,.2,108,114,1)
		EndIf
	EndIf
	If Obj\Attributes\Caged=False And (SimulatedObjectSubType(i)=4 Or SimulatedObjectSubType(i)=5)
		; just released
		If SimulatedObjectData(i,8)>=0 And SimulatedObjectData(i,8)<=2
			EntityTexture Obj\Model\Entity,StinkerWeeTexture(SimulatedObjectData(i,8)+1)
		EndIf

		SimulatedObjectSubType(i)=SimulatedObjectSubType(i)-3
		If Obj\Attributes\ModelName$="!StinkerWee"
			MaybeAnimateMD2(Obj\Model\Entity,1,.4,1,20,1)
		EndIf
		SimulatedObjectCurrentAnim(i)=4
		;SimulatedObjectMovementTypeData(i)=0
	EndIf

	If Obj\Attributes\Caged=True
		TurnObjectTowardDirection(i,PlayerTileX()-Pos\TileX,PlayerTileY()-Pos\TileY,3,0)
		Return
	EndIf

	If SimulatedObjectSubType(i)=0
		If SimulatedObjectData(i,8)>=0 And SimulatedObjectData(i,8)<=2
			EntityTexture Obj\Model\Entity,StinkerWeeTextureSleep(SimulatedObjectData(i,8)+1)
		EndIf
	Else
		If SimulatedObjectData(i,8)>=0 And SimulatedObjectData(i,8)<=2
			EntityTexture Obj\Model\Entity,StinkerWeeTexture(SimulatedObjectData(i,8)+1)
		EndIf
	EndIf

	If SimulatedObjectSubType(i)=3 ; asleep after walking
		; fall asleep now
		;EntityTexture ObjectEntity(i),StinkerWeeTextureSleep
		If Obj\Attributes\ModelName$="!StinkerWee"
			MaybeAnimateMD2(Obj\Model\Entity,3,.2,201,217,1)
		EndIf
		SimulatedObjectCurrentAnim(i)=7
		SimulatedObjectData(i,0)=0
		SimulatedObjectData(i,1)=0
		SimulatedObjectData(i,2)=4
		SimulatedObjectSubType(i)=2
		;ObjectMoveXGoal(i)=Pos\TileX
		;ObjectMoveYGoal(i)=Pos\TileY
	Else If SimulatedObjectData(i,2)<4
		; stopped - but wait a few frames before switching animation
		; (to avoid start/stop animation)
		SimulatedObjectData(i,2)=SimulatedObjectData(i,2)+1

	Else
		; not walking
		If SimulatedObjectSubType(i)=0 ; asleep
			SimulatedObjectData(i,2)=SimulatedObjectData(i,2)+1
			If SimulatedObjectData(i,2)>200
				If Rand(0,100)<3
					AddParticle(9,Pos\TileX+.5,.9,-Pos\TileY-.5,0,0.5,0,0.01,0,0,.001,0,0,0,200,3)
					SimulatedObjectData(i,2)=0
					;PlaySoundFX(59,Pos\TileX,Pos\TileY)
				EndIf

			EndIf
			If SimulatedObjectCurrentAnim(i)<>1
				If Obj\Attributes\ModelName$="!StinkerWee"
					MaybeAnimateMD2(Obj\Model\Entity,1,Rnd(.002,.008),217,219,1)
				EndIf
				SimulatedObjectCurrentAnim(i)=1
			EndIf
			If SimulatedObjectYaw(i)<>180 Then TurnObjectTowardDirection(i,0,1,5,0)

		Else ; either in follow or directive mode, but standing

			If SimulatedObjectCurrentAnim(i)<>7
				; turn toward player unless sitting
				TurnObjectTowardDirection(i,PlayerTileX()-Pos\TileX,PlayerTileY()-Pos\TileY,3,0)
			EndIf
			If SimulatedObjectCurrentAnim(i)<>3 And SimulatedObjectCurrentAnim(i)<>4 And SimulatedObjectCurrentAnim(i)<>5 And SimulatedObjectCurrentAnim(i)<>7
				If Obj\Attributes\ModelName$="!StinkerWee"
					MaybeAnimateMD2(Obj\Model\Entity,1,Rnd(.01,.08),141,160,1)
				EndIf
				SimulatedObjectCurrentAnim(i)=3
				SimulatedObjectData(i,0)=0
			Else If SimulatedObjectCurrentAnim(i)=3
				; possible wave/tap animation when in directive mode
				If Rand(0,1000)<2 And SimulatedObjectData(i,1)>100
					; do an animation
					If (Rand(0,100)<50) ; wave
						;PlaySoundFX(Rand(50,54),Pos\TileX,Pos\TileY)
						If Obj\Attributes\ModelName$="!StinkerWee"
							MaybeAnimateMD2(Obj\Model\Entity,3,.2,101,120,1)
						EndIf
						SimulatedObjectCurrentAnim(i)=4
					Else If SimulatedObjectSubtype(i)=2	; tap
						If Obj\Attributes\ModelName$="!StinkerWee"
							MaybeAnimateMD2(Obj\Model\Entity,1,.2,121,140,1)
						EndIf
						SimulatedObjectCurrentAnim(i)=5
					EndIf
				EndIf
			Else If SimulatedObjectCurrentAnim(i)=4
				SimulatedObjectData(i,0)=SimulatedObjectData(i,0)+1
				If SimulatedObjectData(i,0)>100
					SimulatedObjectData(i,0)=0
					If Obj\Attributes\ModelName$="!StinkerWee"
						MaybeAnimateMD2(Obj\Model\Entity,1,Rnd(.01,.03),141,160,1)
					EndIf
					SimulatedObjectCurrentAnim(i)=3
				EndIf
;			Else If SimulatedObjectCurrentAnim(i)=5
;				SimulatedObjectData(i,0)=SimulatedObjectData(i,0)+1
;				If SimulatedObjectData(i,0)>1500
;					SimulatedObjectData(i,0)=0
;					MaybeAnimateMD2(ObjectEntity(i),1,Rnd(.01,.03),141,160,1)
;					SimulatedObjectCurrentAnim(i)=3
;				EndIf
;			Else If SimulatedObjectCurrentAnim(i)=7
;				SimulatedObjectData(i,0)=SimulatedObjectData(i,0)+1
;				If SimulatedObjectYaw(i)<>180 Then TurnObjectTowardDirection(i,0,1,1,0)
;
;				If SimulatedObjectData(i,0)>100
;					; asleep
;					MaybeAnimateMD2(ObjectEntity(i),1,Rnd(.002,.008),217,219,1)
;					SimulatedObjectCurrentAnim(i)=1
;					SimulatedObjectSubType(i)=0
;				EndIf
			EndIf

			; If in directive mode - use timer to see if falling asleep again
;			If ObjectSubType(i)=1
;				ObjectData(i,1)=ObjectData(i,1)+1
;				If ObjectData(i,1)>5000 And leveltimer Mod 5000=0
;					; bored!
;					PlaySoundFX(68,Pos\TileX,Pos\TileY)
;					ObjectData(i,1)=0
; 					EndIf
;			EndIf

;			If ObjectSubType(i)=2
;				ObjectData(i,1)=ObjectData(i,1)+1
;
;				If ObjectData(i,1)>4800
;					; fell asleep again
;					PlaySoundFX(69,Pos\TileX,Pos\TileY)
;			;		EntityTexture ObjectEntity(i),StinkerWeeTextureSleep
;					AnimateMD2 ObjectEntity(i),3,.2,201,217,1
;					ObjectCurrentAnim(i)=7
;					ObjectData(i,0)=0
;					ObjectData(i,1)=0
;				EndIf
;			EndIf
		EndIf
	EndIf

End Function

Function ControlStinkerWeeExit(i)
	If LevelTimer Mod 3 = 0
		Pos.GameObjectPosition=LevelObjects(i)\Position
		AddParticle(Rand(16,23),Pos\TileX+0.5+0.2*Sin(LevelTimer*10),0,-Pos\TileY-0.5-0.2*Cos(LevelTimer*10),Rand(0,360),0.1,0,0.02,0,0,0.005,0,0,0,100,3)
	EndIf
End Function

Function ControlScritter(i)

	;If ObjectMovementTimer(i)>0
	;	SimulatedObjectZ(i)=0.4*Abs(Sin(ObjectMovementTimer(i)*360/1000))
	;	TurnObjectTowardDirection(i,Pos\TileX2-Pos\TileX,Pos\TileY2-Pos\TileY,10,0)
	;Else
		Pos.GameObjectPosition=LevelObjects(i)\Position
		TurnObjectTowardDirection(i,PlayerTileX()-Pos\TileX,PlayerTileY()-Pos\TileY,6,0)
	;EndIf

End Function

Function ControlCrab(i)

	Obj.GameObject=LevelObjects(i)

	;subtype -0-male, 1-female
	;data1 - 0-normal,1-curious, 2- asleep, 3- disabled
	;status - 0 normal, 2 submerged

	If SimulatedObjectTileTypeCollision(i)=0
		; First time
		If SimulatedObjectSubType(i)=0
			; male
			SimulatedObjectTileTypeCollision(i)=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
;			Select SimulatedObjectData(i,1)
;			Case 0
;				; normal
;				ObjectMovementType(i)=0
;			Case 1
;				; curious
;				ObjectMovementType(i)=14
;			Case 2,3
;				; asleep
;				ObjectMovementType(i)=0
;				AnimateMD2 ObjectEntity(i),3,1,48,49
;			End Select
			SimulatedObjectXScale(i)=0.006
			SimulatedObjectYScale(i)=0.006
			SimulatedObjectZScale(i)=0.006

			;SimulateObjectScale(i)
		Else
			;female
			SimulatedObjectTileTypeCollision(i)=2^0+2^2+2^3+2^4+2^9+2^10+2^11+2^12+2^14
;			Select SimulatedObjectData(i,1)
;			Case 0
;				; normal
;				ObjectMovementType(i)=32
;			Case 1
;				; curious
;				ObjectMovementType(i)=14
;			Case 2,3
;				; asleep
;				ObjectMovementType(i)=0
;				AnimateMD2 ObjectEntity(i),3,1,48,49
;			End Select

		EndIf

	EndIf

	If Obj\Attributes\ModelName$="!Crab"
		; anim
		; 1-idle
		; 2-walk
		; 3-walk (used for non-stop animation between tiles)
		; 4-retract
		; 5-come out
		If Obj\Attributes\Frozen>0
			MaybeAnimateMD2(Obj\Model\Entity,2,.01,1,2)
			SimulatedObjectCurrentAnim(i)=0
		Else If Obj\Attributes\MovementTimer=0 And (SimulatedObjectCurrentAnim(i)=0 Or SimulatedObjectCurrentAnim(i)=3) And SimulatedObjectdata(i,1)<2

			MaybeAnimateMD2(Obj\Model\Entity,2,Rnd(.02,.04),1,13)
			SimulatedObjectCurrentAnim(i)=1

		Else If SimulatedObjectCurrentAnim(i)=2
			SimulatedObjectCurrentAnim(i)=3
		Else If Obj\Attributes\MovementTimer>0 And (SimulatedObjectCurrentAnim(i)=0 Or SimulatedObjectCurrentAnim(i)=1 Or SimulatedObjectCurrentAnim(i)=20)
			MaybeAnimateMD2(Obj\Model\Entity,1,1,1,30)
			SimulatedObjectCurrentAnim(i)=2
		Else If SimulatedObjectCurrentAnim(i)>=5 And SimulatedObjectCurrentAnim(i)<20
			; delay for coming out anim so it doesn' immediately go into walking
			SimulatedObjectCurrentAnim(i)=SimulatedObjectCurrentAnim(i)+1
		EndIf
	EndIf

	If SimulatedObjectStatus(i)=0 And SimulatedObjectData(i,1)<2
		SimulatedObjectZ(i)=0
		;If Obj\Attributes\MovementTimer>0
		;	TurnObjectTowardDirection(i,-(Pos\TileX2-Pos\TileX),-(Pos\TileY2-Pos\TileY),10,0)
		;Else
			TurnObjectTowardDirection(i,-(PlayerX()-SimulatedObjectX(i)),-(PlayerY()-SimulatedObjectY(i)),6,0)
		;EndIf
	EndIf

End Function

Function ControlTrap(i)

	Obj.GameObject=LevelObjects(i)

	SimulatedObjectZ(i)=0.04

	If SimulatedObjectActive(i)=1001 Or SimulationLevel<2
		If SimulatedObjectStatus(i)=0
			; currently off
			SimulatedObjectTimer(i)=SimulatedObjectTimer(i)-1
			If SimulatedObjectTimer(i)<=0
				; turn on
				SimulatedObjectTimer(i)=Obj\Attributes\TimerMax1
				SimulatedObjectStatus(i)=1
			EndIf
		Else
			; currently on
			SimulatedObjectTimer(i)=SimulatedObjectTimer(i)-1
			If SimulatedObjectTimer(i)<=0
				; turn off
				SimulatedObjectTimer(i)=Obj\Attributes\TimerMax2
				SimulatedObjectStatus(i)=0
			EndIf
		EndIf

		Select SimulatedObjectSubType(i)
			Case 0
				; fire - create particle when on

			If SimulatedObjectStatus(i)=1
				;If Rand(0,100)<50 AddParticle(2,Pos\X+Rnd(-.1,.1),ObjectZAdjust(i),-ObjectY(i),0-Rnd(-.1,.1),.5,Rnd(-.005,.005),.05,Rnd(-.005,.005),0,.01,0,-.0001,0,50,0)
				If Rand(0,100)<50 AddParticle(2,SimulatedObjectX(i)+Rnd(-.1,.1),SimulatedObjectZAdjust(i),-SimulatedObjectY(i),0-Rnd(-.1,.1),.5,Rnd(-.005,.005),.05,Rnd(-.005,.005),0,.01,0,-.0001,0,50,4)
			EndIf

		End Select
	Else

		;If Rand(0,100)<2 AddParticle(0,Pos\X+Rnd(-.1,.1),ObjectZAdjust(i),-ObjectY(i),0-Rnd(-.1,.1),.3,Rnd(-.005,.005),.02,Rnd(-.005,.005),0,.01,0,-.0001,0,50,0)
		If Rand(0,100)<2 AddParticle(0,SimulatedObjectX(i)+Rnd(-.1,.1),SimulatedObjectZAdjust(i),-SimulatedObjectY(i),0-Rnd(-.1,.1),.3,Rnd(-.005,.005),.02,Rnd(-.005,.005),0,.01,0,-.0001,0,50,4)

	EndIf

	;SimulateObjectPosition(i)

End Function

Function ControlFireFlower(i)

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes

	If Attributes\ModelName$<>"!FireFlower"
		Return
	EndIf

	If (SimulatedObjectTimer(i)>=0 And SimulatedObjectData(i,2)=0) Or (SimulatedObjectData(i,2)=2 And SimulatedObjectTimer(i)=Attributes\TimerMax1)
		SimulatedObjectData(i,2)=1
		MaybeAnimateMD2(Obj\Model\Entity,1,.2,1,20,1)
	EndIf

	;If ObjectActive(i)<1001
	;	SimulatedObjectTimer(i)=ObjectTimerMax1(i)
	;EndIf

	If Attributes\Indigo=0 SimulatedObjectTimer(i)=SimulatedObjectTimer(i)-1
	SimulatedObjectData(i,0)=(SimulatedObjectData(i,0)+80) Mod 8
	SimulatedObjectYawAdjust(i)=0 ; Necessary to make the model face the correct way due to the weird special case handling of its rotation.

	dx#=0
	dy#=0
	If SimulatedObjectSubType(i)=1
		; follow player
		dx=PlayerX()-SimulatedObjectX(i)
		dy=PlayerY()-SimulatedObjectY(i)
		total#=Sqr(dx^2+dy^2)
		dx=dx/total
		dy=dy/total

	Else
		; turn or static
		If SimulatedObjectData(i,0)>0 And SimulatedObjectData(i,0)<4
			dx=1
		EndIf
		If SimulatedObjectData(i,0)>4
			dx=-1
		EndIf
		If SimulatedObjectData(i,0)<2 Or SimulatedObjectData(i,0)>6
			dy=-1
		EndIf
		If SimulatedObjectData(i,0)>2 And SimulatedObjectData(i,0)<6
			dy=1
		EndIf
	EndIf
	If SimulatedObjectTimer(i)>-10
		TurnObjectTowardDirection(i,dx,dy,3,180)
	EndIf

	If SimulatedObjectTimer(i)<0

		If SimulatedObjectData(i,2)=1
			MaybeAnimateMD2(Obj\Model\Entity,1,.5,21,60,1)
			SimulatedObjectData(i,2)=0
		EndIf

		If SimulatedObjectTimer(i)=-80
			SimulatedObjectTimer(i)=Attributes\TimerMax1
		EndIf

		; and fire
		If SimulatedObjectTimer(i)=-60

			If SimulatedObjectSubType(i)=2
				SimulatedObjectData(i,0)=(SimulatedObjectData(i,0)+1) Mod 8
			EndIf
			If SimulatedObjectSubType(i)=3
				SimulatedObjectData(i,0)=(SimulatedObjectData(i,0)-1) Mod 8
			EndIf

		EndIf

	EndIf

End Function

; col is data0 and direction is data2
Function RedoSuctubeMesh(Entity, col, objactive, direction, yawadjust)

	Surface=GetSurface(Entity,1)
	If objactive Mod 2 = 1
		active=0
	Else
		active=1
	EndIf
	If yawadjust=(-90*direction +3600) Mod 360
		; in original position
		dir=0
	Else
		; switched from original
		dir=1
	EndIf

	; point arrow
	If dir=0
		VertexCoords surface,0,-0.3,1.71,-0.3
		VertexCoords surface,1,+0.3,1.71,-0.3
		VertexCoords surface,2,0,1.71,+0.3
	Else
		VertexCoords surface,0,-0.3,1.71,+0.3
		VertexCoords surface,2,+0.3,1.71,+0.3
		VertexCoords surface,1,0,1.71,-0.3
	EndIf

	; and give colour

	VertexTexCoords surface,0,(col Mod 8)*0.125+.01,(col/8)*0.125+.51+.25*active
	VertexTexCoords surface,1,(col Mod 8)*0.125+.115,(col/8)*0.125+.51+.25*active
	VertexTexCoords surface,2,(col Mod 8)*0.125+.051,(col/8)*0.125+.51+.115+.25*active

	UpdateNormals Entity

End Function

Function ToggleObject(i)
	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes

	; switches objects from activating to deactivating or vice versa
	If SimulatedObjectActive(i)<1001 And SimulatedObjectActive(i) Mod 2 =0
		If Attributes\LogicType=410
			;ActivateFlipBridge(i)
		Else
			SimulatedObjectActive(i)=SimulatedObjectActive(i)+Attributes\ActivationSpeed+1

		EndIf
		If SimulatedObjectActive(i)>1001 Then SimulatedObjectActive(i)=1001
	Else If SimulatedObjectActive(i)>0 And SimulatedObjectActive(i) Mod 2 =1
		If Attributes\LogicType=410
			;DeActivateFlipBridge(i)
		Else
			SimulatedObjectActive(i)=SimulatedObjectActive(i)-Attributes\ActivationSpeed-1

		EndIf
		If SimulatedObjectActive(i)<0 Then SimulatedObjectActive(i)=0
	EndIf
	If Attributes\LogicType=281 And Attributes\ModelName$="!SucTube"
		Redosuctubemesh(Obj\Model\Entity, SimulatedObjectData(i,0), SimulatedObjectActive(i), SimulatedObjectData(i,2), SimulatedObjectYawAdjust(i))
	EndIf

End Function

Function ControlChangeActive(i)
	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes

	If SimulatedObjectActive(i)>0 And SimulatedObjectActive(i) Mod 2 = 0
		; deactivating
		SimulatedObjectActive(i)=SimulatedObjectActive(i)-Attributes\ActivationSpeed
	Else If SimulatedObjectActive(i)<1001 And SimulatedObjectActive(i) Mod 2=1
		; activating
		SimulatedObjectActive(i)=SimulatedObjectActive(i)+Attributes\ActivationSpeed

	EndIf
	If SimulatedObjectActive(i)<0 	SimulatedObjectActive(i)=0
	If SimulatedObjectActive(i)>1001 SimulatedObjectActive(i)=1001

End Function

Function ControlSteppingStone(i)
	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes

	; Data(2) - Alternating?
	If SimulatedObjectData(i,2)=2
		SimulatedObjectTimer(i)=SimulatedObjectTimer(i)-1
		If SimulatedObjectTimer(i)<0 Then SimulatedObjectTimer(i)=Attributes\TimerMax1
		If SimulatedObjectTimer(i)=0
			ToggleObject(i)
			SimulatedObjectTimer(i)=Attributes\TimerMax1
		EndIf

		ControlChangeActive(i)
	EndIf

	; 0-submerged, 1001-surfaced
	If (SimulatedObjectActive(i)<1001-4*Attributes\ActivationSpeed) And SimulatedObjectLastActive(i)>=1001-4*Attributes\ActivationSpeed

		; just submerged
		If SimulatedObjectData(i,3)=0
			AddParticle(4,Floor(SimulatedObjectX(i))+0.5,LevelTiles(Floor(SimulatedObjectX(i)),Floor(SimulatedObjectY(i)))\Water\Height-0.2,-Floor(SimulatedObjectY(i))-0.5,0,.6,0,0,0,0,.006,0,0,0,50,4)
		EndIf

	EndIf

	If (SimulatedObjectActive(i)=>1001-4*Attributes\ActivationSpeed) And SimulatedObjectLastActive(i)<1001-4*Attributes\ActivationSpeed

		; just emerged
		If SimulatedObjectData(i,3)=0
			AddParticle(4,Floor(SimulatedObjectX(i))+0.5,LevelTiles(Floor(SimulatedObjectX(i)),Floor(SimulatedObjectY(i)))\Water\Height-0.2,-Floor(SimulatedObjectY(i))-0.5,0,1,0,0,0,0,.006,0,0,0,100,4)
		EndIf

	EndIf

End Function

Function ControlFlipBridge(i)

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes

	YScale#=6.6

	If (SimulatedObjectActive(i)<>0 And SimulatedObjectActive(i)<>1001) Or SimulationLevel>=2
		YScale#=1+5.6*Float(SimulatedObjectActive(i))/1001.0
	EndIf

	If Attributes\ModelName$="!FlipBridge"
		ScaleEntity GetChild(Obj\Model\Entity,1),1.0,1.0,YScale#
	Else
		SimulatedObjectScaleYAdjust(i)=YScale#
	EndIf

End Function

Function ControlSpring(i)

	SimulatedObjectZ(i)=.5

End Function

Function ControlBabyBoomer(i)

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes

	If Attributes\Dead=1
		; spinning out of control
		SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+10) Mod 360
		;ObjectZ(i)=ObjectZ(i)+.01
		;ObjectSubType(i)=-2
		Return
	EndIf
	If Attributes\Dead=3
		; drowning
		SimulatedObjectYaw(i)=90
		;ObjectZ(i)=ObjectZ(i)-.005
		;ObjectSubType(i)=-2
		Return
	EndIf

	If SimulatedObjectData(i,8)=1
		; lit
;		For j=1 To 5
			If Rand(0,100)<20
				AddParticle(23,SimulatedObjectX(i),Rnd(0.7,0.8),-SimulatedObjectY(i),0,.05,Rnd(-0.005,0.005),Rnd(0,0.005),Rnd(-0.005,0.005),0,.004,0,0,0,50,3)
			EndIf
;		Next
	EndIf

	If SimulatedObjectData(i,8)>0
		;EntityTexture Obj\Model\Entity,KaboomTextureSquint
		; lit and burning
		For j=1 To 5
			If Rand(0,100)<SimulatedObjectData(i,8)
				AddParticle(Rand(16,18),SimulatedObjectX(i),Rnd(0.7,0.8),-SimulatedObjectY(i),0,.1,Rnd(-0.02,0.02),Rnd(0,0.02),Rnd(-0.02,0.02),0,.004,0,-.0001,0,50,3)
			EndIf
		Next
	EndIf

End Function

Function ControlSuctube(i)

	If SimulatedObjectActive(i)<>1001 Then Return

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes
	Pos.GameObjectPosition=Obj\Position

	suck=True
	blow=True

	; check if sucking/blowing active (e.g. if another tube in front of it)
	For j=0 To NofObjects-1
		OtherObj.GameObject=LevelObjects(j)
		OtherAttributes.GameObjectAttributes=Obj\Attributes
		OtherPos.GameObjectPosition=Obj\Position

		If OtherAttributes\LogicType=281 And i<>j
			; found another suctube
			If Attributes\Data2=OtherAttributes\Data2 And Attributes\Data0=OtherAttributes\Data0 And Attributes\Data1=OtherAttributes\Data1
				; same direction
				If SimulatedObjectData(i,2)=0
					If Pos\TileX=OtherPos\TileX And Pos\TileY=OtherPos\TileY-1
						suck=False
					EndIf
					If Pos\TileX=OtherPos\TileX And Pos\TileY=OtherPos\TileY+1
						blow=False
					EndIf
				Else If SimulatedObjectData(i,2)=1
					If Pos\TileX=OtherPos\TileX+1 And Pos\TileY=OtherPos\TileY
						suck=False
					EndIf
					If Pos\TileX=OtherPos\TileX-1 And Pos\TileY=OtherPos\TileY
						blow=False
					EndIf
				Else If SimulatedObjectData(i,2)=2
					If Pos\TileX=OtherPos\TileX And Pos\TileY=OtherPos\TileY+1
						suck=False
					EndIf
					If Pos\TileX=OtherPos\TileX And Pos\TileY=OtherPos\TileY-1
						blow=False
					EndIf
				Else If SimulatedObjectData(i,2)=3
					If Pos\TileX=OtherPos\TileX-1 And Pos\TileY=OtherPos\TileY
						suck=False
					EndIf
					If Pos\TileX=OtherPos\TileX+1 And Pos\TileY=OtherPos\TileY
						blow=False
					EndIf
				EndIf
			EndIf
		EndIf
	Next

	If SimulatedObjectData(i,5)=0
		; particle effects
		If Rand(0,100)<30
			psize#=Rnd(0.1,0.2)
			pspeed#=Rnd(1,2)
			parttex=Rand(16,23)
			If suck=True
				Select SimulatedObjectData(i,2)
				Case 0
					AddParticle(parttex,SimulatedObjectX(i)+Rnd(-1,1),Rnd(0.5,1.4),-SimulatedObjectY(i)-Rnd(1.0,1.9),0,psize,0.0,0.0,-Rnd(-0.01,-0.02)*pspeed,0,0,0,0,0,Rand(10,50),3)
		 		Case 1
					AddParticle(parttex,SimulatedObjectX(i)-Rnd(1.0,1.5),Rnd(0.5,1.4),-SimulatedObjectY(i)+Rnd(-1,1),0,psize,Rnd(0.01,0.02)*pspeed,0.0,0.0,0,0,0,0,0,Rand(10,50),3)
		 		Case 2
				;	AddParticle(0,0,Rnd(0.5,5.5),0,0,5,0.0,0.0,Rnd(-0.01,-0.02),0,0,0,0,0,Rand(10,50),3)

					AddParticle(parttex,SimulatedObjectX(i)+Rnd(-1,1),Rnd(0.5,1.4),-SimulatedObjectY(i)+Rnd(1.0,1.9),0,psize,0.0,0.0,Rnd(-0.01,-0.02)*pspeed,0,0,0,0,0,Rand(10,50),3)
		 		Case 3
					AddParticle(parttex,SimulatedObjectX(i)+Rnd(1.0,1.5),Rnd(0.5,1.4),-SimulatedObjectY(i)+Rnd(-1,1),0,psize,-Rnd(0.01,0.02)*pspeed,0.0,0.0,0,0,0,0,0,Rand(10,50),3)
		 		End Select
			EndIf
			If blow=True
				Select SimulatedObjectData(i,2)
				Case 0
					AddParticle(parttex,SimulatedObjectX(i)+Rnd(-1,1),Rnd(0.5,1.4),-SimulatedObjectY(i)+Rnd(0.0,0.5),0,psize,0.0,0.0,-Rnd(-0.01,-0.02)*pspeed,0,0,0,0,0,Rand(10,50),3)
		 		Case 1
					AddParticle(parttex,SimulatedObjectX(i)+Rnd(0,0.5),Rnd(0.5,1.4),-SimulatedObjectY(i)+Rnd(-1,1),0,psize,Rnd(0.01,0.02)*pspeed,0.0,0.0,0,0,0,0,0,Rand(10,50),3)
		 		Case 2
					AddParticle(parttex,SimulatedObjectX(i)+Rnd(-1,1),Rnd(0.5,1.4),-SimulatedObjectY(i)-Rnd(0.0,0.5),0,psize,0.0,0.0,Rnd(-0.01,-0.02)*pspeed,0,0,0,0,0,Rand(10,50),3)
		 		Case 3
					AddParticle(parttex,SimulatedObjectX(i)-Rnd(0.0,0.5),Rnd(0.5,1.4),-SimulatedObjectY(i)+Rnd(-1,1),0,psize,-Rnd(0.01,0.02)*pspeed,0.0,0.0,0,0,0,0,0,Rand(10,50),3)
		 		End Select
			EndIf
		EndIf
	EndIf

End Function

Function ControlThwart(i)

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes

	TurnObjectTowardDirection(i,-PlayerX()+SimulatedObjectX(i),-PlayerY()+SimulatedObjectY(i),6,-SimulatedObjectYawAdjust(i))

	; shooting?
	If SimulatedObjectData(i,6)>0 And Attributes\Indigo=0
		dx#=PlayerX()-SimulatedObjectX(i)
		dy#=PlayerY()-SimulatedObjectY(i)
		total#=Sqr(dx^2+dy^2)
		dx=dx/total
		dy=dy/total

		SimulatedObjectTimer(i)=SimulatedObjectTimer(i)-1

		If SimulatedObjectTimer(i)<0
			If SimulatedObjectTimer(i)=-10
				; aquire target now
				SimulatedObjectData(i,4)=dx*10000
				SimulatedObjectData(i,5)=dy*10000
			EndIf
			If SimulatedObjectTimer(i)=-1
				If Attributes\ModelName$="!Thwart"
					MaybeAnimateMD2(Obj\Model\Entity,3,1,81,120,1)
				EndIf
			EndIf

			If SimulatedObjectTimer(i)=-40
				SimulatedObjectTimer(i)=SimulatedObjectData(i,7)
			EndIf
		EndIf
	EndIf

End Function

Function ControlTroll(i)

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes

	TurnObjectTowardDirection(i,-PlayerX()+SimulatedObjectX(i),-PlayerY()+SimulatedObjectY(i),6,-SimulatedObjectYawAdjust(i))

	; shooting?
	If SimulatedObjectData(i,6)>0 And SimulatedObjectActive(i)=1001 And Attributes\Indigo=0
		dx#=PlayerX()-SimulatedObjectX(i)
		dy#=PlayerY()-SimulatedObjectY(i)
		total#=Sqr(dx^2+dy^2)
		dx=dx/total
		dy=dy/total

		SimulatedObjectTimer(i)=SimulatedObjectTimer(i)-1

		If SimulatedObjectTimer(i)<0
			If SimulatedObjectTimer(i)=-10
				; aquire target now
				SimulatedObjectData(i,4)=dx*10000
				SimulatedObjectData(i,5)=dy*10000
			EndIf
			If SimulatedObjectTimer(i)=-1
				If Attributes\ModelName$="!Troll"
					MaybeAnimateMD2(Obj\Model\Entity,3,1,81,119,1)
				EndIf
			EndIf

			If SimulatedObjectTimer(i)=-40
				SimulatedObjectTimer(i)=SimulatedObjectData(i,7)
			EndIf
		EndIf
	EndIf

End Function

Function ControlRetroCoily(i)

	SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+2

End Function

Function ControlCuboid(i)

	Obj.GameObject=LevelObjects(i)
	Pos.GameObjectPosition=Obj\Position

	If SimulatedObjectData(i,5)<>Pos\TileX Or SimulatedObjectData(i,6)<>Pos\TileY
		SimulatedObjectData(i,5)=0
		SimulatedObjectData(i,6)=0
	EndIf

	SimulatedObjectXScale(i)=.9+.1*Sin((LevelTimer*2) Mod 360)
	SimulatedObjectYScale(i)=.9+.1*Sin((LevelTimer*2) Mod 360)
	SimulatedObjectZScale(i)=.9+.1*Sin((LevelTimer*2) Mod 360)

	If SimulatedObjectData(i,1)<>0 SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+1

End Function

Function ControlFountain(i)

	If SimulatedObjectActive(i)>0
		Obj.GameObject=LevelObjects(i)
		Pos.GameObjectPosition=Obj\Position

		AddParticle(SimulatedObjectData(i,0),Pos\TileX+.5,SimulatedObjectZAdjust(i)+.5,-Pos\TileY-.5,0,.1,Rnd(-.01,.01),Rnd(.07,.099),Rnd(-.01,.01),0,.001,0,-.001,0,150,3)
	EndIf

End Function

Function ControlMeteorite(i)

	AddParticle(Rand(0,3),SimulatedObjectX(i)+Rnd(-.1,.1),SimulatedObjectZ(i)+Rnd(-.1,.1),-SimulatedObjectY(i)+Rnd(-.1,.1),0,Rnd(0.1,.5),Rnd(-.01,.01),Rnd(-.01,.01),Rnd(-.01,.01),3,.02,0,0,0,125,3)

End Function

Function ControlZipper(i)

	If SimulatedObjectTileTypeCollision(i)=0
		SimulatedObjectTileTypeCollision(i)=-1 ; not really used

		Obj.GameObject=LevelObjects(i)

		EntityBlend Obj\Model\Entity,3

		SimulatedObjectData(i,1)=Rand(0,360)
		SimulatedObjectData(i,2)=Rand(1,4)

	EndIf

	;zz#=.05*Sin(((Leveltimer+ObjectData(i,1))) Mod 360)
	SimulatedObjectZ(i)=0
	size#=.7+.1*Sin(leveltimer Mod 360)
	If size<0 Then size=0

	SimulatedObjectXScale(i)=size
	SimulatedObjectYScale(i)=size
	SimulatedObjectZScale(i)=size
	If leveltimer Mod 4=1 AddParticle(Rand(24,30),SimulatedObjectX(i),SimulatedObjectZ(i),-SimulatedObjectY(i),0,.4*size,0,0.00,0,3,0,0,0,0,25,3)

End Function

Function ControlButterfly(i)

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes

	If SimulatedObjectTileTypeCollision(i)=0
		SimulatedObjectTileTypeCollision(i)=-1 ; not really used

		If Attributes\ModelName$="!Busterfly"

			SimulatedObjectXScale(i)=.01
			SimulatedObjectYScale(i)=.01
			SimulatedObjectZScale(i)=.01
			SimulatedObjectRoll2(i)=90

			;AnimateMD2 Obj\Model\Entity,2,.4,2,9
		Else
			EntityBlend Obj\Model\Entity,3
		EndIf

		SimulatedObjectData(i,1)=Rand(0,360)
		SimulatedObjectData(i,2)=Rand(1,4)

	EndIf

	If Attributes\ModelName$="!Busterfly"
		zz#=.2*Sin((SimulatedObjectData(i,2)*(Leveltimer+SimulatedObjectData(i,1))) Mod 360)
		;TurnObjectTowardDirection(i,ObjectDX(i),ObjectDY(i),2,90)
		SimulatedObjectZ(i)=.4+zz
	Else
		zz#=.2*Sin(((Leveltimer+SimulatedObjectData(i,1))) Mod 360)
		SimulatedObjectZ(i)=.4+zz
		size#=.4+2*zz
		If size<0 Then size=0

		SimulatedObjectXScale(i)=size
		SimulatedObjectYScale(i)=size
		SimulatedObjectZScale(i)=size
		If leveltimer Mod 4=1 AddParticle(Rand(24,30),SimulatedObjectX(i)-3*Attributes\DX,SimulatedObjectZ(i),-SimulatedObjectY(i)+3*Attributes\DY,0,.3*size,0,0.00,0,3,0,0,0,0,15,3)

	EndIf

End Function

Function ControlSpellBall(i)

	If SimulatedObjectSubType(i)<8
		myparticle=24+SimulatedObjectSubType(i)
	Else
		myparticle=Rand(24,31)
	EndIf

	; do the trail
	If (LevelTimer Mod 2=0) And SimulatedObjectData(i,8)<>-99
		AddParticle(myparticle,SimulatedObjectX(i)+Rnd(-.1,.1),SimulatedObjectZ(i)+Rnd(-.1,.1),-SimulatedObjectY(i)+Rnd(-.1,.1),0,0.5,0,0.00,0,3,.01,0,0,0,75,3)
	EndIf

End Function

Function ControlChomper(i)

	Obj.GameObject=LevelObjects(i)
	Pos.GameObjectPosition=Obj\Position

	If SimulatedObjectTileTypeCollision(i)=0
		;AnimateMD2 Obj\Model\Entity,1,.6,1,29
		SimulatedObjectYawAdjust(i)=0
		SimulatedObjectMovementSpeed(i)=20+5*SimulatedObjectData(i,0)
		;SimulatedPos\TileX=Floor(SimulatedObjectX(i))
		;SimulatedPos\TileY=Floor(SimulatedObjectY(i))
		SimulatedObjectTileTypeCollision(i)=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		;SimulatedObjectObjectTypeCollision(i)=2^1+2^3+2^6
		;SimulatedObjectMovementType(i)=13
		If SimulatedObjectData(i,1)=1
			;SimulatedObjectObjectTypeCollision(i)=2^1+2^6+2^4
			EntityBlend Obj\Model\Entity,3

		EndIf
		If SimulatedObjectData(i,1)=2
			EntityFX Obj\Model\Entity,1
		EndIf
	EndIf

	If SimulatedObjectData(i,1)=1
		If leveltimer Mod 360<180
			EntityAlpha Obj\Model\Entity,Abs(Sin(LevelTimer Mod 360))
		Else
			EntityAlpha Obj\Model\Entity,0.3*Abs(Sin(LevelTimer Mod 360))

		EndIf
	EndIf

	;If Obj\Attributes\MovementTimer>0
	;	TurnObjectTowardDirection(i,Pos\TileX2-Pos\TileX,Pos\TileY2-Pos\TileY,3,180)
	;Else
		TurnObjectTowardDirection(i,PlayerTileX()-Pos\TileX,PlayerTileY()-Pos\TileY,1,180)
	;EndIf

End Function

Function ControlNPC(i)

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes
	Pos.GameObjectPosition=Obj\Position

	; Q - player NPC functionality
	If not IsModelNPC(Attributes\ModelName$) Return ; don't want to risk a MAV

	If SimulatedObjectFrozen(i)=1 Or SimulatedObjectFrozen(i)=10001 Or SimulatedObjectFrozen(i)=-1
		; freeze
		If SimulatedObjectFrozen(i)=10001
			SimulatedObjectFrozen(i)=SimulatedObjectFrozen(i)+999
		Else
			SimulatedObjectFrozen(i)=1000*SimulatedObjectFrozen(i)
		EndIf
		SimulatedObjectCurrentAnim(i)=11
		MaybeAnimate(GetChild(Obj\Model\Entity,3),3,1,11)
		;PlaySoundFX(85,ObjectX(i),ObjectY(i))

	EndIf
	If SimulatedObjectFrozen(i)=2 Or SimulatedObjectFrozen(i)=10002
		; revert
		SimulatedObjectFrozen(i)=0
		SimulatedObjectCurrentAnim(i)=10
		MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.05,10)

	EndIf
;	If SimulatedObjectFrozen(i)>2 Or SimulatedObjectFrozen(i)<0
;		; frozen
;		SimulatedObjectFrozen(i)=SimulatedObjectFrozen(i)-1
;
;		Return
;	EndIf

	;dist=100 ; Distance from player
	Dist=maximum2(Abs(Pos\TileX-BrushCursorX),Abs(Pos\TileY-BrushCursorY))
	; Exclamation
	If SimulatedObjectExclamation(i)>=100 And SimulatedObjectExclamation(i)<200 And Dist>4
		SimulatedObjectExclamation(i)=SimulatedObjectExclamation(i)-100
	EndIf
	If SimulatedObjectExclamation(i)>=0 And SimulatedObjectExclamation(i)<100 And Dist<4

		AddParticle(SimulatedObjectExclamation(i),Pos\TileX+.5,1.3,-Pos\TileY-.5,0,.5,0,0.0125,0,0,.004,0,-.0001,0,150,3)
		SimulatedObjectExclamation(i)=SimulatedObjectExclamation(i)+100
	EndIf

	If SimulatedObjectTileTypeCollision(i)=0
		SimulatedObjectData10(i)=-1

		SimulatedObjectTileTypeCollision(i)=2^0+2^3+2^4+2^9+2^11+2^12+2^14
		;SimulatedObjectObjectTypeCollision(i)=2^6
		If SimulatedObjectMoveXGoal(i)=0 And SimulatedObjectMoveYGoal(i)=0
			SimulatedObjectMoveXGoal(i)=Floor(SimulatedObjectX(i))
			SimulatedObjectMoveYGoal(i)=Floor(SimulatedObjectY(i))
			;ObjectMovementType(i)=0
			;ObjectMovementTimer(i)=0
			;ObjectSubType(i)=0
			SimulatedObjectCurrentAnim(i)=10
		EndIf

	EndIf

	If Attributes\Linked=-1 And SimulatedObjectData10(i)>=0

		; just restarted after talking and/or after transporter
	;	If ObjectMoveXGoal(i)=Pos\TileX And ObjectMoveYGoal(i)=Pos\TileY
			SimulatedObjectMoveXGoal(i)=SimulatedObjectData10(i) Mod 200
			SimulatedObjectMoveYGoal(i)=SimulatedObjectData10(i) / 200
			;SimulatedObjectMovementType(i)=10
	;	EndIf
		SimulatedObjectData10(i)=-1
	EndIf

	If Attributes\Flying/10=1
		; flying
		If SimulatedObjectCurrentAnim(i)<>11
			MaybeAnimate(GetChild(Obj\Model\Entity,3),1,1,11)
			SimulatedObjectCurrentAnim(i)=11
		EndIf
		TurnObjectTowardDirection(i,-(Pos\TileX-Pos\TileX2),-(Pos\TileY-Pos\TileY2),10,-SimulatedObjectYawAdjust(i))
	Else If Attributes\Flying/10=2
		; on ice
		If SimulatedObjectCurrentAnim(i)<>13
			MaybeAnimate(GetChild(Obj\Model\Entity,3),3,2,13)
			SimulatedObjectCurrentAnim(i)=13
		EndIf

	Else
		; standing controls

		; Turning?
		Select SimulatedObjectData(i,7) Mod 10
		Case 0
			; Turn toward ObjectYawAdjust, i.e. Angle 0
			If SimulatedObjectYaw(i)<>0
				TurnObjectTowardDirection(i,0,1,4,0)
			EndIf
		Case 1
			; Turn Toward Player
			TurnObjectTowardDirection(i,PlayerX()-SimulatedObjectX(i),PlayerY()-SimulatedObjectY(i),6,-SimulatedObjectYawAdjust(i))

		Case 2
			; Various turning options
			SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+.5) Mod 360
		Case 3
			; Various turning options
			SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+2) Mod 360
		Case 4
			; Various turning options
			SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)-.5) Mod 360
		Case 5
			; Various turning options
			SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)-2) Mod 360
		End Select

		; Jumping?
		If SimulatedObjectData(i,7)/10=1
			SimulatedObjectZ(i)=0.4*Abs(Sin((Float(Leveltimer)*3.6) Mod 360))
		Else If SimulatedObjectData(i,7)/10=2
			SimulatedObjectZ(i)=0.2*Abs(Sin((Float(Leveltimer)*7.2) Mod 360))
		EndIf

		; Animation?
		Select SimulatedObjectData(i,8)
		Case 0
			; Just Swaying
			If SimulatedObjectCurrentAnim(i)<>10
				SimulatedObjectCurrentAnim(i)=10
				MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.05,10)
			EndIf
		Case 1
			; Wave from time to Time
			If SimulatedObjectCurrentAnim(i)=10
				If Rand(1,10)<5 And Leveltimer Mod 120 =0
					SimulatedObjectCurrentAnim(i)=8
					MaybeAnimate(GetChild(Obj\Model\Entity,3),3,.2,8)
				EndIf
			Else If Animating (GetChild(Obj\Model\Entity,3))=False
				SimulatedObjectCurrentAnim(i)=10
				MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.05,10)
			EndIf

		Case 2
			; Wave All The Time
			If SimulatedObjectCurrentAnim(i)<>15
				SimulatedObjectCurrentAnim(i)=15
				MaybeAnimate(GetChild(Obj\Model\Entity,3),2,.2,15)
			EndIf
		Case 3
			; Foottap from time to Time
			If SimulatedObjectCurrentAnim(i)=10
				If Rand(1,10)<5 And Leveltimer Mod 240 =0
					SimulatedObjectCurrentAnim(i)=9
					MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.4,9)
				EndIf
			Else
				If Rand(0,1000)<2
					SimulatedObjectCurrentAnim(i)=10
					MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.05,10)
				EndIf
			EndIf

		Case 4
			; Foottap All The Time
			If SimulatedObjectCurrentAnim(i)<>9
				SimulatedObjectCurrentAnim(i)=9
				MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.2,9)
			EndIf

		Case 5
			; Dance
			If SimulatedObjectCurrentAnim(i)<>12
				SimulatedObjectCurrentAnim(i)=12
				If SimulatedObjectData(i,7)>=20
					MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.4,12)
				Else
					MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.2,12)
				EndIf
			EndIf
		Case 6
			; Just Sit
			If SimulatedObjectCurrentAnim(i)<>14
				SimulatedObjectCurrentAnim(i)=14
				MaybeAnimate(GetChild(Obj\Model\Entity,3),3,.2,14)
			EndIf
		Case 7
			; Sit if far from player, otherwise stand

			If SimulatedObjectCurrentAnim(i)<>14 And dist>3
				SimulatedObjectCurrentAnim(i)=14
				MaybeAnimate(GetChild(Obj\Model\Entity,3),3,.4,14)
			EndIf
			If SimulatedObjectCurrentAnim(i)<>114 And dist<=3
				SimulatedObjectCurrentAnim(i)=114
				MaybeAnimate(GetChild(Obj\Model\Entity,3),3,-.4,14)
			EndIf
		Case 8
			; Sit if far from player, otherwise stand and wave fast
			Dist=maximum2(Abs(Pos\TileX-PlayerTileX()),Abs(Pos\TileY-PlayerTileY()))
			If SimulatedObjectCurrentAnim(i)<>14 And dist>3
				SimulatedObjectCurrentAnim(i)=14
				MaybeAnimate(GetChild(Obj\Model\Entity,3),3,.4,14)
			EndIf
			If SimulatedObjectCurrentAnim(i)<>114 And dist<=3
				SimulatedObjectCurrentAnim(i)=114
				MaybeAnimate(GetChild(Obj\Model\Entity,3),3,-.4,14)
			EndIf
			If SimulatedObjectCurrentAnim(i)=114 And Animating(GetChild(Obj\Model\Entity,3))=False
				MaybeAnimate(GetChild(Obj\Model\Entity,3),3,.4,15)
			EndIf

		Case 9
			; Deathwave from time to Time (+Jumping)
			If SimulatedObjectCurrentAnim(i)=10
				If Rand(1,10)<5 And Leveltimer Mod 240 =0
					SimulatedObjectCurrentAnim(i)=11
					MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.4,11)
					If SimulatedObjectData(i,y)<10 Then SimulatedObjectData(i,7)=SimulatedObjectData(i,7)+20
				EndIf
			Else
				If Leveltimer Mod 120 =0
					SimulatedObjectCurrentAnim(i)=10
					MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.05,10)
					SimulatedObjectData(i,7)=SimulatedObjectData(i,7)-20
					SimulatedObjectZ(i)=0
				EndIf
			EndIf

		Case 10
			; Deathwave All The Time
			If SimulatedObjectCurrentAnim(i)<>11
				SimulatedObjectCurrentAnim(i)=11
				If SimulatedObjectData(i,7)>=20
					MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.4,11)
				Else
					MaybeAnimate(GetChild(Obj\Model\Entity,3),1,.2,11)
				EndIf

			EndIf
		End Select

	EndIf

End Function

Function ControlKaboom(i)

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes
	Pos.GameObjectPosition=Obj\Position

	If Attributes\ModelName$<>"!Kaboom" Return

	If SimulatedObjectTileTypeCollision(i)=0
		; First time (should later be put into object creation at level editor)
		SimulatedObjectData10(i)=-1

		SimulatedObjectTileTypeCollision(i)=2^0+2^3+2^4+2^9+2^11+2^12+2^14
		;SimulatedObjectObjectTypeCollision(i)=2^6
		If SimulatedObjectMoveXGoal(i)=0 And SimulatedObjectMoveYGoal(i)=0
			SimulatedObjectMoveXGoal(i)=Floor(SimulatedObjectX(i))
			SimulatedObjectMoveYGoal(i)=Floor(SimulatedObjectY(i))
			;ObjectMovementType(i)=0
			;ObjectMovementTimer(i)=0
			;ObjectSubType(i)=0
			SimulatedObjectCurrentAnim(i)=10
			AnimateMD2 Obj\Model\Entity,0,.2,1,2

		EndIf

	EndIf

	If Attributes\Dead=1
		; spinning out of control
		SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+10) Mod 360
		SimulatedObjectZ(i)=SimulatedObjectZ(i)+.01

		Return
	EndIf
	If Attributes\Dead=3
		; drowning
		SimulatedObjectYaw(i)=0
		SimulatedObjectZ(i)=SimulatedObjectZ(i)-.005

		Return
	EndIf

	;dist=100 ; Distance to player
	Dist=maximum2(Abs(Pos\TileX-PlayerTileX()),Abs(Pos\TileY-PlayerTileY()))
	; Exclamation
	If SimulatedObjectExclamation(i)>=100 And SimulatedObjectExclamation(i)<200 And Dist>4
		SimulatedObjectExclamation(i)=SimulatedObjectExclamation(i)-100
	EndIf
	If SimulatedObjectExclamation(i)>=0 And SimulatedObjectExclamation(i)<100 And Dist<4

		AddParticle(SimulatedObjectExclamation(i),Pos\TileX+.5,1.3,-Pos\TileY-.5,0,.5,0,0.0125,0,0,.004,0,-.0001,0,150,3)
		SimulatedObjectExclamation(i)=SimulatedObjectExclamation(i)+100
	EndIf

	If Attributes\Flying/10=1
		; flying
		If SimulatedObjectCurrentAnim(i)<>11
			;Animate GetChild(Obj\Model\Entity,3),1,1,11
			AnimateMD2 Obj\Model\Entity,3,2,31,60
			SimulatedObjectCurrentAnim(i)=11
		EndIf
		TurnObjectTowardDirection(i,-(Pos\TileX-Pos\TileX2),-(Pos\TileY-Pos\TileY2),10,-SimulatedObjectYawAdjust(i))
	Else If Attributes\Flying/10=2
		; on ice
		If SimulatedObjectCurrentAnim(i)<>11
			;Animate GetChild(Obj\Model\Entity,3),3,2,13
			AnimateMD2 Obj\Model\Entity,3,2,31,60
			SimulatedObjectCurrentAnim(i)=11
		EndIf

	Else
		; standing controls

		; Turning?
		Select SimulatedObjectData(i,7) Mod 10
		Case 0
			; Turn toward ObjectYawAdjust, i.e. Angle 0
			If SimulatedObjectYaw(i)<>0
				TurnObjectTowardDirection(i,0,1,4,0)
			EndIf
		Case 1
			; Turn Toward Player
			TurnObjectTowardDirection(i,PlayerX()-SimulatedObjectX(i),PlayerY()-SimulatedObjectY(i),6,-SimulatedObjectYawAdjust(i))

		Case 2
			; Various turning options
			SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)-.5) Mod 360
		Case 3
			; Various turning options
			SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)-2) Mod 360
		Case 4
			; Various turning options
			SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+.5) Mod 360
		Case 5
			; Various turning options
			SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+2) Mod 360
		End Select
		; Jumping?
		If SimulatedObjectData(i,7)/10=1
			SimulatedObjectZ(i)=0.4*Abs(Sin((Float(Leveltimer)*3.6) Mod 360))
		Else If SimulatedObjectData(i,7)/10=2
			SimulatedObjectZ(i)=0.2*Abs(Sin((Float(Leveltimer)*7.2) Mod 360))
		EndIf
		; Animation?
		Select SimulatedObjectData(i,8)
		Case 0
			; Just Swaying
			If SimulatedObjectCurrentAnim(i)<>10
				SimulatedObjectCurrentAnim(i)=10
				;Animate GetChild(Obj\Model\Entity,3),1,.05,10
				AnimateMD2 Obj\Model\Entity,0,.2,1,2
			EndIf

		Case 1
			; Just Sit
			If SimulatedObjectCurrentAnim(i)<>13
				SimulatedObjectCurrentAnim(i)=13
				;Animate GetChild(Obj\Model\Entity,3),3,.2,14
				AnimateMD2 Obj\Model\Entity,3,.5,31,50
			EndIf
		Case 2
			; Sit if far from player, otherwise stand

			If SimulatedObjectCurrentAnim(i)<>13 And dist>3
				SimulatedObjectCurrentAnim(i)=13
				;Animate GetChild(Obj\Model\Entity,3),3,.4,14
				AnimateMD2 Obj\Model\Entity,3,.5,31,50
			EndIf
			If SimulatedObjectCurrentAnim(i)<>113 And dist<=3
				SimulatedObjectCurrentAnim(i)=113
				;Animate GetChild(Obj\Model\Entity,3),3,-.4,14
				AnimateMD2 Obj\Model\Entity,3,-.5,50,31
			EndIf

		Case 3
			; Shiver from time to time
			If SimulatedObjectCurrentAnim(i)=10
				If Rand(1,10)<5 And Leveltimer Mod 240 =0
					SimulatedObjectCurrentAnim(i)=15
					;Animate GetChild(Obj\Model\Entity,3),1,.4,11
					AnimateMD2 Obj\Model\Entity,2,.5,55,70

				EndIf
			Else
				If Leveltimer Mod 240 =0
					SimulatedObjectCurrentAnim(i)=10
					;Animate GetChild(Obj\Model\Entity,3),1,.05,10
					AnimateMD2 Obj\Model\Entity,3,-.2,70,53

				EndIf
			EndIf

		Case 4
			; Shiver All The Time
			If SimulatedObjectCurrentAnim(i)<>15
				SimulatedObjectCurrentAnim(i)=15
				AnimateMD2 Obj\Model\Entity,2,.5,59,70

			EndIf
		Case 5
			; Bounce
			If SimulatedObjectCurrentAnim(i)<>16
				SimulatedObjectCurrentAnim(i)=16
				;Animate GetChild(Obj\Model\Entity,3),3,.2,14
				AnimateMD2 Obj\Model\Entity,2,.5,31,50
			EndIf

		End Select

	EndIf

End Function

Function ControlZbotNPC(i)

	Obj.GameObject=LevelObjects(i)
	Attributes.GameObjectAttributes=Obj\Attributes

	If SimulatedObjectData(i,0)>0 And Attributes\Indigo=0
		SimulatedObjectData(i,0)=SimulatedObjectData(i,0)+1
		If SimulatedObjectData(i,0)=120
			;DestroyObject(i,0)
			Return
		EndIf
	EndIf

	If SimulatedObjectData(i,0)>0 And Attributes\Indigo=0
		SimulatedObjectYaw(i)=SimulatedObjectYaw(i)+Float(SimulatedObjectData(i,0))/10.0
		SimulatedObjectZ(i)=SimulatedObjectZ(i)+0.002
		Return
	EndIf

	; particle effects
	If SimulatedObjectActive(i)>0 And SimulatedObjectActive(i)<1001 ; currently activating or deactivating
		If Rand(0,100)<50
			a=Rand(0,360)
			b#=Rnd(0.002,0.006)
			AddParticle(23,SimulatedObjectX(i)+0.5*Sin(a),0,-SimulatedObjectY(i)-0.5*Cos(a),0,.2,b*Sin(a),0.015,-b*Cos(a),1,0,0,0,0,150,3)
		EndIf
	EndIf

	If SimulatedObjectData(i,2)=0
		TurnObjectTowardDirection(i,-PlayerX()+SimulatedObjectX(i),-PlayerY()+SimulatedObjectY(i),6,-SimulatedObjectYawAdjust(i))
	EndIf

End Function

Function ControlMirror(i)

	Obj.GameObject=LevelObjects(i)

	Select SimulatedObjectSubtype(i)

	Case 0	; inactive
		;ObjectActivationSpeed(i)=20
		;DeActivateObject(i)

	Case 1,2,3,4,5	; fire, ice, time, acid, home
		;ObjectActivationSpeed(i)=4
		;ActivateObject(i)
		EntityTexture Obj\Model\Entity,MirrorTexture(SimulatedObjectSubtype(i))
		PositionTexture MirrorTexture(SimulatedObjectSubtype(i)),Sin(Leveltimer/10.0),Cos(leveltimer/17.0)
		ScaleTexture mirrortexture(Simulatedobjectsubtype(i)),0.5+0.1*Sin(leveltimer/7.0),0.5+0.1*Cos(leveltimer/11.0)
		RotateTexture mirrortexture(Simulatedobjectsubtype(i)),leveltimer / 24.0

		;If Leveltimer Mod 400 = 0 playsoundfx(123,objectx(i),objectY(i))

	End Select

End Function

Function ControlGhost(i)

	Obj.GameObject=LevelObjects(i)
	Pos.GameObjectPosition=Obj\Position

	If SimulatedObjectTileTypeCollision(i)=0

		SimulatedObjectYawAdjust(i)=0
		;SimulatedObjectMovementSpeed(i)=5+5*ObjectData(i,1)
		;SimulatedPos\TileX=Floor(ObjectX(i))
		;SimulatedPos\TileY=Floor(ObjectY(i))
		SimulatedObjectTileTypeCollision(i)=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		;SimulatedObjectObjectTypeCollision(i)=2^1+2^3+2^6
		;SimulatedObjectMovementType(i)=0

	EndIf

	;If ObjectMovementTimer(i)>0
	;	TurnObjectTowardDirection(i,Pos\TileX2-Pos\TileX,Pos\TileY2-Pos\TileY,3,180)
	;Else
		TurnObjectTowardDirection(i,PlayerTileX()-Pos\TileX,PlayerTileY()-Pos\TileY,1,180)
	;EndIf

	If SimulatedObjectStatus(i)=0
		If Rand(0,100)<5
			If SimulatedObjectData(i,8)=1
				a=Rand(0,360)
				b#=Rnd(0.002,0.003)

				AddParticle(30,SimulatedObjectX(i)+0.2*Sin(a),0,-SimulatedObjectY(i)-0.2*Cos(a),0,.2,b*Sin(a),0.005,-b*Cos(a),1,0,0,0,0,80,3)

			EndIf
		EndIf

		If SimulationLevel>=2
			EntityAlpha Obj\Model\Entity,Float(SimulatedObjectData(i,9))/60.0
		EndIf
		If SimulatedObjectData(i,9)>0 Then SimulatedObjectData(i,9)=SimulatedObjectData(i,9)-1

		;ObjectMovementType(i)=0
		If Abs(SimulatedObjectX(i)-PlayerX())<=SimulatedObjectData(i,0) And Abs(SimulatedObjectY(i)-PlayerY())<=SimulatedObjectData(i,0)

			; in range
			SimulatedObjectStatus(i)=1
			;SoundPitch SoundFX(28),21000

			;PlaySoundFX(28,ObjectX(i),ObjectY(i))
		EndIf

	Else If SimulatedObjectStatus(i)=1
		SimulatedObjectData(i,8)=1
		;ObjectMovementTYpe(i)=13
		If SimulationLevel>=2
			EntityAlpha Obj\Model\Entity,Float(SimulatedObjectData(i,9))/60.0
		EndIf
		If SimulatedObjectData(i,9)<50 Then SimulatedObjectData(i,9)=SimulatedObjectData(i,9)+2

		If Abs(SimulatedObjectX(i)-PlayerX())>SimulatedObjectData(i,0) Or Abs(SimulatedObjectY(i)-PlayerY())>SimulatedObjectData(i,0)

			SimulatedObjectStatus(i)=0
			;PlaySoundFX(102,ObjectX(i),ObjectY(i))
		EndIf

	EndIf

End Function

Function ControlWraith(i)

	Obj.GameObject=LevelObjects(i)
	Pos.GameObjectPosition=Obj\Position

	If SimulatedObjectTileTypeCollision(i)=0

		SimulatedObjectYawAdjust(i)=0
		;ObjectMovementSpeed(i)=20+5*ObjectData(i,0)
		;Pos\TileX=Floor(ObjectX(i))
		;Pos\TileY=Floor(ObjectY(i))
		SimulatedObjectTileTypeCollision(i)=2^0+2^3+2^4+2^9+2^10+2^11+2^12+2^14
		;SimulatedObjectObjectTypeCollision(i)=2^1+2^3+2^6
		;SimulatedObjectMovementType(i)=0

	EndIf

	;If ObjectMovementTimer(i)>0
	;	TurnObjectTowardDirection(i,Pos\TileX2-Pos\TileX,Pos\TileY2-Pos\TileY,3,180)
	;Else
		TurnObjectTowardDirection(i,PlayerTileX()-Pos\TileX,PlayerTileY()-Pos\TileY,1,180)
	;EndIf

	If SimulatedObjectStatus(i)=0
		If SimulationLevel>=2
			EntityAlpha Obj\Model\Entity,Float(SimulatedObjectData(i,9))/60.0
		EndIf

		If SimulatedObjectData(i,9)>0 Then SimulatedObjectData(i,9)=SimulatedObjectData(i,9)-1

		;ObjectMovementType(i)=0
		If Abs(SimulatedObjectX(i)-PlayerX())<=SimulatedObjectData(i,0) And Abs(SimulatedObjectY(i)-PlayerY())<=SimulatedObjectData(i,0)

			; in range
			SimulatedObjectStatus(i)=1
			;PlaySoundFX(29,ObjectX(i),ObjectY(i))
		EndIf
		SimulatedObjectData(i,8)=0

	Else If SimulatedObjectStatus(i)=1

		If SimulationLevel>=2
			EntityAlpha Obj\Model\Entity,Float(SimulatedObjectData(i,9))/60.0
		EndIf

		If SimulatedObjectData(i,9)<50 Then SimulatedObjectData(i,9)=SimulatedObjectData(i,9)+2

		If SimulatedObjectData(i,8)<SimulatedObjectData(i,1)
			SimulatedObjectData(i,8)=SimulatedObjectData(i,8)+1
		 	If SimulatedObjectData(i,8)=SimulatedObjectData(i,1)-20
				Select SimulatedObjectData(i,2)

					Case 0
						part=25
					Case 1
						part=28
					Case 2
						part=27

				End Select

				For xx=1 To 30
					AddParticle(part,SimulatedObjectX(i)+Sin(xx*12),1.1,-SimulatedObjectY(i)+Cos(xx*12),Rand(0,360),.3,-0.05*Sin(xx*12),0,-0.05*Cos(xx*12),Rnd(0,2),0,0,0,0,30,4)
				Next
				;If SimulatedObjectData(i,2)=2
				;	SimulatedObjectData(i,6)=ObjectTileX2(PlayerObject)*100+50
				;	SimulatedObjectData(i,7)=ObjectTileY2(PlayerObject)*100+50
				;Else
				;	SimulatedObjectData(i,6)=(ObjectX(PlayerObject)*100+0)
				;	SimulatedObjectData(i,7)=ObjectY(PlayerObject)*100+0
				;EndIf

			EndIf
		Else
			;fire
;			If ObjectData(i,6)=-1
;				If ObjectData(i,2)=2
;					ObjectData(i,6)=ObjectTileX2(PlayerObject)*100+50
;
;					ObjectData(i,7)=ObjectTileY2(PlayerObject)*100+50
;
;				Else
;					ObjectData(i,6)=(ObjectX(PlayerObject)*100+0)
;					ObjectData(i,7)=ObjectY(PlayerObject)*100+0
;				EndIf
;
;			EndIf
			SimulatedObjectData(i,8)=0

;			SimulatedObjectData(i,6)=-1

		EndIf

		If Abs(SimulatedObjectX(i)-PlayerX())>SimulatedObjectData(i,0) Or Abs(SimulatedObjectY(i)-PlayerY())>SimulatedObjectData(i,0)

			SimulatedObjectData(i,8)=0

			SimulatedObjectStatus(i)=0
			;PlaySoundFX(102,ObjectX(i),ObjectY(i))
		EndIf

	EndIf

End Function

Function ControlPickUpItem(i)

	SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+2) Mod 360

	SimulatedObjectPitch2(i)=10*Sin(LevelTimer Mod 360)

End Function

Function ControlUsedItem(i)

	SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+10) Mod 360

	If SimulatedObjectTimer(i)=120
		For j=0 To 29
			k=Rand(0,360)
			AddParticle(23,SimulatedObjectX(i)+1.8*Sin(k),SimulatedObjectZ(i),-SimulatedObjectY(i)-1.8*Cos(k),0,.4,-0.06*Sin(k),0,0.06*Cos(k),5,0,0,0,0,30,3)
		Next
	EndIf

	SimulatedObjectTimer(i)=SimulatedObjectTimer(i)-1

End Function

Function ControlConveyorLead(i)

	Obj.GameObject=LevelObjects(i)

	Select (SimulatedObjectData(i,2)+40) Mod 4
	Case 0
		dx=0
		dy=-1
	Case 1
		dx=1
		dy=0
	Case 2
		dx=0
		dy=1
	Case 3
		dx=-1
		dy=0
	End Select

	If SimulatedObjectData(i,4)=3 TurnobjectTowardDirection(i,dx,dy,20,180)

	If SimulatedObjectData(i,4)=4 SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+SimulatedObjectData(i,9)/10.0) Mod 360

	If Obj\Attributes\MovementTimer=0
		size#=1.0
	Else
		size#=Float(1001-Obj\Attributes\MovementTimer)/Float(1001)
		If size<0 Then size=0.0
		size=1.0-size
	EndIf
	If SimulatedObjectData(i,4)=4
		SimulatedObjectXScale(i)=1.5*size*(0.9+0.095*Sin((leveltimer*4) Mod 360))
		SimulatedObjectYScale(i)=1.5*size*(0.9+0.095*Sin((leveltimer*4) Mod 360))
		SimulatedObjectZScale(i)=1.5*size
	Else
		SimulatedObjectXScale(i)=size*(0.9+0.095*Sin((leveltimer*4) Mod 360))
		SimulatedObjectYScale(i)=size*(0.9+0.095*Sin((leveltimer*4) Mod 360))
		SimulatedObjectZScale(i)=size
	EndIf

End Function

Function ControlConveyorTail(i)

	If SimulatedObjectActive(i)=1001

		Select (SimulatedObjectData(i,2)+40) Mod 4
		Case 0
			dx=0
			dy=-1
		Case 1
			dx=1
			dy=0
		Case 2
			dx=0
			dy=1
		Case 3
			dx=-1
			dy=0
		End Select

		If SimulatedObjectData(i,4)=3 TurnobjectTowardDirection(i,dx,dy,6,180)
		If SimulatedObjectData(i,4)=4 SimulatedObjectYaw(i)=(SimulatedObjectYaw(i)+SimulatedObjectData(i,9)/10.0) Mod 360

		If SimulatedObjectData(i,4)=4
			SimulatedObjectXScale(i)=1.5*(0.9+0.095*Sin((leveltimer*4) Mod 360))
			SimulatedObjectYScale(i)=1.5*(0.9+0.095*Sin((leveltimer*4) Mod 360))
		Else
			SimulatedObjectXScale(i)=.8*(0.9+0.095*Sin((leveltimer*4) Mod 360))
			SimulatedObjectYScale(i)=.8*(0.9+0.095*Sin((leveltimer*4) Mod 360))
		EndIf

		SimulatedObjectZScale(i)=1
	EndIf

End Function