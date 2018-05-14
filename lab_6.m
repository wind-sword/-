n=1;
for i=1:1:10
   
        M(n)=var(magic04(:,i)) 
        n=n+1; 
    
end
M;
max(M)
min(M)

