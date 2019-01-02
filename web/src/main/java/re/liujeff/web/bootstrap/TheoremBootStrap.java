package re.liujeff.web.bootstrap;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import re.liujeff.web.model.Hypothesis;
import re.liujeff.web.model.Prefix;
import re.liujeff.web.model.Theorem;
import re.liujeff.web.repositories.HypothesisRepository;
import re.liujeff.web.repositories.PrefixRepository;
import re.liujeff.web.repositories.TheoremRepository;

import java.util.*;

@Component
public class TheoremBootStrap implements ApplicationListener<ContextRefreshedEvent> {

    private final TheoremRepository theoremRepository;
    private final HypothesisRepository hypothesisRepository;
    private final PrefixRepository prefixRepository;

    public TheoremBootStrap(TheoremRepository theoremRepository,
                            HypothesisRepository hypothesisRepository,
                            PrefixRepository prefixRepository) {
        this.theoremRepository = theoremRepository;
        this.hypothesisRepository = hypothesisRepository;
        this.prefixRepository = prefixRepository;
    }

    private Prefix findPrefix(String prefix) {
        Optional<Prefix> opt = prefixRepository.findByPrefix(prefix);
        if (!opt.isPresent()) {
            throw new RuntimeException("Expected prefix " + prefix + " not found.");
        }
        return opt.get();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (prefixRepository.count() == 0) {
            prefixRepository.saveAll(getPrefixes());
        }
        if (theoremRepository.count() == 0) {
            theoremRepository.saveAll(getTheorems());
        }
    }

    private List<Prefix> getPrefixes() {
        List<Prefix> prefixes = new ArrayList<>();

        prefixes.add(new Prefix("coll", "collinear"));
        prefixes.add(new Prefix("para", "parallel"));
        prefixes.add(new Prefix("perp", "perpendicular"));
        prefixes.add(new Prefix("midp", "midpoint"));
        prefixes.add(new Prefix("circle", "circle"));
        prefixes.add(new Prefix("cong", "congruent"));
        prefixes.add(new Prefix("cyclic", "concyclic"));
        prefixes.add(new Prefix("eqangle", "equal angles"));
        prefixes.add(new Prefix("eqratio", "equal ratios"));
        prefixes.add(new Prefix("simtri", "similar triangles"));
        prefixes.add(new Prefix("contri", "contri"));
        prefixes.add(new Prefix("tri", "triangle"));

        return prefixes;
    }

    @SafeVarargs
    private final <T> List<T> list(T... arr) {
        if (arr.length == 1) {
            return Collections.singletonList(arr[0]);
        }
        return Arrays.asList(arr);
    }

    private String A = "_A";
    private String B = "_B";
    private String C = "_C";
    private String D = "_D";
    private String E = "_E";
    private String F = "_F";
    private String G = "_G";
    private String H = "_H";
    private String I = "_I";
    private String J = "_J";
    private String K = "_K";
    private String L = "_L";

    private Prefix coll;
    private Prefix para;
    private Prefix perp;
    private Prefix midp;
    private Prefix circle;
    private Prefix cong;
    private Prefix cyclic;
    private Prefix eqratio;
    private Prefix eqangle;
    private Prefix simtri;
    private Prefix contri;
    private Prefix tri;

    private List<Theorem> getTheorems() {
        List<Theorem> theorems = new ArrayList<>();
        A = "_A";
        B = "_B";
        C = "_C";
        D = "_D";
        E = "_E";
        F = "_F";
        G = "_G";
        H = "_H";
        I = "_I";
        J = "_J";
        K = "_K";
        L = "_L";

        coll = findPrefix("coll");
        para = findPrefix("para");
        perp = findPrefix("perp");
        midp = findPrefix("midp");
        circle = findPrefix("circle");
        cong = findPrefix("cong");
        cyclic = findPrefix("cyclic");
        eqratio = findPrefix("eqratio");
        eqangle = findPrefix("eqangle");
        simtri = findPrefix("simtri");
        contri = findPrefix("contri");
        tri = findPrefix("tri");

        addDefinitions(theorems);
        addNDGs(theorems);
        addCommonKnowledge(theorems);

        return theorems;
    }

