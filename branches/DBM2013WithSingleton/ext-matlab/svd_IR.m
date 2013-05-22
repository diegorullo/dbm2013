function svd_IR(filename)
functionpath = strcat('../data/',filename);
X = csvread(functionpath);
[U,S,V] = svd(X,'econ');
csvwrite(strcat('../data/U_',filename),U);
csvwrite(strcat('../data/S_',filename),S);
csvwrite(strcat('../data/V_',filename),V');
end
