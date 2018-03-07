(* Writted by: Sayef Iqbal
Help used from
http://www.cs.cornell.edu/courses/cs312/2006fa/
http://sml-family.org/Basis/list.html
http://sml-family.org/Basis/char.html
https://pangram.me/en to check for pangrams
Used functon:
all f l
applies f to each element x of the list l, from left to right, until f x evaluates to false;
it returns false if such an x exists and true otherwise. It is equivalent to not(exists (not o f) l)).

contains s c
returns true if character c occurs in the string s; otherwise it returns false.*)

fun checkString(line: string, cList: char list) =
  if List.all (Char.contains line) cList then "true\n" else "false\n"

fun pangram(inFile : string, outFile : string) =
let
  val ins = TextIO.openIn inFile
  val outs = TextIO.openOut outFile
  val lineRead = TextIO.inputLine ins
  val alphabet = "abcdefghijklmnopqrstuvwxyz"

  fun helper(lineRead: string option) =
    case lineRead of
        NONE => (TextIO.closeIn ins; TextIO.closeOut outs)
        | SOME(c) => (TextIO.output(outs,checkString(c,explode(alphabet)));
        helper(TextIO.inputLine ins))
in
  helper(lineRead)
end