    private void addDefinitions(List<Theorem> theorems) {
        Theorem congReflexivity = new Theorem(list(new Hypothesis(cong, list(A, B, A, B))),
                Collections.emptyList(), "Congruency is reflexive", "definitions");
        theorems.add(congReflexivity);

//        THESE ARENT GOOD
//         Theorem eqangleReflexivity = new Theorem(list(new Hypothesis(eqangle, list(A, B, C, D, A, B, C, D))),
//                Collections.emptyList(), "Equality of angles is reflexive", "definitions");
//        theorems.add(eqangleReflexivity);
//
//        Theorem eqratioReflexivity = new Theorem(list(new Hypothesis(eqratio, list(A, B, C, D, A, B, C, D))),
//                Collections.emptyList(), "Equality of ratios is reflexive", "definitions");
//        theorems.add(eqratioReflexivity);

        Theorem collTrans = new Theorem(list(new Hypothesis(coll, list(C, D, A))),
                list(new Hypothesis(coll, list(A, B, C)), new Hypothesis(coll, list(A, B, D))),
                "Collinearity is transitive", "definitions");
        theorems.add(collTrans);

        Theorem paraTrans = new Theorem(list(new Hypothesis(para, list(A, B, E, F))),
                list(new Hypothesis(para, list(A, B, C, D)), new Hypothesis(para, list(C, D, E, F))),
                "Parallelism is transitive", "definitions");
        theorems.add(paraTrans);

        // technically this is covered in traversing line theorems?
        Theorem perpToPara = new Theorem(list(new Hypothesis(para, list(A, B, E, F))),
                list(new Hypothesis(perp, list(A, B, C, D)), new Hypothesis(perp, list(C, D, E, F))),
                "Perpendicularity implies parallelism", "definitions");
        theorems.add(perpToPara);

        Theorem paraToPerp = new Theorem(list(new Hypothesis(perp, list(A, B, E, F))),
                list(new Hypothesis(para, list(A, B, C, D)), new Hypothesis(perp, list(C, D, E, F))),
                "Parallel to perpendicular lines", "definitions");
        theorems.add(paraToPerp);

        Theorem circleDef = new Theorem(list(new Hypothesis(circle, list(A, B, C, D))),
                list(new Hypothesis(cong, list(A, B, A, C)), new Hypothesis(cong, list(A, B, A, D))),
                "Fold circle definition", "definitions");
        theorems.add(circleDef);

        Theorem circleDefCon = new Theorem(list(new Hypothesis(cong, list(A, B, A, C))),
                list(new Hypothesis(circle, list(A, B, C, D))),
                "Converse to definition of circle", "definitions");
        theorems.add(circleDefCon);

        Theorem concyclicDef = new Theorem(list(new Hypothesis(cyclic, list(A, B, C, D))),
                list(new Hypothesis(cong, list(E, A, E, B)), new Hypothesis(cong, list(E, A, E, C)),
                        new Hypothesis(cong, list(E, A, E, D))),
                "Definition of concyclic", "definitions");
        theorems.add(concyclicDef);

        Theorem concyclicDefCon = new Theorem(list(new Hypothesis(cong, list(E, A, E, D))),
                list(new Hypothesis(cyclic, list(A, B, C, D)), new Hypothesis(cong, list(E, A, E, B)),
                        new Hypothesis(cong, list(E, A, E, C))),
                "Converse to definition of concyclic", "definitions");
        theorems.add(concyclicDefCon);

        Theorem concyclicTrans = new Theorem(list(new Hypothesis(cyclic, list(B, C, D, E))),
                list(new Hypothesis(cyclic, list(A, B, C, D)), new Hypothesis(cyclic, list(A, B, C, E))),
                "Cyclic property is transitive", "definitions");
        theorems.add(concyclicTrans);

        Theorem eqangleTrans = new Theorem(list(new Hypothesis(eqangle, list(A, B, C, D, I, J, K, L))),
                list(new Hypothesis(eqangle, list(A, B, C, D, E, F, G, H)),
                        new Hypothesis(eqangle, list(E, F, G, H, I, J, K, L))),
                "Equality of angles is transitive", "definitions");
        theorems.add(eqangleTrans);

        Theorem eqratioTrans = new Theorem(list(new Hypothesis(eqratio, list(A, B, C, D, I, J, K, L))),
                list(new Hypothesis(eqratio, list(A, B, C, D, E, F, G, H)),
                        new Hypothesis(eqratio, list(E, F, G, H, I, J, K, L))),
                "Equality of ratios is transitive", "definitions");
        theorems.add(eqratioTrans);

        Theorem congTrans = new Theorem(list(new Hypothesis(cong, list(A, B, E, F))),
                list(new Hypothesis(cong, list(A, B, C, D)), new Hypothesis(cong, list(C, D, E, F))),
                "Congruency of lengths is transitive", "definitions");
        theorems.add(congTrans);

        Theorem simtriTrans = new Theorem(list(new Hypothesis(simtri, list(A, B, C, G, H, I))),
                list(new Hypothesis(simtri, list(A, B, C, D, E, F)), new Hypothesis(simtri, list(D, E, F, G, H, I))),
                "Similarity of triangles is transitive", "definitions");
        theorems.add(simtriTrans);

        Theorem contriTrans = new Theorem(list(new Hypothesis(contri, list(A, B, C, G, H, I))),
                list(new Hypothesis(contri, list(A, B, C, D, E, F)), new Hypothesis(contri, list(D, E, F, G, H, I))),
                "Congruency of triangles is transitive", "definitions");
        theorems.add(contriTrans);

        Theorem paraDef = new Theorem(list(new Hypothesis(para, list(A, B, C, D))),
                list(new Hypothesis(eqangle, list(A, B, E, F, C, D, E, F))),
                "Parallel implies corresponding angles", "definitions");
        theorems.add(paraDef);

        Theorem paraDefConv = new Theorem(list(new Hypothesis(eqangle, list(A, B, E, F, C, D, E, F))),
                list(new Hypothesis(para, list(A, B, C, D))),
                "Corresponding angles implies parallel", "definitions");
        theorems.add(paraDefConv);

        Theorem inscribedAngles = new Theorem(list(new Hypothesis(eqangle, list(C, A, C, B, D, A, D, B))),
                list(new Hypothesis(cyclic, list(A, B, C, D))),
                "Inscribed angles are congruent", "definitions");
        theorems.add(inscribedAngles);

        Theorem inscribedAnglesCon = new Theorem(list(new Hypothesis(cyclic, list(A, B, C, D))),
                list(new Hypothesis(eqangle, list(C, A, C, B, D, A, D, B)),
                        new Hypothesis(tri, list(A, B, C)), new Hypothesis(tri, list(B, C, D)),
                        new Hypothesis(tri, list(A, C, D)), new Hypothesis(tri, list(A, B, D))),
                "Congruent inscribed angles imply concyclic points", "definitions");
        theorems.add(inscribedAnglesCon);

        Theorem congruentChords = new Theorem(list(new Hypothesis(cong, list(A, B, D, E))),
                list(new Hypothesis(cyclic, list(A, B, C, D)),
                        new Hypothesis(cyclic, list(B, C, D, E)),
                        new Hypothesis(cyclic, list(C, D, E, F)),
                        new Hypothesis(eqangle, list(C, A, C, B, F, D, F, E))),
                "Congruent arcs imply congruent chords", "definitions");
        theorems.add(congruentChords);

        // potentially can add an isosceles hypothesis for ease of use?
        Theorem isoscelesAngles = new Theorem(list(new Hypothesis(eqangle, list(C, A, A, B, A, B, B, C))),
                list(new Hypothesis(cong, list(C, A, C, B))),
                "isosceles triangles have congruent angles","definitions");
        theorems.add(isoscelesAngles);

        Theorem isoscelesSides = new Theorem(list(new Hypothesis(cong, list(C, A, C, B))),
                list(new Hypothesis(eqangle, list(C, A, A, B, A, B, B, C))),
                "isosceles triangles have congruent sides","definitions");
        theorems.add(isoscelesSides);

        Theorem aaaSymmetry = new Theorem(list(new Hypothesis(simtri, list(A, B, C, D, E, F))),
                list(new Hypothesis(eqangle, list(A, B, B, C, D, E, E, F)),
                        new Hypothesis(eqangle, list(A, C, C, B, D, F, F, E)),
                        new Hypothesis(tri, list(A, B, C))),
                "AAA triangle symmetry", "definitions");
        theorems.add(aaaSymmetry);

        Theorem aaaAntiSymmetry = new Theorem(list(new Hypothesis(simtri, list(A, B, C, D, E, F))),
                list(new Hypothesis(eqangle, list(A, B, B, C, F, E, E, D)),
                        new Hypothesis(eqangle, list(A, C, C, B, E, F, F, D)),
                        new Hypothesis(tri, list(A, B, C))),
                "AAA Triangle antisymmetry", "definitions");
        theorems.add(aaaAntiSymmetry);

//        SAS SIMILARITY NOT TRUE WITH DIRECTED FULL ANGLES
//        Theorem sasSimilarity = new Theorem(list(new Hypothesis(simtri, list(A, B, C, D, E, F))),
//                list(new Hypothesis(eqangle, list(A, B, B, C, D, E, E, F)),
//                        new Hypothesis(eqratio, list(A, B, B, C, D, E, E, F)),
//                        new Hypothesis(tri, list(A, B, C))),
//                "SAS triangle similarity", "definitions");
//        theorems.add(sasSimilarity);

        Theorem sssSymmetry = new Theorem(list(new Hypothesis(simtri, list(A, B, C, D, E, F))),
                list(new Hypothesis(eqratio, list(A, B, D, E, B, C, E, F)),
                        new Hypothesis(eqratio, list(A, B, D, E, A, C, D, F)),
                        new Hypothesis(tri, list(A, B, C))),
                "SSS triangle symmetry", "definitions");
        theorems.add(sssSymmetry);

        Theorem sssCongruence = new Theorem(list(new Hypothesis(contri, list(A, B, C, D, E, F))),
                list(new Hypothesis(cong, list(A, B, D, E)), new Hypothesis(cong, list(B, C, E, F)),
                        new Hypothesis(cong, list(C, A, F, D)), new Hypothesis(tri, list(A, B, C))),
                "SSS triangle congruence", "definitions");
        theorems.add(sssCongruence);

//        SAS IS NOT CORRECT WITH DIRECTED FULL ANGLES
//        Theorem sasCongruence = new Theorem(list(new Hypothesis(contri, list(A, B, C, D, E, F))),
//                list(new Hypothesis(cong, list(A, B, D, E)), new Hypothesis(cong, list(B, C, E, F)),
//                        new Hypothesis(eqangle, list(A, B, B, C, D, E, E, F)), new Hypothesis(tri, list(A, B, C))),
//                "SAS triangle congruence", "definitions");
//        theorems.add(sasCongruence);

        // technically not needed from similar triangles -> congruent triangles
//        Theorem asaCongruence = new Theorem(list(new Hypothesis(contri, list(A, B, C, D, E, F))),
//                list(new Hypothesis(eqangle, list(A, B, B, C, D, E, E, F)),
//                        new Hypothesis(eqangle, list(A, C, C, B, D, F, F, E)),
//                        new Hypothesis(cong, list(B, C, E, F)), new Hypothesis(tri, list(A, B, C))),
//                "ASA triangle congruence", "definitions");
//        theorems.add(asaCongruence);

//        Theorem aasCongruence = new Theorem(list(new Hypothesis(contri, list(A, B, C, D, E, F))),
//                list(new Hypothesis(eqangle, list(A, B, B, C, D, E, E, F)),
//                        new Hypothesis(eqangle, list(A, C, C, B, D, F, F, E)),
//                        new Hypothesis(cong, list(A, C, D, F)), new Hypothesis(tri, list(A, B, C))),
//                "AAS triangle congruence", "definitions");

        Theorem similarToCongruence = new Theorem(list(new Hypothesis(contri, list(A, B, C, D, E, F))),
                list(new Hypothesis(simtri, list(A, B, C, D, E, F)),
                        new Hypothesis(cong, list(A, B, D, E))),
                "ASA/AAS triangle congruence", "definitions");
        theorems.add(similarToCongruence);

        Theorem congruenceImpliesAll = new Theorem(list(new Hypothesis(simtri, list(A, B, C, D, E, F)),
                new Hypothesis(cong, list(A, B, D, E)), new Hypothesis(cong, list(A, E, D, F)),
                new Hypothesis(cong, list(B, C, E, F))),
                list(new Hypothesis(contri, list(A, B, C, D, E, F))),
                "Unfold congruent triangles", "definitions");
        theorems.add(congruenceImpliesAll);

        Theorem similarImpliesRatios = new Theorem(list(new Hypothesis(eqratio, list(A, B, D, E, B, C, E, F)),
                new Hypothesis(eqratio, list(A, B, D, E, A, C, D, F)),
                new Hypothesis(eqratio, list(A, C, D, F, B, C, E, F))), // technically not needed by transitivity
                list(new Hypothesis(simtri, list(A, B, C, D, E, F))),
                "Unfold similar triangles", "definitions");
        theorems.add(similarImpliesRatios);

        Theorem similarImpliesAngles = new Theorem(list(new Hypothesis(eqangle, list(A, B, B, C, D, E, E, F)),
                new Hypothesis(eqangle, list(A, C, C, B, D, F, F, E))),
                list(new Hypothesis(simtri, list(A, B, C, D, E, F)),
                        new Hypothesis(eqangle, list(B, A, A, C, E, D, D, F))),
                "Unfold similar trianles", "definitions");
        theorems.add(similarImpliesAngles);

        Theorem antisimilarImpliesAngles = new Theorem(list(new Hypothesis(eqangle, list(A, B, B, C, F, E, E, D)),
                new Hypothesis(eqangle, list(A, C, C, B, E, F, F, D))),
                list(new Hypothesis(simtri, list(A, B, C, D, E, F)),
                        new Hypothesis(eqangle, list(B, A, A, C, F, D, D, E))),
                "Unfold similar triangles", "definitions");
        theorems.add(antisimilarImpliesAngles);

        Theorem paraImpliesCollinearity = new Theorem(list(new Hypothesis(coll, list(A, B, C))),
                list(new Hypothesis(para, list(A, B, A, C))),
                "Same slope and intercept", "definitions");
        theorems.add(paraImpliesCollinearity);

        Theorem collinearityImpliesPara = new Theorem(list(new Hypothesis(para, list(A, B, A, C))),
                list(new Hypothesis(coll, list(A, B, C))),
                "Collinear implies parallel", "definitions");
        theorems.add(collinearityImpliesPara);

        Theorem midpointDefinition = new Theorem(list(new Hypothesis(midp, list(A, B, C))),
                list(new Hypothesis(cong, list(A, B, A, C)), new Hypothesis(coll, list(A, B, C))),
                "Fold definition of midpoint", "definitions");
        theorems.add(midpointDefinition);

        Theorem midpointDefinitionCon = new Theorem(list(new Hypothesis(cong, list(A, B, A, C)),
                new Hypothesis(coll, list(A, B, C))),
                list(new Hypothesis(midp, list(A, B, C))),
                "Unfold definition of midpoint", "definitions");
        theorems.add(midpointDefinitionCon);

        // TODO make this ratio specifically equal to 1:2
        Theorem oneTwoRatio = new Theorem(list(new Hypothesis(eqratio, list(E, A, A, B, F, C, C, D))),
                list(new Hypothesis(midp, list(E, A, B)), new Hypothesis(midp, list(F, C, D))),
                "Midpoint ratios", "definitions");
        theorems.add(oneTwoRatio);

        Theorem rightAnglesAreSupplementary = new Theorem(list(new Hypothesis(perp, list(A, B, C, D))),
                list(new Hypothesis(eqangle, list(A, B, C, D, C, D, A, B)), new Hypothesis(tri, list(A, B, C))),
                "Right angles are supplementary", "definitions");
        theorems.add(rightAnglesAreSupplementary);

        // TODO? skipped D72, not sure if actually needed

        Theorem rightAnglesEqualCon = new Theorem(list(new Hypothesis(perp, list(A, B, C, D))),
                list(new Hypothesis(eqangle, list(A, B, C, D, E, F, G, H)), new Hypothesis(perp, list(E, F, G, H))),
                "Converse to all right angles are equal", "definitions");
        theorems.add(rightAnglesEqualCon);

        Theorem zeroAnglesEqualCon = new Theorem(list(new Hypothesis(para, list(A, B, C, D))),
                list(new Hypothesis(eqangle, list(A, B, C, D, E, F, G, H)), new Hypothesis(para, list(E, F, G, H))),
                "Converse to all zero angles are equal", "definitions");
        theorems.add(zeroAnglesEqualCon);

        Theorem rightAnglesEqual = new Theorem(list(new Hypothesis(eqangle, list(A, B, C, D, E, F, G, H))),
                list(new Hypothesis(perp, list(A, B, C, D)), new Hypothesis(perp, list(E, F, G, H))),
                "All right angles are equal", "definitions");
        theorems.add(rightAnglesEqual);

        Theorem zeroAnglesEqual = new Theorem(list(new Hypothesis(eqangle, list(A, B, C, D, E, F, G, H))),
                list(new Hypothesis(para, list(A, B, C, D)), new Hypothesis(para, list(E, F, G, H))),
                "All zero angles are equal", "definitions");
        theorems.add(zeroAnglesEqual);

        Theorem congruentEqualLengths = new Theorem(list(new Hypothesis(cong, list(A, B, C, D))),
                list(new Hypothesis(eqratio, list(A, B, C, D, E, F, G, H)), new Hypothesis(cong, list(E, F, G, H))),
                "Congruent lengths have same ratio", "definitions");
        theorems.add(congruentEqualLengths);

        Theorem angleExtendRay = new Theorem(list(new Hypothesis(eqangle, list(D, A, A, B, D, A, A, C))),
                list(new Hypothesis(coll, list(A, B, C))), "Extended angle", "definitions");
        theorems.add(angleExtendRay);

        Theorem supplementaryAnglesEqual = new Theorem(list(new Hypothesis(eqangle, list(A, B, B, C, D, E, E, F))),
                list(new Hypothesis(eqangle, list(C, B, B, A, F, E, E, D))),
                "Supplements of equal angles are equal", "definitions");
        theorems.add(supplementaryAnglesEqual);
    }

