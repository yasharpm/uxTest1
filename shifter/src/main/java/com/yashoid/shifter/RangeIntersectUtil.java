package com.yashoid.shifter;

import java.util.ArrayList;
import java.util.List;

public class RangeIntersectUtil {

    private static final double PI = Math.PI;
    private static final double P2 = 2 * Math.PI;

    public static void intersect(CircleMenu.Range a, CircleMenu.Range b) {
        double a1Start;
        double a1End;
        double a2Start = 0;
        double a2End = 0;

        if (a.start < 0) {
            a1Start = a.start + P2;
            a1End = P2;

            a2Start = 0;
            a2End = a.end;
        }
        else {
            a1Start = a.start;
            a1End = a.end;
        }

        double b1Start;
        double b1End;
        double b2Start = 0;
        double b2End = 0;

        if (b.start < 0) {
            b1Start = b.start + P2;
            b1End = P2;

            b2Start = 0;
            b2End = b.end;
        }
        else {
            b1Start = b.start;
            b1End = b.end;
        }

        double r1Start = Math.max(a1Start, b1Start);
        double r1End = Math.min(a1End, b1End);
        double r2Start = Math.max(a1Start, b2Start);
        double r2End = Math.min(a1End, b2End);
        double r3Start = Math.max(a2Start, b1Start);
        double r3End = Math.min(a2End, b1End);
        double r4Start = Math.max(a2Start, b2Start);
        double r4End = Math.min(a2End, b2End);

        if (r1End < r1Start) {
            r1Start = r1End = 0;
        }

        if (r2End < r2Start) {
            r2Start = r2End = 0;
        }

        if (r3End < r3Start) {
            r3Start = r3End = 0;
        }

        if (r4End < r4Start) {
            r4Start = r4End = 0;
        }

        double c1Start = 0;
        double c1End = 0;
        double c2Start = 0;
        double c2End = 0;

        if (r1Start != r1End) {
            if (r1End == P2) {
                c1Start = r1Start - P2;

                if (r2Start != r2End) {
                    if (r2Start == 0) {
                        c1End = r2End;

                        if (r3Start != r3End) {
                            if (r3Start == 0) {
                                c2End = r3Start;

                                if (r4Start != r4End) {
                                    c2Start = r4Start - P2;
                                }
                            }
                            else if (r3End == P2) {
                                c2Start = r3Start - P2;

                                if (r4Start != r4End) {
                                    c2End = r4End;
                                }
                            }
                            else {
                                c2Start = r3Start;
                                c2End = r3End;
                            }
                        }
                        else {
                            c2Start = r4Start;
                            c2End = r4End;
                        }
                    }
                    else {
                        c2Start = r2Start;
                        c2End = r2End;

                        if (r3Start != r3End) {
                            c1End = r3End;
                        }
                        else {
                            c1End = r4End;
                        }
                    }
                }
                else {
                    if (r3Start != r3End) {
                        if (r3Start == 0) {
                            c1End = r3End;

                            c2Start = r4Start;
                            c2End = r4End;
                        }
                        else {
                            c2Start = r3Start;
                            c2End = r3End;

                            c1End = r4End;
                        }
                    }
                    else {
                        if (r4Start != r4End) {
                            if (r4Start == 0) {
                                c1End = r4End;
                            }
                            else {
                                c2Start = r4Start;
                                c2End = r4End;
                            }
                        }
                    }
                }
            }
            else {
                c1Start = r1Start;
                c1End = r1End;

                if (r2Start != r2End) {
                    if (r2Start == 0) {
                        c2End = r2End;

                        if (r3Start != r3End) {
                            c2Start = r3Start - P2;
                        }
                        else {
                            if (r4Start != r4End) {
                                c2Start = r4Start - P2;
                            }
                        }
                    }
                    else if (r2End == P2) {
                        c2Start = r2Start - P2;

                        if (r3Start != r3End) {
                            c2End = r3End;
                        }
                        else {
                            if (r4Start != r4End) {
                                c2End = r4End;
                            }
                        }
                    }
                    else {
                        c2Start = r2Start;
                        c2End = r2End;
                    }
                }
                else {
                    if (r3Start != r3End) {
                        if (r3Start == 0) {
                            c2End = r3End;

                            if (r4Start != r4End) {
                                c2Start = r4Start - P2;
                            }
                        }
                        else if (r3Start == P2) {
                            c2Start = r3Start - P2;

                            if (r4Start != r4End) {
                                c2End = r4End;
                            }
                        }
                        else {
                            c2Start = r3Start;
                            c2End = r3End;
                        }
                    }
                    else {
                        c2Start = r4Start;
                        c2End = r4End;
                    }
                }
            }
        }
        else {
            if (r2Start != r2End) {
                if (r2Start == 0) {
                    c1End = r2End;

                    if (r3Start != r3End) {
                        if (r3End == P2) {
                            c1Start = r3Start - P2;

                            c2Start = r4Start;
                            c2End = r4End;
                        }
                        else {
                            c2Start = r3Start;
                            c2End = r3End;

                            if (r4Start != r4End) {
                                c1Start = r4Start - P2;
                            }
                        }
                    }
                    else {
                        if (r4Start != r4End) {
                            if (r4End == P2) {
                                c1Start = r4Start - P2;
                            }
                        }
                    }
                }
                else if (r2End == P2) {
                    c1Start = r2Start - P2;

                    if (r3Start != r3End) {
                        if (r3Start == 0) {
                            c1End = r3End;

                            c2Start = r4Start;
                            c2End = r4End;
                        }
                        else {
                            c2Start = r3Start;
                            c2End = r3End;

                            if (r4Start != r4End) {
                                c1End = r4End;
                            }
                        }
                    }
                    else {
                        if (r4Start != r4End) {
                            c2End = r4End;
                        }
                    }
                }
                else {
                    c1Start = r2Start;
                    c1End = r2End;

                    if (r3Start != r3End) {
                        if (r3Start == 0) {
                            c2End = r3End;

                            if (r4Start != r4End) {
                                c2Start = r4Start - P2;
                            }
                        }
                        else if (r3End == P2) {
                            c2Start = r3Start - P2;

                            if (r4Start != r4End) {
                                c2End = r4End;
                            }
                        }
                        else {
                            c2Start = r3Start;
                            c2End = r3End;
                        }
                    }
                    else {
                        c2Start = r4Start;
                        c2End = r4End;
                    }
                }
            }
            else {
                c1Start = r3Start;
                c1End = r3End;

                c2Start = r4Start;
                c2End = r4End;
            }
        }

        double start = c1Start;
        double end = c1End;

        if (c2End - c2Start > end - start) {
            start = c2Start;
            end = c2End;
        }

        a.start = start;
        a.end = end;

        if (1 > 0) {
            return;
        }








        List<Range> flatA = breakRange(a);
        List<Range> flatB = breakRange(b);

        List<Range> intersection = intersect(flatA, flatB);

        List<Range> roundedUps = roundUp(intersection);

        if (roundedUps.size() == 0) {
            a.start = 0;
            a.end = 0;
            return;
        }

        if (roundedUps.size() == 1) {
            Range range = roundedUps.get(0);

            a.start = range.start;
            a.end = range.end;
            return;
        }

        Range range = pickLongestRange(roundedUps);

        a.start = range.start;
        a.end = range.end;
    }

