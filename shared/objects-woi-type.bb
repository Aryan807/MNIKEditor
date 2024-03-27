Function FillObjectWithDefaults(this.woi)
	this\Exists=True
	this\Entity=0
	this\Texture=0
	this\ModelName$=""
	this\TextureName$=""
	this\XScale=1
	this\YScale=1
	this\ZScale=1
	this\XAdjust=0
	this\YAdjust=0
	this\ZAdjust=0
	this\PitchAdjust=0
	this\YawAdjust=0
	this\RollAdjust=0
	this\X=0
	this\Y=0
	this\Z=0
	this\OldX=-999
	this\OldY=-999
	this\OldZ=-999
	this\DX=0
	this\DY=0
	this\DZ=0
	this\Pitch=0
	this\Yaw=0
	this\Roll=0
	this\Pitch2=0
	this\Yaw2=0
	this\Roll2=0
	this\XGoal=0
	this\YGoal=0
	this\ZGoal=0
	this\MovementType=0
	this\MovementTypeData=0
	this\Speed=0
	this\Radius=0
	this\RadiusType=0
	this\Data10=0
	this\PushDX=0
	this\PushDY=0
	this\AttackPower=0
	this\DefensePower=0
	this\DestructionType=0
	this\ID=-1
	this\ObjType=0
	this\SubType=0
	this\Active=0
	this\LastActive=0
	this\ActivationType=0
	this\ActivationSpeed=0
	this\Status=0
	this\Timer=0
	this\TimerMax1=0
	this\TimerMax2=0
	this\Teleportable=False
	this\ButtonPush=False
	this\WaterReact=0
	this\Telekinesisable=0
	this\Freezable=0
	this\Reactive=True
	this\Child=-1
	this\Parent=-1
	For k=0 To 9
		this\ObjData[k]=0
	Next
	For k=0 To 3
		this\TextData[k]=""
	Next
	this\Talkable=0
	this\CurrentAnim=0
	this\StandardAnim=0
	this\TileX=0
	this\TileY=0
	this\TileX2=0
	this\TileY2=0
	this\MovementTimer=0
	this\MovementSpeed=0
	this\MoveXGoal=0
	this\MoveYGoal=0
	this\TileTypeCollision=0
	this\ObjectTypeCollision=0
	this\Caged=0
	this\Dead=0
	this\DeadTimer=0
	this\Exclamation=0
	this\Shadow=0
	this\Linked=-1
	this\LinkBack=-1
	this\Flying=0
	this\Frozen=0
	this\Indigo=0
	this\FutureInt24=0
	this\FutureInt25=0
	this\ScaleAdjust=0
	this\ScaleXAdjust=1.0
	this\ScaleYAdjust=1.0
	this\ScaleZAdjust=1.0
	this\FutureFloat5=0
	this\FutureFloat6=0
	this\FutureFloat7=0
	this\FutureFloat8=0
	this\FutureFloat9=0
	this\FutureFloat10=0
	this\FutureString1$=0
	this\FutureString2$=0
End Function