    private void addNDGs(List<Theorem> theorems) {
        Theorem concyclicImpliesTriangle = new Theorem(list(new Hypothesis(tri, list(A, B, C)),
                new Hypothesis(tri, list(A, B, D)), new Hypothesis(tri, list(A, C, D)),
                new Hypothesis(tri, list(B, C, D))), list(new Hypothesis(cyclic, list(A, B, C, D))),
                "NDG", "NDGs");
        theorems.add(concyclicImpliesTriangle);

        Theorem equalAnglesImpliesTriangle = new Theorem(list(new Hypothesis(tri, list(A, B, C))),
                list(new Hypothesis(tri, list(D, E, F)), new Hypothesis(eqangle, list(A, B, B, C, D, E, E, F))),
                "NDG", "NDGs");
        theorems.add(equalAnglesImpliesTriangle);

//        TODO ADD ANOTHER NDG UNEQUAL(A, B) WHICH ENSURES A NOT EQUAL B
//        potential theorems include stuff like
//        unequal(a, b) <-| unequal(a, c), midp(b, a, c)
//        etc
//        NOT GUARANTEED UNLESS A NOT EQUAL B
//        Theorem externalAngleImpliesTriangle = new Theorem(list(new Hypothesis(tri, list(A, B, D))),
//                list(new Hypothesis(tri, list(B, C, D)), new Hypothesis(coll, list(A, B, C))),
//                "NDG", "NDGs");
//        theorems.add(externalAngleImpliesTriangle);

        // SIMILARLY BCD IS TRIANGLE IF D NOT EQUAL C BUT ALAS
        Theorem paraTriangleImpliesTriangle = new Theorem(list(new Hypothesis(tri, list(A, B, D))),
                list(new Hypothesis(tri, list(A, B, C)), new Hypothesis(para, list(D, C, A, B))),
                "NDG", "NDGs");
        theorems.add(paraTriangleImpliesTriangle);
    }

