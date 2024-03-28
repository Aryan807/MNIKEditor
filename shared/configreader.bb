; Blitz3D Configuration Files Reader
; -------------
; By Aryan


; HOW TO USE
; =============
; (there are also two undocumented functions StorePos and FetchPos, which are internal to the config reader)

; Function: ReadConfigFile(path$)
; Arguments:
; 	path$ = Valid file path
; Returns: Handle of the config file opened.
; Description:
; 	This function loads the config file into the memory.
; 	Almost all the file extension types are supported, but the file must be a
; 	valid configuration file. (see below for an example)

; Function: CloseConfigFile(handle%)
; Arguments:
; 	handle% = handle of the file previously opened by ReadConfigFile()
; Description:
;	This function frees the config file from the memory.
;	Handle MUST be a config file opened by ReadConfigFile or the function will fail.

; Function: JumpToSection(handle%,section$)
; Arguments:
; 	handle% = handle of the file previously opened by ReadConfigFile()
; 	section$ = name of the section to jump to (should NOT contain "[" and "]")
; Returns:
; 	True(1) if succeeded in jumping to the section, False(0) if didn't.
; Description:
; 	Jumps to the section in the file. Section must exist in the file, if not, it will return false.

; Function: GetVarValue$(handle%,variablename$)
; Arguments:
; 	handle% = handle of the file previously opened by ReadConfigFile()
; 	variablename$ = name of the variable (which should be in section jumped by JumpToSection) to get the value from.
; Returns:
; 	The value of the variable in the section (previously jumped thorugh JumpToSection) as string,
; Description:
; 	Returns the value of the variable present in the section previously jumped thorugh JumpToSection in the file.
;	Variable must exist in the section, else this function will return an empty string.


; SAMPLE CONFIG FILE (named "sample.txt" for code example below)
; ========================
; [Section]
; Variable1= value1
; Variable2 =value2
;
; [Section2]
; Variable1 : value1
; Variable2 = value2
; Variable3:value3
; =========================
; NOTES:
; 1. As you can see, you can use either colon(:) or equals to(=) for assigning
; 	values to the variables.
; 2. You can have as much spacing and line breaks in the config file you want,
; 	but make sure you do not make any spacing in section names or variable names!
; ==========================


; SIMPLE EXAMPLE CODE FOR LOADING ABOVE FILE
; ========================
; file = ReadConfigFile("sample.txt")
; JumpToSection("Section2")
; var1$ = GetVarValue(file, "Variable1")
; var2$ = GetVarValue(file, "Variable2")
; Print "Section2 has: " + var1 + ", " + var2
; JumpToSection("Section")
; var3$ = GetVarValue(file, "Variable3")
; var1$ = GetVarValue(file, "Variable1")
; var2$ = GetVarValue(file, "Variable2")
; Print "Section has: " + var1 + ", " + var2 + ", " + var3
; CloseConfigFile(file)
; ========================
; NOTES:
; 1. As you can see, you can call any section using JumpToSection, no need to call in chronological order.
; 2. Variable names can also be called in any order.


Type ConfigFile
	Field handler, sectionpos
End Type

Function ReadConfigFile(ex$)
	f = ReadFile(ex$)

	p.ConfigFile = New ConfigFile
	p\handler = f
	p\sectionpos = 0
	
	Return f
End Function

Function CloseConfigFile(f)
	For p.ConfigFile = Each ConfigFile
		If p\handler = f
			Delete p
			Exit
		EndIf 
	Next
	CloseFile f
End Function

Function JumpToSection(f, section$)
	SeekFile f, 0
	While Not Eof(f)
		s$ = Lower(Trim(ReadLine(f)))
		If s$ = "[" + Lower(section$) + "]"
			StorePos(f)
			Return True
		EndIf
	Wend
	SeekFile f, 0
	Return False
End Function

Function GetVarValue$(f,var$)
	SeekFile f, FetchPos(f)
	While Not Eof(f)
		s$ = Lower(Trim(ReadLine(f)))
		If Left(s$, 1) = "[" And Right(s$, 1) = "]"
			SeekFile f, 0
			Return ""
		EndIf
		colonpos = Instr(s$, "=", 1)
		
		If colonpos = 0
			colonpos = Instr(s$, ":", 1)
		EndIf
		
		If colonpos = 0
			; invalid var
			Exit
		EndIf
		
		filevar$ = Trim(Left(s$, colonpos-1))
		If filevar$ = Lower(var$)
			Return Trim(Right(s$, Len(s$) - colonpos))
		EndIf
	Wend
	SeekFile f, 0
	Return ""
End Function

Function StorePos(f)
	For p.ConfigFile = Each ConfigFile
		If p\handler = f
			p\sectionpos = FilePos(f)
			Return
		EndIf
	Next
End Function

Function FetchPos(f)
	For p.ConfigFile = Each ConfigFile
		If p\handler = f
			Return p\sectionpos
		EndIf
	Next
	Return -1
End Function