Function FillObjectFromFile(this.woi,file)
	this\ModelName$=Replace$(ReadString(file),Chr$(92),Chr$(47))
	this\TextureName$=Replace$(ReadString(file),Chr$(92),Chr$(47))
	this\XScale=ReadFloat(file)
	this\YScale=ReadFloat(file)
	this\ZScale=ReadFloat(file)
	this\XAdjust=ReadFloat(file)
	this\YAdjust=ReadFloat(file)
	this\ZAdjust=ReadFloat(file)
	this\PitchAdjust=ReadFloat(file)
	this\YawAdjust=ReadFloat(file)
	this\RollAdjust=ReadFloat(file)
	this\X=ReadFloat(file)
	this\Y=ReadFloat(file)
	this\Z=ReadFloat(file)
	this\OldX=ReadFloat(file)
	this\OldY=ReadFloat(file)
	this\OldZ=ReadFloat(file)
	this\DX=ReadFloat(file)
	this\DY=ReadFloat(file)
	this\DZ=ReadFloat(file)
	this\Pitch=ReadFloat(file)
	this\Yaw=ReadFloat(file)
	this\Roll=ReadFloat(file)
	this\Pitch2=ReadFloat(file)
	this\Yaw2=ReadFloat(file)
	this\Roll2=ReadFloat(file)
	this\XGoal=ReadFloat(file)
	this\YGoal=ReadFloat(file)
	this\ZGoal=ReadFloat(file)
	this\MovementType=ReadInt(file)
	this\MovementTypeData=ReadInt(file)
	this\Speed=ReadFloat(file)
	this\Radius=ReadFloat(file)
	this\RadiusType=ReadInt(file)
	this\Data10=ReadInt(file)
	this\PushDX=ReadFloat(file)
	this\PushDY=ReadFloat(file)
	this\AttackPower=ReadInt(file)
	this\DefensePower=ReadInt(file)
	this\DestructionType=ReadInt(file)
	this\ID=ReadInt(file)
	this\ObjType=ReadInt(file)
	this\SubType=ReadInt(file)
	this\Active=ReadInt(file)
	this\LastActive=ReadInt(file)
	this\ActivationType=ReadInt(file)
	this\ActivationSpeed=ReadInt(file)
	this\Status=ReadInt(file)
	this\Timer=ReadInt(file)
	this\TimerMax1=ReadInt(file)
	this\TimerMax2=ReadInt(file)
	this\Teleportable=ReadInt(file)
	this\ButtonPush=ReadInt(file)
	this\WaterReact=ReadInt(file)
	this\Telekinesisable=ReadInt(file)
	this\Freezable=ReadInt(file)
	this\Reactive=ReadInt(file)
	this\Child=ReadInt(file)
	this\Parent=ReadInt(file)
	For k=0 To 9
		this\ObjData[k]=ReadInt(file)
	Next
	For k=0 To 3
		this\TextData$[k]=Replace$(ReadString(file),Chr$(92),Chr$(47))
	Next
	
	this\Talkable=ReadInt(file)
	this\CurrentAnim=ReadInt(file)
	this\StandardAnim=ReadInt(file)
	this\TileX=ReadInt(file)
	this\TileY=ReadInt(file)
	this\TileX2=ReadInt(file)
	this\TileY2=ReadInt(file)
	this\MovementTimer=ReadInt(file)
	this\MovementSpeed=ReadInt(file)
	this\MoveXGoal=ReadInt(file)
	this\MoveYGoal=ReadInt(file)
	this\TileTypeCollision=ReadInt(file)
	this\ObjectTypeCollision=ReadInt(file)
	this\Caged=ReadInt(file)
	this\Dead=ReadInt(file)
	this\DeadTimer=ReadInt(file)
	this\Exclamation=ReadInt(file)
	this\Shadow=ReadInt(file)
	this\Linked=ReadInt(file)
	this\LinkBack=ReadInt(file)
	this\Flying=ReadInt(file)
	this\Frozen=ReadInt(file)
	this\Indigo=ReadInt(file)
	this\FutureInt24=ReadInt(file)
	this\FutureInt25=ReadInt(file)
	this\ScaleAdjust=ReadFloat(file)
	this\ScaleXAdjust=ReadFloat(file)	
	this\ScaleYAdjust=ReadFloat(file)
	this\ScaleZAdjust=ReadFloat(file)
	this\FutureFloat5=ReadFloat(file)
	this\FutureFloat6=ReadFloat(file)
	this\FutureFloat7=ReadFloat(file)	
	this\FutureFloat8=ReadFloat(file)
	this\FutureFloat9=ReadFloat(file)
	this\FutureFloat10=ReadFloat(file)
	this\FutureString1$=ReadString(file)
	this\FutureString2$=ReadString(file)
End Function

