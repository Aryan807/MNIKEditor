; TYPE CONVERTOR
; CONVERT MNIKGAMEOBJECT TO WOI
; WORST CONVERSION EVER!!!

Function BuildWOIObjectModel(MNIK.GameObject)
	openwaObject.woi = new woi
	If MyCurrentObject <> Null
		Delete MyCurrentObject
	EndIf
	MyCurrentObject = new woi
	FillWOIObject(openwaObject, MNIK)
	FillWOIObject(MyCurrentObject, CurrentObject)
	CreateObjectModel(openwaObject, False, True, False)
	; refill all!
	FillMNIKObject(MNIK, openwaObject)
	;FillMNIKObject(CurrentObject, MyCurrentObject)
	Delete openwaObject
	Delete MyCurrentObject
End Function

Function FillWOIObject(openwa.woi, MNIK.GameObject)
	openwa\Entity=MNIK\Model\Entity
	openwa\Texture=MNIK\Model\Texture

    openwa\HatEntity=MNIK\Model\HatEntity
    openwa\HatTexture=MNIK\Model\HatTexture
    openwa\AccEntity=MNIK\Model\AccEntity
    openwa\HatTexture=MNIK\Model\HatTexture

	openwa\X=MNIK\Position\X
	openwa\Y=MNIK\Position\Y
	openwa\Z=MNIK\Position\Z
	openwa\OldX=MNIK\Position\OldX
	openwa\OldY=MNIK\Position\OldY
	openwa\OldZ=MNIK\Position\OldZ

	openwa\TileX=MNIK\Position\TileX
	openwa\TileY=MNIK\Position\TileY
	openwa\TileX2=MNIK\Position\TileX2
	openwa\TileY2=MNIK\Position\TileY2

	openwa\ModelName$=MNIK\Attributes\ModelName$
	openwa\TextureName$=MNIK\Attributes\TexName$
	openwa\XScale=MNIK\Attributes\XScale
	openwa\YScale=MNIK\Attributes\YScale
	openwa\ZScale=MNIK\Attributes\ZScale
	openwa\XAdjust=MNIK\Attributes\XAdjust
	openwa\YAdjust=MNIK\Attributes\YAdjust
	openwa\ZAdjust=MNIK\Attributes\ZAdjust
	openwa\PitchAdjust=MNIK\Attributes\PitchAdjust
	openwa\YawAdjust=MNIK\Attributes\YawAdjust
	openwa\RollAdjust=MNIK\Attributes\RollAdjust
	;openwa\Exists=MNIK\Attributes\Exists	
	openwa\DX=MNIK\Attributes\DX
	openwa\DY=MNIK\Attributes\DY
	openwa\DZ=MNIK\Attributes\DZ
	openwa\Pitch=MNIK\Attributes\Pitch
	openwa\Yaw=MNIK\Attributes\Yaw
	openwa\Roll=MNIK\Attributes\Roll
	openwa\Pitch2=MNIK\Attributes\Pitch2
	openwa\Yaw2=MNIK\Attributes\Yaw2
	openwa\Roll2=MNIK\Attributes\Roll2
	openwa\XGoal=MNIK\Attributes\XGoal
	openwa\YGoal=MNIK\Attributes\YGoal
	openwa\ZGoal=MNIK\Attributes\ZGoal
	openwa\MovementType=MNIK\Attributes\MovementType
	openwa\MovementTypeData=MNIK\Attributes\MovementTypeData
	openwa\Speed=MNIK\Attributes\Speed
	openwa\Radius=MNIK\Attributes\Radius
	openwa\RadiusType=MNIK\Attributes\RadiusType
	openwa\Data10=MNIK\Attributes\Data10
	openwa\PushDX=MNIK\Attributes\PushDX
	openwa\PushDY=MNIK\Attributes\PushDY
	openwa\AttackPower=MNIK\Attributes\AttackPower
	openwa\DefensePower=MNIK\Attributes\DefensePower
	openwa\DestructionType=MNIK\Attributes\DestructionType
	openwa\ID=MNIK\Attributes\ID
	openwa\ObjType=MNIK\Attributes\LogicType
	openwa\SubType=MNIK\Attributes\LogicSubType
	openwa\Active=MNIK\Attributes\Active
	openwa\LastActive=MNIK\Attributes\LastActive
	openwa\ActivationType=MNIK\Attributes\ActivationType
	openwa\ActivationSpeed=MNIK\Attributes\ActivationSpeed
	openwa\Status=MNIK\Attributes\Status
	openwa\Timer=MNIK\Attributes\Timer
	openwa\TimerMax1=MNIK\Attributes\TimerMax1
	openwa\TimerMax2=MNIK\Attributes\TimerMax2
	openwa\Teleportable=MNIK\Attributes\Teleportable
	openwa\ButtonPush=MNIK\Attributes\ButtonPush
	openwa\WaterReact=MNIK\Attributes\WaterReact
	openwa\Telekinesisable=MNIK\Attributes\Telekinesisable
	openwa\Freezable=MNIK\Attributes\Freezable
	openwa\Reactive=MNIK\Attributes\Reactive
	openwa\Child=MNIK\Attributes\Child
	openwa\Parent=MNIK\Attributes\Parent
	openwa\ObjData[0]=MNIK\Attributes\Data0
	openwa\ObjData[1]=MNIK\Attributes\Data1
	openwa\ObjData[2]=MNIK\Attributes\Data2
	openwa\ObjData[3]=MNIK\Attributes\Data3
	openwa\ObjData[4]=MNIK\Attributes\Data4
	openwa\ObjData[5]=MNIK\Attributes\Data5
	openwa\ObjData[6]=MNIK\Attributes\Data6
	openwa\ObjData[7]=MNIK\Attributes\Data7
	openwa\ObjData[8]=MNIK\Attributes\Data8
	openwa\ObjData[9]=MNIK\Attributes\Data9
	openwa\TextData$[0]=MNIK\Attributes\TextData0
	openwa\TextData$[1]=MNIK\Attributes\TextData1
	openwa\TextData$[2]=MNIK\Attributes\TextData2
	openwa\TextData$[3]=MNIK\Attributes\TextData3
	openwa\Talkable=MNIK\Attributes\Talkable
	openwa\CurrentAnim=MNIK\Attributes\CurrentAnim
	openwa\StandardAnim=MNIK\Attributes\StandardAnim
	openwa\MovementTimer=MNIK\Attributes\MovementTimer
	openwa\MovementSpeed=MNIK\Attributes\MovementSpeed
	openwa\MoveXGoal=MNIK\Attributes\MoveXGoal
	openwa\MoveYGoal=MNIK\Attributes\MoveYGoal
	openwa\TileTypeCollision=MNIK\Attributes\TileTypeCollision
	openwa\ObjectTypeCollision=MNIK\Attributes\ObjectTypeCollision
	openwa\Caged=MNIK\Attributes\Caged
	openwa\Dead=MNIK\Attributes\Dead
	openwa\DeadTimer=MNIK\Attributes\DeadTimer
	openwa\Exclamation=MNIK\Attributes\Exclamation
	openwa\Shadow=MNIK\Attributes\Shadow
	openwa\Linked=MNIK\Attributes\Linked
	openwa\LinkBack=MNIK\Attributes\LinkBack
	openwa\Flying=MNIK\Attributes\Flying
	openwa\Frozen=MNIK\Attributes\Frozen
	openwa\Indigo=MNIK\Attributes\Indigo
	openwa\FutureInt24=MNIK\Attributes\FutureInt24
	openwa\FutureInt25=MNIK\Attributes\FutureInt25
	openwa\ScaleAdjust=MNIK\Attributes\ScaleAdjust
	openwa\ScaleXAdjust=MNIK\Attributes\ScaleXAdjust
	openwa\ScaleYAdjust=MNIK\Attributes\ScaleYAdjust
	openwa\ScaleZAdjust=MNIK\Attributes\ScaleZAdjust
	openwa\FutureFloat5=MNIK\Attributes\FutureFloat5
	openwa\FutureFloat6=MNIK\Attributes\FutureFloat6
	openwa\FutureFloat7=MNIK\Attributes\FutureFloat7
	openwa\FutureFloat8=MNIK\Attributes\FutureFloat8
	openwa\FutureFloat9=MNIK\Attributes\FutureFloat9
	openwa\FutureFloat10=MNIK\Attributes\FutureFloat10
	openwa\FutureString1$=MNIK\Attributes\FutureString1$
	openwa\FutureString2$=MNIK\Attributes\FutureString1$
