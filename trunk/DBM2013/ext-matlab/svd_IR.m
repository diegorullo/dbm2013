function svd_IR(filename)
path = strcat('../../data/',filename)
X = csvread(path);
[U,S,V] = svd(X,'econ');
csvwrite(strcat('../../data/U_',filename),U);
csvwrite(strcat('../../data/S_',filename),S);
csvwrite(strcat('../../data/V_',filename),V');
end
