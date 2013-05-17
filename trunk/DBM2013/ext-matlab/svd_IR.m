function svd_IR
X = csvread('../data/X.csv');
[U,S,V] = svd(X,'econ');
csvwrite('../data/U.csv',U);
csvwrite('../data/S.csv',S);
csvwrite('../data/V.csv',V');
end
