Type woi ; WOP = Wonderland Object Preset, WOI = Wonderland Object Instance
	; Original comments by Patrick kept
	; NOT SAVED IN WOP\WLV
	Field HatEntity,HatTexture
	Field AccEntity,AccTexture
	Field Exists ; true or false - used to delete objects at end of round
	Field Entity ; the entity
	Field Texture ; what texture is currently applied
	; SAVED IN WOP\WLV
	Field ModelName$
	Field TextureName$
	Field XScale#
	Field YScale#
	Field ZScale#
	Field XAdjust#
	Field YAdjust#
	Field ZAdjust#
	Field PitchAdjust#
	Field YawAdjust#
	Field RollAdjust#
	; floats of x/y/z position on field
	Field X#
	Field Y#
	Field Z#
	; from last frame
	Field OldX#
	Field OldY#
	Field OldZ#
	Field DX#
	Field DY#
	Field DZ#
	Field Pitch#
	Field Yaw#
	Field Roll#
	Field Pitch2#
	Field Yaw2#
	Field Roll2#
	Field XGoal#
	Field YGoal#
	Field ZGoal#
	Field MovementType
	Field MovementTypeData
	Field Speed#
	Field Radius#
	Field RadiusType ; 0: circle, 1: square
	Field Data10 ; old editor: CollisionPower
	Field PushDX#
	Field PushDY#
	Field AttackPower
	Field DefensePower
	Field DestructionType
	Field ID
	Field ObjType
	Field SubType
	Field Active ; 0: inactive, 1001: active, odd: activating, even: deactivating
	Field LastActive
	Field ActivationType ; how does Active manifest itself (fade/shrink/etc)
	Field ActivationSpeed ; must be even number, 2+: how fast ObjectActive is changed
	Field Status
	Field Timer
	Field TimerMax1
	Field TimerMax2
	Field Teleportable
	Field ButtonPush
	Field WaterReact	; NOTE: THESE ACTUALLY ARE NOT IMPLEMENTED
						; 0-9:	ignore water
						; 10-19:	can't enter water
						; 20-29: destroyed by water
						; 30-39: float (with final height adjustment .0 to .9)
						; 40-49: sink (with final height adjustment .0 to .9)
						; 50-59: drift at depth -2 (with final height adjustment .0 to .9)
						; 60-69: drift at depth -3 (with final height adjustment .0 to .9)
						; 70-79: drift at depth -4 (with final height adjustment .0 to .9)
						; 80-89: drift at depth -5 (with final height adjustment .0 to .9)
	Field Telekinesisable
	Field Freezable ; 0: no, 1: yes, 2: yes, but currently disabled (e.g. when frozen)
	Field Reactive
	; for linked objects, e.g. iceblocks
	Field Child
	Field Parent
	Field ObjData[9]
	Field TextData$[3]
	Field Talkable
	Field CurrentAnim
	Field StandardAnim
	Field TileX
	Field TileY
	Field TileX2
	Field TileY2
	Field MovementTimer ; old editor: FutureInt8
	Field MovementSpeed
	Field MoveXGoal ; old editor: FutureInt10
	Field MoveYGoal ; old editor: FutureInt11
	Field TileTypeCollision ; old editor: FutureInt12
	Field ObjectTypeCollision ; old editor: FutureInt13
	Field Caged ; old editor: FutureInt14
	Field Dead ; old editor: FutureInt15
	Field DeadTimer ; old editor: FutureInt16
	Field Exclamation
	Field Shadow
	Field Linked
	Field LinkBack
	Field Flying ; old editor: FutureInt21
	Field Frozen
	Field Indigo ; old editor: FutureInt23
	Field FutureInt24
	Field FutureInt25
	Field ScaleAdjust#
	Field ScaleXAdjust# ; old editor: FutureFloat2#
	Field ScaleYAdjust# ; old editor: FutureFloat3#
	Field ScaleZAdjust# ; old editor: FutureFloat4#
	Field FutureFloat5#
	Field FutureFloat6#
	Field FutureFloat7#
	Field FutureFloat8#
	Field FutureFloat9#
	Field FutureFloat10#
	Field FutureString1$
	Field FutureString2$
End Type

Const MaxNofObjects=1024 ; 4 of these slots are needed for player + accessory1 + accessory2 + shadow
Global NofObjects=0
Dim GameObject.woi(MaxNofObjects)
Global MyCurrentObject.woi
MyCurrentObject = New woi