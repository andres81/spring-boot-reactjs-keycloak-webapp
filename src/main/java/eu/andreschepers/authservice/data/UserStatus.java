/*
	Copyright 2018 Andre Schepers

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package eu.andreschepers.authservice.data;

import javax.persistence.*;

/**
 * Created by andres81 on 3/8/17.
 */
@Entity
@Table(name="user_status")
public class UserStatus {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_account_id")
    private UserAccount user;

    @Column(name="account_status")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    public UserStatus() {}

    public UserStatus(UserAccount user, AccountStatus accountStatus) {
        if (user == null ||
            accountStatus == null) throw new NullPointerException();
        this.user = user;
        this.accountStatus = accountStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }
}
