;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;   Windows clipboard
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; libs: user32.decls,kernel32.decls
;
; Sources: 
; http://www.bettiesart.com/tc/blitz/lib.html 
; https://www.syntaxbomb.com/index.php?topic=1740.0

Function SetClipboard( txt$ )
	If txt = "" Then Return 0
	str_size=Len(txt)+1
	st=CreateBank(str_size)
	For i=1 To str_size
		PokeByte st,i-1,Asc(Mid(txt,i,1))
	Next
	hDIB=api_GlobalAlloc(66,str_size)
	If Not hDIB ;major bummer if we couldn't get memory block
		FreeBank st
		Return -1 ;fail code
	EndIf
	pbmi=api_GlobalLock(hDIB)
	api_RtlCopyMemory(pbmi,st,str_size)
	api_GlobalUnlock(hDIB)
	If api_OpenClipboard( 0 )
		api_EmptyClipboard
		api_SetClipboardData( 1 , hDIB )
		api_CloseClipboard
	EndIf
	Return 1
End Function
;
Function GetClipboard$()
	Local txt$
    
	If api_OpenClipboard( 0 )
		If api_ExamineClipboard( 1 )
			txt = api_GetClipboardData( 1 )
		EndIf
		api_CloseClipboard
	EndIf
    
	Return txt
End Function
;

;~IDEal Editor Parameters:
;~C#Blitz3D1105