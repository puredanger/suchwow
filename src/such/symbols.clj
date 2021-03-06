(ns such.symbols
  "Symbol utilities, such as different ways to create symbols."
  (:require [such.casts :as cast]
            [clojure.string :as str]))

(defn +symbol
  "Creates a symbol. A variant of the `clojure.core` version with a wider domain.     
  The `ns` argument may be a namespace, symbol, keyword, or string ([[as-ns-string]]).    
  The `name` argument may be a symbol, string, keyword, or var ([[as-string-without-namespace]]).

  In the one-argument version, the resulting symbol has a `nil` namespace.
  In the two-argument version, it has the symbol version of `ns` as the namespace.
  Note that `ns` need not refer to an existing namespace.

      (+symbol \"th\") => 'th
      (+symbol 'clojure.core \"th\") => 'clojure.core/th

      (+symbol *ns* 'th) => 'this.namespace/th ; \"add\" a namespace
      (+symbol *ns* 'clojure.core/even?) => 'this.namespace/even? ; \"localize\" a symbol.
"
([name]
 (symbol (cast/as-string-without-namespace name)))
([ns name]
  (symbol (str (cast/as-ns-symbol ns)) (cast/as-string-without-namespace name))))



(defn from-concatenation 
  "Construct a symbol from the concatenation of the string versions of the
   `nameables`, which may be symbols, strings, keywords, or vars. If given,
   the `join-nameable` is interposed between the segments.
   
        (symbol/from-concatenation ['a \"b\" :c #'d]) => 'abcd
        (symbol/from-concatenation [\"a\" \"b\"] '-) => 'a-b)
   
   Note that the namespace qualifiers for symbols and strings are not included:
   
        (symbol/from-concatenation [:namespace/un #'clojure.core/even?]) => 'uneven?
"
  ([nameables join-nameable]
     (symbol (str/join (cast/as-string-without-namespace join-nameable)
                       (map cast/as-string-without-namespace nameables))))
  ([nameables]
     (from-concatenation nameables "")))

(defn without-namespace
  "Return a symbol with the same name as `sym` but no 
   namespace.
   
        (symbol/without-namespace 'clojure.core/even?) => 'even?
"
  [sym]
  (symbol (name sym)))
