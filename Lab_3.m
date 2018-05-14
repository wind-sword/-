M=[];
for k=1:1:3
    for i=1:1:3
        M(k,i)=cross(magic04(:,k),magic04(:,i))
    end
end
