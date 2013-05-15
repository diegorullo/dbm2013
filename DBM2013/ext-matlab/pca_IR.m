function pca_IR
X = csvread('../../data/X.csv');
[pc,score,latent] = princomp(X,'econ');
csvwrite('../../data/pc.csv',pc);
csvwrite('../../data/score.csv',score);
csvwrite('../../data/latent.csv',latent);
end