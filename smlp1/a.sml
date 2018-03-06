datatype 'a stack = Stack of ('a list)
                    | Empty
  fun push (item, Stack s)        = Stack (item::s)
  fun pop (Stack (first_item::s)) = (first_item, Stack s)
  fun top (Stack (first_item::s)) = (first_item)

fun interpret =
  let
    val _store = Stack nil;

fun interpreter(inFile : string, outFile : string) =
  let
    val func = [];
    val ins = TextIO.openIn inFile
    (* val outs = TextIO.openOut outFile *)
    val lineRead = TextIO.inputLine ins

    fun helper(lineRead: string option) =
      case lineRead of
          NONE => (TextIO.closeIn ins;)
          | SOME(c) => (c::func);
          helper(TextIO.inputLine ins))
  in
    helper(lineRead)
  end