    private static Range pickLongestRange(List<Range> ranges) {
        Range longestRange = null;

        for (Range range: ranges) {
            if (longestRange == null) {
                longestRange = range;
            }
            else if (range.end - range.start > longestRange.end - longestRange.start) {
                longestRange = range;
            }
        }

        return longestRange;
    }

    private static List<Range> roundUp(List<Range> ranges) {
        List<Range> rounds = new ArrayList<>();

        Range startRange = null;
        Range endRange = null;

        for (Range range: ranges) {
            if (range.start == 0) {
                if (endRange != null) {
                    rounds.add(new Range(endRange.start - P2, range.end));

                    endRange = null;
                }
                else {
                    startRange = range;
                }
            }
            else if (range.end == P2) {
                if (startRange != null) {
                    rounds.add(new Range(range.start - P2, startRange.end));

                    startRange = null;
                }
                else {
                    endRange = range;
                }
            }
            else {
                rounds.add(range);
            }
        }

        if (startRange != null) {
            rounds.add(startRange);
        }

        if (endRange != null) {
            rounds.add(endRange);
        }

        return rounds;
    }

    private static List<Range> intersect(List<Range> a, List<Range> b) {
        List<Range> ranges = new ArrayList<>();

        for (Range range: a) {
            ranges.addAll(intersect(range, b));
        }

        return ranges;
    }

    private static List<Range> intersect(Range a, List<Range> b) {
        List<Range> ranges = new ArrayList<>();

        for (Range range: b) {
            Range intersect = intersect(a, range);

            if (intersect != null) {
                ranges.add(intersect);
            }
        }

        return ranges;
    }

    private static Range intersect(Range a, Range b) {
        double start = Math.max(a.start, b.start);
        double end = Math.min(a.end, b.end);

        if (end <= start) {
            return null;
        }

        return new Range(start, end);
    }

    private static List<Range> breakRange(CircleMenu.Range r) {
        ArrayList<Range> ranges = new ArrayList<>();

        double start = r.start;
        double end = r.end;

        if (start < 0) {
            Range range = new Range(r.start, 0);

            range.offset(P2);

            ranges.add(range);

            start = 0;
        }

        Range range = new Range(start, end);

        ranges.add(range);

        return ranges;
    }

    private static class Range {

        private double start;
        private double end;

        private Range() {

        }

        private Range(double start, double end) {
            this.start = start;
            this.end = end;
        }

        private void offset(double offset) {
            start += offset;
            end += offset;
        }

    }

}
