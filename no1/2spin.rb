class Twospin
	@@initial_probably = [0.7, 0.7]

	def initialize(j=1.0, h1=0.5, h2=0.1, n=10000, t=1.01, tmax=500)
		@j = j
		@h1 = h1
		@h2 = h2
		@n = n
		@t = t
		@tmax = tmax
		@es = []
		@current = 0
		@@initial_probably.each do |p|
			if p > rand()
				@current.push(1)
			else
				@current.push(-1)
			end
		end
		@es.push(@current)
	end

	def a(s)
		-@J*s[0] * s[1] - @h1 * s[0]- @h2 * s[1]
	end

	def e(s)
		a s
	end

	def p(s)
		
	end

	def run
		t = 0
		@tmax.times do |i|

		end
	end


end
