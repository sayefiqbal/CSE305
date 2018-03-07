
datatype 'a stack = Stack of ('a list)
                    | Empty
  fun push (item, Stack s)        = Stack (item::s)
  fun pop (Stack (first_item::s)) = (first_item, Stack s)
  fun top (Stack (first_item::s)) = (first_item)

fun interpreter (infile : string) =
  let
  fun readlist (infile : string) = let
    val ins = TextIO.openIn infile
    fun loop ins =
     case TextIO.inputLine ins of
        SOME line => line :: loop ins
      | NONE      => []
  in
    loop ins before TextIO.closeIn ins
  end
  in
    readlist(infile)
  end
