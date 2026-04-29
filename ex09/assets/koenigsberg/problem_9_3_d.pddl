(define (problem euler-koenigsberg)

 (:domain bridges)

 (:objects
  N1
  N2
  N3
  N4
  N5
  N6
  N7
  N8

  V13
  V14
  V15
  V18
  V23
  V26
  V36
  V38
  V46
  V47
  V48
  V57
  V68
  )



 (:init
  (IS-LAND-MASS N1)
  (IS-LAND-MASS N2)
  (IS-LAND-MASS N3)
  (IS-LAND-MASS N4)
  (IS-LAND-MASS N5)
  (IS-LAND-MASS N6)
  (IS-LAND-MASS N7)
  (IS-LAND-MASS N8)

  (IS-BRIDGE V13)
  (IS-BRIDGE V14)
  (IS-BRIDGE V15)
  (IS-BRIDGE V18)
  (IS-BRIDGE V23)
  (IS-BRIDGE V26)
  (IS-BRIDGE V36)
  (IS-BRIDGE V38)
  (IS-BRIDGE V46)
  (IS-BRIDGE V47)
  (IS-BRIDGE V48)
  (IS-BRIDGE V57)
  (IS-BRIDGE V68)

;   (IS-BRIDGE V13)
  (CONNECTS V13 N1 N3)
  (CONNECTS V13 N3 N1)
;   (IS-BRIDGE V14)
  (CONNECTS V14 N1 N4)
  (CONNECTS V14 N4 N1)
;   (IS-BRIDGE V15)
  (CONNECTS V15 N1 N5)
  (CONNECTS V15 N5 N1)
;   (IS-BRIDGE V18)
  (CONNECTS V18 N1 N8)
  (CONNECTS V18 N8 N1)
;   (IS-BRIDGE V23)
  (CONNECTS V23 N2 N3)
  (CONNECTS V23 N3 N2)
;   (IS-BRIDGE V26)
  (CONNECTS V26 N2 N6)
  (CONNECTS V26 N6 N2)
;   (IS-BRIDGE V36)
  (CONNECTS V36 N3 N6)
  (CONNECTS V36 N6 N3)
;   (IS-BRIDGE V38)
  (CONNECTS V38 N3 N8)
  (CONNECTS V38 N8 N3)
;   (IS-BRIDGE V46)
  (CONNECTS V46 N4 N6)
  (CONNECTS V46 N6 N4)
;   (IS-BRIDGE V47)
  (CONNECTS V47 N4 N7)
  (CONNECTS V47 N7 N4)
;   (IS-BRIDGE V48)
  (CONNECTS V48 N4 N8)
  (CONNECTS V48 N8 N4)
;   (IS-BRIDGE V57)
  (CONNECTS V57 N5 N7)
  (CONNECTS V57 N7 N5)
;   (IS-BRIDGE V68)
  (CONNECTS V68 N6 N8)
  (CONNECTS V68 N8 N6)

  (IS-BRIDGE V13)
  (IS-BRIDGE V14)
  (IS-BRIDGE V15)
  (IS-BRIDGE V18)
  (IS-BRIDGE V23)
  (IS-BRIDGE V26)
  (IS-BRIDGE V36)
  (IS-BRIDGE V38)
  (IS-BRIDGE V46)
  (IS-BRIDGE V47)
  (IS-BRIDGE V48)
  (IS-BRIDGE V57)
  (IS-BRIDGE V68)

  (bridge-has-not-been-used V13)
  (bridge-has-not-been-used V14)
  (bridge-has-not-been-used V15)
  (bridge-has-not-been-used V18)
  (bridge-has-not-been-used V23)
  (bridge-has-not-been-used V26)
  (bridge-has-not-been-used V36)
  (bridge-has-not-been-used V38)
  (bridge-has-not-been-used V46)
  (bridge-has-not-been-used V47)
  (bridge-has-not-been-used V48)
  (bridge-has-not-been-used V57)
  (bridge-has-not-been-used V68)

  (is-current-location N1)
  )

 (:goal
  (and
   (bridge-has-been-used V13)
   (bridge-has-been-used V14)
   (bridge-has-been-used V15)
   (bridge-has-been-used V18)
   (bridge-has-been-used V23)
   (bridge-has-been-used V26)
   (bridge-has-been-used V36)
   (bridge-has-been-used V38)
   (bridge-has-been-used V46)
   (bridge-has-been-used V47)
   (bridge-has-been-used V48)
   (bridge-has-been-used V57)
   (bridge-has-been-used V68)
   (is-current-location N1)
   ))
)
