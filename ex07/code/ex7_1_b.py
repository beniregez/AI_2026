from typing import Set
import copy

class dom:
    def __init__(self, values: set):
        self.values = values

    def __str__(self):
        res = "<"
        for v_idx, v in enumerate(sorted(self.values)):
            res += v
            if v_idx != len(self.values) - 1:
                res += ", "
        res += ">"
        return res

    def remove_value(self, value):
        self.values.remove(value)

class Variable:
    def __init__(self, name: str, domain: dom):
        self.name = name
        self.domain = domain
    
    def __str__(self):
        return f"var: {self.name}, dom({self.name}) = {self.domain}"

class Tuple:
    def __init__(self, value1, value2):
        self.value1 = value1
        self.value2 = value2
    
    def __str__(self):
        return f"<{self.value1}, {self.value2}>"

def reverse_tuples(tuples):
    return {Tuple(t.value2, t.value1) for t in tuples}

class R:
    def __init__(self, var1: Variable, var2: Variable, tuples: Set[Tuple]):
        self.var1 = var1
        self.var2 = var2
        self.tuples = tuples

    def __str__(self):
        res = f"R_({self.var1.name}{self.var2.name}) = {{"
        i = 0
        for t in self.tuples:
            res += f"{t}"
            if i != len(self.tuples) - 1:
                res += ", "
            i += 1
        res += f"}}"
        return res

class C:
    def __init__(self, variables: Set[str], relations: Set[R]):
        self.variables = variables
        self.relations = relations

        self.relation_map = {(r.var1, r.var2): r for r in relations}

def revise_3(c: C, u: Variable, v: Variable, w: Variable):
    r_uv = c.relation_map.get((u, v))
    r_uw = c.relation_map.get((u, w))
    r_vw = c.relation_map.get((v, w))

    if r_uv is None or r_uw is None or r_vw is None:
        return c, False

    removed = False
    tuples_to_remove = set()

    for t_uv in list(r_uv.tuples):
        du = t_uv.value1
        dv = t_uv.value2

        supported = False
        for dw in w.domain.values:
            ok_uw = any(t.value1 == du and t.value2 == dw for t in r_uw.tuples)
            if not ok_uw:
                continue

            ok_vw = any(t.value1 == dv and t.value2 == dw for t in r_vw.tuples)
            if ok_vw:
                supported = True
                break

        if not supported:
            tuples_to_remove.add(t_uv)

    for t in tuples_to_remove:
        r_uv.tuples.remove(t)
        removed = True

    return c, removed

def pc_2(c: C):
    queue = []
    variables = list(c.variables)

    for i in range(len(variables)):
        for j in range(i + 1, len(variables)):
            u = variables[i]
            v = variables[j]
            for w in variables:
                if w is not u and w is not v:
                    queue.append((u, v, w))
                    queue.append((v, u, w))

    iteration = 1

    while queue:
        u, v, w = queue.pop(0)
        new_c, changed = revise_3(c, u, v, w)

        if changed:
            print(f"\n=== Iteration {iteration} ===")
            print(f"revise_3({u.name}, {v.name}, {w.name}) executed.")
            print("Changed relation:")
            print(new_c.relation_map[(u, v)])
            iteration += 1

            c = new_c

            for w2 in c.variables:
                if w2 is not u and w2 is not v:
                    queue.append((w2, u, v))
                    queue.append((w2, v, u))

    return c


dom_all = dom({"r", "b", "g"})
v1 = Variable("v1", dom({"r"}))
v2 = Variable("v2", copy.deepcopy(dom_all))
v3 = Variable("v3", copy.deepcopy(dom_all))
v4 = Variable("v4", dom({"b"}))
v5 = Variable("v5", copy.deepcopy(dom_all))

variables = set()
variables.add(v1), variables.add(v2), variables.add(v3), variables.add(v4), variables.add(v5)

tuples = {Tuple(a, b) for a in "rgb" for b in "rgb" if a != b}
relations = set()
relations.add(R(v1, v2, copy.deepcopy(tuples)))
relations.add(R(v2, v1, reverse_tuples(tuples)))

relations.add(R(v1, v3, copy.deepcopy(tuples)))
relations.add(R(v3, v1, reverse_tuples(tuples)))

relations.add(R(v2, v3, copy.deepcopy(tuples)))
relations.add(R(v3, v2, reverse_tuples(tuples)))

relations.add(R(v2, v5, copy.deepcopy(tuples)))
relations.add(R(v5, v2, reverse_tuples(tuples)))

relations.add(R(v3, v4, copy.deepcopy(tuples)))
relations.add(R(v4, v3, reverse_tuples(tuples)))

relations.add(R(v4, v5, copy.deepcopy(tuples)))
relations.add(R(v5, v4, reverse_tuples(tuples)))

c_net = C(
    variables=variables,
    relations=relations
)

final_c = pc_2(c_net)

print("\n=== Final Constraint Network C'' ===")
for r in final_c.relations:
    print(r)

c_check = pc_2(final_c)

# For Debugging:
# set1 = set()
# set1.add("a")
# set1.add("b")
# set1.add("c")

# test_dom = dom(set1)
# test_var = Variable("v0", test_dom)
# print(test_var)

# v1 = Variable("v1", test_dom.copy())
# v2 = Variable("v2", test_dom.copy())

# tuples = set()
# tuples.add(Tuple("r", "g"))
# tuples.add(Tuple("g", "r"))
# tuples.add(Tuple("r", "b"))
# tuples.add(Tuple("b", "r"))
# tuples.add(Tuple("b", "g"))
# tuples.add(Tuple("g", "b"))

# test_relation = R(v1, v2, copy.deepcopy(tuples))
# print(test_relation)