Function WriteObjectToFile(this.woi,file,writeLink=True)
	WriteString file,this\ModelName$
	WriteString file,this\TextureName$
	WriteFloat file,this\XScale
	WriteFloat file,this\YScale
	WriteFloat file,this\ZScale
	WriteFloat file,this\XAdjust
	WriteFloat file,this\YAdjust
	WriteFloat file,this\ZAdjust
	WriteFloat file,this\PitchAdjust
	WriteFloat file,this\YawAdjust
	WriteFloat file,this\RollAdjust
	WriteFloat file,this\X
	WriteFloat file,this\Y
	WriteFloat file,this\Z
	WriteFloat file,this\OldX
	WriteFloat file,this\OldY
	WriteFloat file,this\OldZ
	WriteFloat file,this\DX
	WriteFloat file,this\DY
	WriteFloat file,this\DZ
	WriteFloat file,this\Pitch
	WriteFloat file,this\Yaw
	WriteFloat file,this\Roll
	WriteFloat file,this\Pitch2
	WriteFloat file,this\Yaw2
	WriteFloat file,this\Roll2
	WriteFloat file,this\XGoal
	WriteFloat file,this\YGoal
	WriteFloat file,this\ZGoal
	WriteInt file,this\MovementType
	WriteInt file,this\MovementTypeData
	WriteFloat file,this\Speed
	WriteFloat file,this\Radius
	WriteInt file,this\RadiusType
	WriteInt file,this\Data10
	WriteFloat file,this\PushDX
	WriteFloat file,this\PushDY
	WriteInt file,this\AttackPower
	WriteInt file,this\DefensePower
	WriteInt file,this\DestructionType
	WriteInt file,this\ID
	WriteInt file,this\ObjType
	WriteInt file,this\SubType
	WriteInt file,this\Active
	WriteInt file,this\LastActive
	WriteInt file,this\ActivationType
	WriteInt file,this\ActivationSpeed
	WriteInt file,this\Status
	WriteInt file,this\Timer
	WriteInt file,this\TimerMax1
	WriteInt file,this\TimerMax2
	WriteInt file,this\Teleportable
	WriteInt file,this\ButtonPush
	WriteInt file,this\WaterReact
	WriteInt file,this\Telekinesisable
	WriteInt file,this\Freezable
	WriteInt file,this\Reactive
	WriteInt file,this\Child
	WriteInt file,this\Parent
	For k=0 To 9
		WriteInt file,this\ObjData[k]
	Next
	For k=0 To 3
		WriteString file,this\TextData[k]
	Next
	WriteInt file,this\Talkable
	WriteInt file,this\CurrentAnim
	WriteInt file,this\StandardAnim
	WriteInt file,this\TileX
	WriteInt file,this\TileY
	WriteInt file,this\TileX2
	WriteInt file,this\TileY2
	WriteInt file,this\MovementTimer
	WriteInt file,this\MovementSpeed
	WriteInt file,this\MoveXGoal
	WriteInt file,this\MoveYGoal
	WriteInt file,this\TileTypeCollision
	WriteInt file,this\ObjectTypeCollision
	WriteInt file,this\Caged
	WriteInt file,this\Dead
	WriteInt file,this\DeadTimer
	WriteInt file,this\Exclamation
	WriteInt file,this\Shadow
	If writeLink=True
		WriteInt file,this\Linked
		WriteInt file,this\LinkBack
	Else
		WriteInt file,-1
		WriteInt file,-1
	EndIf
	WriteInt file,this\Flying
	WriteInt file,this\Frozen
	WriteInt file,this\Indigo
	WriteInt file,this\FutureInt24
	WriteInt file,this\FutureInt25
	WriteFloat file,this\ScaleAdjust
	WriteFloat file,this\ScaleXAdjust
	WriteFloat file,this\ScaleYAdjust
	WriteFloat file,this\ScaleZAdjust
	WriteFloat file,this\FutureFloat5
	WriteFloat file,this\FutureFloat6
	WriteFloat file,this\FutureFloat7
	WriteFloat file,this\FutureFloat8
	WriteFloat file,this\FutureFloat9
	WriteFloat file,this\FutureFloat10
	WriteString file,this\FutureString1$
	WriteString file,this\FutureString2$
End Function

Function CreateNewObject()
	Dest=NofObjects
	If GameObject(Dest)<>Null
		Delete GameObject(Dest)
	EndIf
	GameObject(Dest) = New woi
	FillObjectWithDefaults(GameObject(Dest))
	NofObjects=NofObjects+1
	Return Dest
End Function

Function GetGameObjectIndex(this.woi)
	For i=0 To NofObjects-1
		If GameObject(i)=this
			Return i
		EndIf
	Next
	Return -1
End Function