End Function

Function FillMNIKObject(MNIK.GameObject, openwa.woi)
	MNIK\Model\Entity=openwa\Entity
	MNIK\Model\Texture=openwa\Texture

    MNIK\Model\HatEntity=openwa\HatEntity
    MNIK\Model\HatTexture=openwa\HatTexture
    MNIK\Model\AccEntity=openwa\AccEntity
    MNIK\Model\HatTexture=openwa\HatTexture

	MNIK\Position\X=openwa\X
	MNIK\Position\Y=openwa\Y
	MNIK\Position\Z=openwa\Z
	MNIK\Position\OldX=openwa\OldX
	MNIK\Position\OldY=openwa\OldY
	MNIK\Position\OldZ=openwa\OldZ

	MNIK\Position\TileX=openwa\TileX
	MNIK\Position\TileY=openwa\TileY
	MNIK\Position\TileX2=openwa\TileX2
	MNIK\Position\TileY2=openwa\TileY2

	MNIK\Attributes\ModelName$=openwa\ModelName$
	MNIK\Attributes\TexName$=openwa\TextureName$
	MNIK\Attributes\XScale=openwa\XScale
	MNIK\Attributes\YScale=openwa\YScale
	MNIK\Attributes\ZScale=openwa\ZScale
	MNIK\Attributes\XAdjust=openwa\XAdjust
	MNIK\Attributes\YAdjust=openwa\YAdjust
	MNIK\Attributes\ZAdjust=openwa\ZAdjust
	MNIK\Attributes\PitchAdjust=openwa\PitchAdjust
	MNIK\Attributes\YawAdjust=openwa\YawAdjust
	MNIK\Attributes\RollAdjust=openwa\RollAdjust
	;MNIK\Attributes\Exists=openwa\Exists	
	MNIK\Attributes\DX=openwa\DX
	MNIK\Attributes\DY=openwa\DY
	MNIK\Attributes\DZ=openwa\DZ
	MNIK\Attributes\Pitch=openwa\Pitch
	MNIK\Attributes\Yaw=openwa\Yaw
	MNIK\Attributes\Roll=openwa\Roll
	MNIK\Attributes\Pitch2=openwa\Pitch2
	MNIK\Attributes\Yaw2=openwa\Yaw2
	MNIK\Attributes\Roll2=openwa\Roll2
	MNIK\Attributes\XGoal=openwa\XGoal
	MNIK\Attributes\YGoal=openwa\YGoal
	MNIK\Attributes\ZGoal=openwa\ZGoal
	MNIK\Attributes\MovementType=openwa\MovementType
	MNIK\Attributes\MovementTypeData=openwa\MovementTypeData
	MNIK\Attributes\Speed=openwa\Speed
	MNIK\Attributes\Radius=openwa\Radius
	MNIK\Attributes\RadiusType=openwa\RadiusType
	MNIK\Attributes\Data10=openwa\Data10
	MNIK\Attributes\PushDX=openwa\PushDX
	MNIK\Attributes\PushDY=openwa\PushDY
	MNIK\Attributes\AttackPower=openwa\AttackPower
	MNIK\Attributes\DefensePower=openwa\DefensePower
	MNIK\Attributes\DestructionType=openwa\DestructionType
	MNIK\Attributes\ID=openwa\ID
	MNIK\Attributes\LogicType=openwa\ObjType
	MNIK\Attributes\LogicSubType=openwa\SubType
	MNIK\Attributes\Active=openwa\Active
	MNIK\Attributes\LastActive=openwa\LastActive
	MNIK\Attributes\ActivationType=openwa\ActivationType
	MNIK\Attributes\ActivationSpeed=openwa\ActivationSpeed
	MNIK\Attributes\Status=openwa\Status
	MNIK\Attributes\Timer=openwa\Timer
	MNIK\Attributes\TimerMax1=openwa\TimerMax1
	MNIK\Attributes\TimerMax2=openwa\TimerMax2
	MNIK\Attributes\Teleportable=openwa\Teleportable
	MNIK\Attributes\ButtonPush=openwa\ButtonPush
	MNIK\Attributes\WaterReact=openwa\WaterReact
	MNIK\Attributes\Telekinesisable=openwa\Telekinesisable
	MNIK\Attributes\Freezable=openwa\Freezable
	MNIK\Attributes\Reactive=openwa\Reactive
	MNIK\Attributes\Child=openwa\Child
	MNIK\Attributes\Parent=openwa\Parent
	MNIK\Attributes\Data0=openwa\ObjData[0]
	MNIK\Attributes\Data1=openwa\ObjData[1]
	MNIK\Attributes\Data2=openwa\ObjData[2]
	MNIK\Attributes\Data3=openwa\ObjData[3]
	MNIK\Attributes\Data4=openwa\ObjData[4]
	MNIK\Attributes\Data5=openwa\ObjData[5]
	MNIK\Attributes\Data6=openwa\ObjData[6]
	MNIK\Attributes\Data7=openwa\ObjData[7]
	MNIK\Attributes\Data8=openwa\ObjData[8]
	MNIK\Attributes\Data8=openwa\ObjData[9]
	MNIK\Attributes\TextData0=openwa\TextData$[0]
	MNIK\Attributes\TextData1=openwa\TextData$[1]
	MNIK\Attributes\TextData2=openwa\TextData$[2]
	MNIK\Attributes\TextData3=openwa\TextData$[3]
	MNIK\Attributes\Talkable=openwa\Talkable
	MNIK\Attributes\CurrentAnim=openwa\CurrentAnim
	MNIK\Attributes\StandardAnim=openwa\StandardAnim
	MNIK\Attributes\MovementTimer=openwa\MovementTimer
	MNIK\Attributes\MovementSpeed=openwa\MovementSpeed
	MNIK\Attributes\MoveXGoal=openwa\MoveXGoal
	MNIK\Attributes\MoveYGoal=openwa\MoveYGoal
	MNIK\Attributes\TileTypeCollision=openwa\TileTypeCollision
	MNIK\Attributes\ObjectTypeCollision=openwa\ObjectTypeCollision
	MNIK\Attributes\Caged=openwa\Caged
	MNIK\Attributes\Dead=openwa\Dead
	MNIK\Attributes\DeadTimer=openwa\DeadTimer
	MNIK\Attributes\Exclamation=openwa\Exclamation
	MNIK\Attributes\Shadow=openwa\Shadow
	MNIK\Attributes\Linked=openwa\Linked
	MNIK\Attributes\LinkBack=openwa\LinkBack
	MNIK\Attributes\Flying=openwa\Flying
	MNIK\Attributes\Frozen=openwa\Frozen
	MNIK\Attributes\Indigo=openwa\Indigo
	MNIK\Attributes\FutureInt24=openwa\FutureInt24
	MNIK\Attributes\FutureInt25=openwa\FutureInt25
	MNIK\Attributes\ScaleAdjust=openwa\ScaleAdjust
	MNIK\Attributes\ScaleXAdjust=openwa\ScaleXAdjust
	MNIK\Attributes\ScaleYAdjust=openwa\ScaleYAdjust
	MNIK\Attributes\ScaleZAdjust=openwa\ScaleZAdjust
	MNIK\Attributes\FutureFloat5=openwa\FutureFloat5
	MNIK\Attributes\FutureFloat6=openwa\FutureFloat6
	MNIK\Attributes\FutureFloat7=openwa\FutureFloat7
	MNIK\Attributes\FutureFloat8=openwa\FutureFloat8
	MNIK\Attributes\FutureFloat9=openwa\FutureFloat9
	MNIK\Attributes\FutureFloat10=openwa\FutureFloat10
	MNIK\Attributes\FutureString1$=openwa\FutureString1$
	MNIK\Attributes\FutureString2$=openwa\FutureString1$
End Function