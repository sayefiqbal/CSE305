datatype 'a inflist = NIL
                    | CONS of 'a * (unit -> 'a inflist);

exception Empty;
exception Subscript;

fun HD (CONS(a,b)) = a
  | HD NIL = raise Empty; 

fun TL (CONS(a,b)) = b()
  | TL NIL = raise Empty;

fun NUL NIL = true
  | NUL _ = false;

fun NTH 0 L = HD L
  | NTH n L = NTH (n-1) (TL L);

fun TAKE (xs, 0) = []
  | TAKE (NIL, n) = raise Subscript
  | TAKE (CONS(x, xf), n) = x::TAKE(xf(), n-1);

fun FROMN n = CONS(n, fn () => FROMN (n+1));

fun FIB n m = CONS(n, fn () => FIB m (n+m));

fun FILTER f l =
  if NUL l
  then NIL
  else if f (HD l)
       then CONS(HD l, fn() => (FILTER f (TL l)))
       else FILTER f (TL l);

fun SIFT NIL = NIL
  | SIFT l =
     let val a = HD l
     in CONS(a, fn () => SIFT(FILTER (fn x => x mod a <> 0) (TL l)))
     end;

	 
(**********************
 *
 * FUNCTION AND INFLIST STUBS -- YOU MUST IMPLEMENT THESE
 * 
 * printList and printPairList must write to the file named by f.
 * Anything printed to the terminal will not be graded.
 *
 **********************)
 
fun even (x : int) : bool = 
  if(x mod 2 = 0) then true
  else false 

fun odd  (x : int) : bool = 
  if(x mod 2 = 0) then false
  else true 

val l = FROMN(0)

val fibs  = FIB 0 1 
val evens = FILTER even l
val odds  = FILTER odd l

fun FROM0 n = CONS(n, fn () => FROM0 (n));

val allZeros = FROM0(0)
val allOnes  = FROM0(1)

val primes   = SIFT(FROMN(2))

fun printGenList (f : ('a -> 'b)) (l : ('a list)) : unit = 
  case l of
    [] => ()
    | (xs::l) => (f(xs); printGenList f l)

fun printList (f : string, l : int list) : unit =
  let
    val outs = TextIO.openOut f
  in
    (printGenList (fn a => TextIO.output(outs, Int.toString(a) ^ " ")) l; TextIO.closeOut(outs))
  end

fun printPairList (f : string, l : (int * int) list) : unit =
  let
    val outs = TextIO.openOut f
  in
    (printGenList (fn a => TextIO.output(outs, "(" ^ Int.toString(#1a) ^ ", " ^ Int.toString(#2a) ^ ") ")) l; TextIO.closeOut(outs))
  end

fun rev_zip (infL1 : 'a inflist, infL2 : 'b inflist) : ('b * 'a) inflist =
  let
    fun ZIP(n,m) = CONS((HD n, HD m), fn () => ZIP (TL n, TL m));
  in
    ZIP(infL2, infL1)
  end