    /**
     * Adds many theorems commonly taught at the high school level.
     * @param theorems - the list to add things to
     */
    private void addCommonKnowledge(List<Theorem> theorems) {
        // technically special case of the more general one
        Theorem thales = new Theorem(list(new Hypothesis(para, list(D, E, B, C))),
                list(new Hypothesis(midp, list(D, A, B)), new Hypothesis(midp, list(E, A, C))),
                "Thale's parallel theorem", "common");
        theorems.add(thales);

        Theorem thalesGen = new Theorem(list(new Hypothesis(para, list(D, E, B, C))),
                list(new Hypothesis(eqratio, list(A, D, A, B, A, E, A, C)),
                        new Hypothesis(coll, list(A, E, C)),
                        new Hypothesis(coll, list(A, D, B))),
                "Thale's parallel theorem", "common");
        theorems.add(thalesGen);

        // technically special case of the more general one
        Theorem thalesCon = new Theorem(list(new Hypothesis(midp, list(E, A, C))),
                list(new Hypothesis(para, list(D, E, B, C)), new Hypothesis(midp, list(D, A, B)),
                        new Hypothesis(coll, list(A, E, C))),
                "Converse to Thale's Parallel Theorem", "common");
        theorems.add(thalesCon);

        Theorem thalesGenCon = new Theorem(list(new Hypothesis(eqratio, list(A, D, A, B, A, E, A, C))),
                list(new Hypothesis(para, list(D, E, B, C)), new Hypothesis(coll, list(A, D, B)),
                        new Hypothesis(coll, list(A, E, C))),
                "Converse to Thale's Parallel Theorem", "common");
        theorems.add(thalesGenCon);

        Theorem tangentAngle = new Theorem(list(new Hypothesis(eqangle, list(D, A, A, B, A, C, C, B))),
                list(new Hypothesis(circle, list(E, A, B, C)), new Hypothesis(perp, list(E, A, A, D))),
                "Inscribed angle to tangent", "common");
        theorems.add(tangentAngle);

        Theorem tangentAngleCon = new Theorem(list(new Hypothesis(perp, list(E, A, A, D))),
                list(new Hypothesis(circle, list(E, A, B, C)), new Hypothesis(eqangle, list(D, A, A, B, A, C, C, B))),
                "Converse to inscribed angle to tangent", "common");
        theorems.add(tangentAngleCon);

        Theorem inscribedAngleMeasure = new Theorem(list(new Hypothesis(eqangle, list(B, A, A, C, B, E, E, D))),
                list(new Hypothesis(circle, list(E, A, B, C)), new Hypothesis(midp, list(D, B, C))),
                "Inscribed angle is half the arc", "common");
        theorems.add(inscribedAngleMeasure);

        Theorem inscribedAngleMeasureCon = new Theorem(list(new Hypothesis(midp, list(D, B, C))),
                list(new Hypothesis(circle, list(E, A, B, C)), new Hypothesis(eqangle, list(B, A, A, C, B, E, E, D)),
                        new Hypothesis(coll, list(D, B, C))),
                "Arc is twice the inscribed angle", "common");
        theorems.add(inscribedAngleMeasureCon);

        Theorem thalesCircle = new Theorem(list(new Hypothesis(cong, list(A, D, B, D))),
                list(new Hypothesis(perp, list(A, B, B, C)), new Hypothesis(midp, list(D, A, C))),
                "Thale's circle theorem", "common");
        theorems.add(thalesCircle);

        Theorem thalesCircleCon = new Theorem(list(new Hypothesis(perp, list(A, B, B, C))),
                list(new Hypothesis(circle, list(D, A, B, C)), new Hypothesis(midp, list(D, A, C))),
                "Converse to Thale's circle theorem", "common");
        theorems.add(thalesCircleCon);

        Theorem inscribedTrapezoid = new Theorem(list(new Hypothesis(eqangle, list(A, D, D, C, D, C, C, B)),
                new Hypothesis(cong, list(A, D, B, C))),
                list(new Hypothesis(cyclic, list(A, B, C, D)), new Hypothesis(para, list(A, B, C, D))),
                "Inscribed trapezoid", "common");
        theorems.add(inscribedTrapezoid);

        Theorem isoscelesBisector = new Theorem(list(new Hypothesis(eqangle, list(A, C, C, D, D, C, C, B)),
                new Hypothesis(cong, list(A, C, C, B))),
                list(new Hypothesis(midp, list(D, A, B)), new Hypothesis(perp, list(A, B, C, D))),
                "Bisector in isosceles triangle", "common");
        theorems.add(isoscelesBisector);

        // converse to isosceles bisector is a degenerate form of the following
        Theorem kiteDiagonals = new Theorem(list(new Hypothesis(perp, list(A, B, C, D))),
                list(new Hypothesis(cong, list(A, C, B, C)), new Hypothesis(cong, list(A, D, B, D))),
                "Diagonals in a kite", "common");
        theorems.add(kiteDiagonals);

        Theorem rightKite = new Theorem(list(new Hypothesis(perp, list(C, A, A, D))),
                list(new Hypothesis(cyclic, list(A, B, C, D)), new Hypothesis(cong, list(A, C, B, C)),
                        new Hypothesis(cong, list(A, D, B, D))),
                "A cyclic kite is right", "common");
        theorems.add(rightKite);

        Theorem parallelogramDiagonalsCon = new Theorem(list(new Hypothesis(para, list(A, C, B, D)),
                new Hypothesis(para, list(A, D, B, C))),// technically not needed since C<->D symmetry
                list(new Hypothesis(midp, list(E, A, B)), new Hypothesis(midp, list(E, C, D))),
                "Parallelogram's diagonals bisect converse", "common");
        theorems.add(parallelogramDiagonalsCon);

        Theorem parallelogramDiagonals = new Theorem(list(new Hypothesis(midp, list(E, C, D))),
                list(new Hypothesis(midp, list(E, A, B)), new Hypothesis(para, list(A, C, B, D)),
                        new Hypothesis(para, list(A, D, B, C))),
                "Parallelogram's diagonals bisect", "common");
        theorems.add(parallelogramDiagonals);

        Theorem parallelogramsDiagonalsVerbose = new Theorem(list(new Hypothesis(midp, list(E, C, D)),
                new Hypothesis(midp, list(E, A, B))),
                list(new Hypothesis(para, list(A, C, B, D)), new Hypothesis(para, list(A, D, B, C)),
                        new Hypothesis(coll, list(E, C, D)), new Hypothesis(coll, list(E, A, B))),
                "Parallelogram's diagonals bisect", "common");
        theorems.add(parallelogramsDiagonalsVerbose);

    }

}
